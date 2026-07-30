# jsf-mq

基于 RocketMQ 5.5 封装的通用消息中间件模块，统一生产者 API、消费者基类，并提供可靠的消息投递与消费兜底能力。

## 功能特性

- **统一生产者** — 同步 / 异步 / 单向 / 延迟四类发送，异常统一包装为 `MqProducerException`
- **消费者基类** — 继承 `AbstractMqConsumer`，返回 `SUCCESS / RETRY_LATER / DISCARD` 三态结果
- **消费幂等** — 可选 `idempotentKey` 启用去重（依赖 `MqIdempotentStore` 实现）
- **可靠发送（Outbox）** — 事务内落库 + 提交后立即投递 + relay 兜底补发，弥合"本地写库与发消息"的原子性缺口
- **失败兜底** — `DISCARD` 消息落库 + 手动/定时 `replay` 重放补偿

## 模块与引入依赖

jsf-mq 由 2 个模块组成，core 含生产者与消费者核心（异常、配置各归各侧），mongodb 提供两侧共用的可靠性落库实现，按需引入：

| 模块 | 职责 | 何时引入 |
|------|------|---------|
| `jsf-mq-core` | 统一生产者 API、Outbox 可靠发送、消费者基类、失败落库重放、幂等抽象 | 需要发/收消息时（必选） |
| `jsf-mq-mongodb` | Outbox / 幂等去重 / 消费失败记录的 MongoDB 落库实现 | 使用 Outbox 可靠发送、启用消费幂等 / 失败重放时 |

前置条件：通过 [jsf-bom](../../jsf-bom/README.md) 管理依赖版本（继承 jsf-parent 或导入 jsf-dependencies BOM）。

```xml
<!-- 发/收消息核心（必选） -->
<dependency>
    <groupId>io.soil.jsf</groupId>
    <artifactId>jsf-mq-core</artifactId>
</dependency>

<!-- 可靠性组件 MongoDB 落库实现（按需） -->
<dependency>
    <groupId>io.soil.jsf</groupId>
    <artifactId>jsf-mq-mongodb</artifactId>
</dependency>
```

> 仅引入 `jsf-mq-core` 也能发/收消息；`jsf-mq-mongodb` 提供 MongoDB 落库实现，引入后 `MqOutbox` / `MqOutboxRelay` / `MqConsumeFailureHandler` 才会被条件装配。
> RocketMQ 连接信息默认复用 `rocketmq.name-server`，生产者/消费者也可各自独立配置 NameServer。

## 配置

生产者与消费者配置完全独立（前缀分别为 `jsf.mq.producer` / `jsf.mq.consumer`）：

| 配置项 | 默认值 | 说明 |
|--------|--------|------|
| `jsf.mq.producer.group` | `jsf-mq-producer` | 默认生产者组 |
| `jsf.mq.consumer.enabled` | `true` | 是否启用消费者（本地开发可关闭避免连接 RocketMQ） |
| `jsf.mq.producer.outbox.immediate-send` | `true` | 事务提交后立即投递（false 则纯 relay 轮询） |
| `jsf.mq.producer.outbox.max-attempts` | `16` | 最大发送尝试，耗尽置 FAILED（人工介入） |
| `jsf.mq.producer.outbox.lock-seconds` | `60` | 认领锁时长（秒），SENDING 超时视为僵尸行 |
| `jsf.mq.producer.outbox.initial-backoff-seconds` | `10` | 首次退避（秒），同时作为 insert 时 relay 缓冲 |
| `jsf.mq.producer.outbox.max-backoff-seconds` | `3600` | 退避上限（秒） |
| `jsf.mq.producer.outbox.relay.enabled` | `true` | relay 兜底开关（需应用 `@EnableScheduling`） |
| `jsf.mq.producer.outbox.relay.interval` | `5000` | 扫描间隔（毫秒） |
| `jsf.mq.producer.outbox.relay.batch-size` | `100` | 单轮扫描批量 |

完整配置项见 [生产者配置属性](../../docs/jsf-docs/skills/jsf-mq-doc/references/io.soil.jsf.mq.config.properties/MqProducerProperties.md) 与 [消费者配置属性](../../docs/jsf-docs/skills/jsf-mq-doc/references/io.soil.jsf.mq.config.properties/MqConsumerProperties.md)。

## 快速开始

### 1. 生产者（直发）

```java
@Component
public class OrderEventPublisher {

    @Autowired
    private MqProducer mqProducer;

    public void publish(OrderCreatedEvent event) {
        mqProducer.send("order-topic:created", event);                // 同步
        mqProducer.sendDelay("order-topic:created", event, 3);         // 延迟（delayLevel 1~18）
    }
}
```

### 2. 消费者（三态结果 + 幂等）

```java
@Component
@RocketMQMessageListener(topic = "order-topic", consumerGroup = "order-group")
public class OrderConsumer extends AbstractMqConsumer<OrderCreatedEvent> {

    @Autowired
    private OrderService orderService;

    @Override
    protected ConsumeStatus handleMessage(OrderCreatedEvent msg, MqConsumeContext ctx) throws Exception {
        try {
            orderService.process(msg);
            return ConsumeStatus.SUCCESS;
        } catch (TransientException e) {     // 瞬时故障
            return ConsumeStatus.RETRY_LATER; // 抛 MqConsumerException 触发 broker 重试
        } catch (InvalidDataException e) {   // 不可重试
            return ConsumeStatus.DISCARD;     // 落库 + 待重放
        }
    }

    // 启用幂等去重（需容器存在 MqIdempotentStore 实现，如引入 jsf-mq-mongodb）
    @Override
    protected String idempotentKey(OrderCreatedEvent msg, MqConsumeContext ctx) {
        return msg.getOrderId();
    }
}
```

> `RETRY_LATER` 由 broker 自动指数退避重试（默认 16 次后进 `%DLQ%+group` 死信队列）；**最后一次重试（即将进死信队列前）会由 `MqConsumeFailureHandler` 落库**（reason=`RETRY_EXHAUSTED`），避免异常类失败无声丢失。`DISCARD` 由 `MqConsumeFailureHandler` 落库（reason=`DISCARDED`）。两类失败均供后续重放。

### 2.1 消息元信息（metadata）

jsf-mq 不把业务 payload 包成信封，而是用 RocketMQ 的 **keys / 用户属性** 承载元信息，链路字节保持纯业务 JSON，兼容 Outbox 与既有消费者。

**发送侧** —— 通过 `MqSendOptions` 设置 keys（业务键 / 幂等键）与自定义属性（如 traceId / eventType）：

```java
mqProducer.send("order-topic", "created", MqSendOptions.builder()
        .keys(order.getOrderId())                       // consumer 侧 getKeys() 可读
        .property("traceId", traceId)                   // consumer 侧 getProperty("traceId") 可读
        .build(), event);
```

**消费侧** —— 实现带 `MqConsumeContext` 的 `handleMessage` 读取元信息（`ctx` 始终可用，不使用时忽略即可）：

```java
@Override
protected ConsumeStatus handleMessage(OrderCreatedEvent msg, MqConsumeContext ctx) throws Exception {
    log.info("收到消息 msgId={} keys={} traceId={}", ctx.getMessageId(), ctx.getKeys(), ctx.getProperty("traceId"));
    orderService.process(msg);   // 业务不变，msg 即反序列化后的业务体
    return ConsumeStatus.SUCCESS;
}

// 用发送方 keys 作为幂等键（需引入 jsf-mq-mongodb）
@Override
protected String idempotentKey(OrderCreatedEvent msg, MqConsumeContext ctx) {
    return ctx.getKeys();
}
```

> `MqConsumeContext` 提供 `messageId / topic / tag / bornTimestamp / keys / properties`；`topic`、`tag` 取消息真实值（而非消费端 selector 表达式），`DISCARD` 落库的记录也更准确。

### 3. 可靠发送（Outbox）

业务写库与发消息需原子时，用 Outbox 替代直发：

```java
@Service
public class OrderService {

    @Autowired
    private MqOutbox mqOutbox;

    @Transactional
    public void createOrder(OrderCmd cmd) {
        orderRepo.save(buildOrder(cmd));
        mqOutbox.save("order-topic", "created", new OrderCreatedEvent(cmd.getOrderId()));
        // 事务内落库，事务提交后自动立即投递；失败由 MqOutboxRelay 兜底
    }
}
```

Outbox 语义为 at-least-once，消费端务必配合幂等。

### 4. 消费失败重放

`DISCARD` 落库的失败记录不会自动重放，需自行接入定时任务或管理端接口：

```java
@Component
@RequiredArgsConstructor
public class FailureReplayJob {

    private final MqConsumeFailureHandler failureHandler;
    private final ObjectMapper objectMapper;

    @Scheduled(fixedDelay = 30_000)
    public void replay() {
        int n;
        while ((n = failureHandler.replay(100, r -> {
            MyPayload p = objectMapper.readValue(r.getPayload(), MyPayload.class);
            bizService.process(p);   // 重放需保证幂等
        })) > 0) {
            log.info("重放 {} 条", n);
        }
    }
}
```

## 可靠性组件装配

可靠性组件按 Store Bean 存在性条件装配（依赖倒置），自动配置类分布如下：

| 自动配置类（所在模块） | 容器存在 | 注册 Bean |
|------------------------|----------|-----------|
| `MqProducerAutoConfig`（jsf-mq-core） | 始终 | `MqProducer`、配置属性 `MqProducerProperties` |
| `MqProducerAutoConfig`（jsf-mq-core） | `MqOutboxStore`（引入 jsf-mq-mongodb） | `MqOutbox` + `MqOutboxRelay`（需 `@EnableScheduling`） |
| `MqConsumerAutoConfig`（jsf-mq-core） | 始终 | 配置属性 `MqConsumerProperties` |
| `MqConsumerAutoConfig`（jsf-mq-core） | `MqConsumeFailureStore`（引入 jsf-mq-mongodb） | `MqConsumeFailureHandler` |

所有 Bean 均 `@ConditionalOnMissingBean`，业务可自定义实现覆盖。各 Store 接口由 `jsf-mq-mongodb` 以 MongoDB 实现，未来可新增其他存储实现而核心无需改动。

## 设计文档

- 完整可靠性设计：[doc/mq-reliability-design.md](doc/mq-reliability-design.md)
- SDK 参考 skill：`../../docs/jsf-docs/skills/jsf-mq-doc/`

## 文档 Skill 引入

如需在 AI 编码助手中引入 jsf-mq SDK 参考：

```bash
pnpm dlx skills add https://github.com/zeno-common/java-service-framework-doc --skill jsf-mq-doc
```
