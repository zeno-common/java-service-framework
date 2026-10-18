# jsf-mq-core

jsf-mq 的核心模块，提供生产者统一 API、消费者基类、Outbox 可靠发送骨架、消费失败落库与重放、幂等存储抽象。整体设计与使用见 [jsf-mq/README.md](../README.md)。

> 本模块只定义**接口与核心逻辑**，Outbox / 幂等 / 失败落库的具体存储由外部实现（如 [jsf-mq-mongodb](../jsf-mq-mongodb/README.md)）。仅引入 core 即可完成基础的发/收消息。

## 提供的能力

| 分类 | 关键类型 | 说明 |
|------|----------|------|
| 生产者 | `MqProducer` | 同步 / 异步 / 单向 / 延迟四类发送，异常统一包装为 `MqProducerException` |
| 发送选项 | `MqSendOptions` | 承载 keys（业务/幂等键）与自定义属性（traceId 等） |
| 消费者 | `AbstractMqConsumer` | 消费基类，返回 `ConsumeStatus`（SUCCESS / RETRY_LATER / DISCARD）三态 |
| 消费上下文 | `MqConsumeContext` | 提供 messageId / topic / tag / bornTimestamp / keys / properties |
| 可靠发送 | `MqOutbox` / `MqOutboxRelay` / `MqOutboxStore`(接口) | 事务内落库 + 提交后投递 + relay 兜底补发 |
| 失败兜底 | `MqConsumeFailureHandler` / `MqConsumeFailureStore`(接口) | DISCARD 与重试耗尽记录落库 + 手动/定时重放 |
| 幂等 | `MqIdempotentStore`(接口) | 消费去重抽象 |
| 异常 | `MqProducerException` / `MqConsumerException` | 生产/消费侧异常 |

## 引入依赖

前置条件：通过 [jsf-bom](../../../jsf-bom/README.md) 管理依赖版本。

```xml
<dependency>
    <groupId>io.soil.jsf</groupId>
    <artifactId>jsf-mq-core</artifactId>
</dependency>
```

> RocketMQ 5.5 客户端（`rocketmq-client`）与 `rocketmq-spring-boot-starter` 由本模块传递引入；连接信息默认复用 `rocketmq.name-server`。

## 自动配置

| 自动配置类 | 注册 Bean | 生效条件 |
|------------|-----------|----------|
| `MqProducerAutoConfig` | `MqProducer`、`MqProducerProperties` | 始终 |
| `MqProducerAutoConfig` | `MqOutbox` + `MqOutboxRelay` | 容器存在 `MqOutboxStore`（引入 jsf-mq-mongodb） |
| `MqConsumerAutoConfig` | `MqConsumerProperties` | 始终 |
| `MqConsumerAutoConfig` | `MqConsumeFailureHandler` | 容器存在 `MqConsumeFailureStore`（引入 jsf-mq-mongodb） |

所有 Bean 均 `@ConditionalOnMissingBean`，业务可自定义实现覆盖。`MqOutboxStore` / `MqConsumeFailureStore` / `MqIdempotentStore` 为依赖倒置接口，由存储实现模块提供。

## 配置项前缀

生产者与消费者配置完全独立，前缀分别为 `jsf.mq.producer` / `jsf.mq.consumer`，完整项见 [jsf-mq/README.md](../README.md#配置)。

## 项目结构

```
io.soil.jsf.mq/
├── config/           MqProducerAutoConfig, MqConsumerAutoConfig, properties/
├── core/
│   ├── MqProducer, MqSendOptions
│   ├── AbstractMqConsumer, ConsumeStatus, MqConsumeContext
│   ├── outbox/       MqOutbox, MqOutboxRelay, MqOutboxStore, MqOutboxMessage, MqOutboxStatus
│   ├── failure/      MqConsumeFailureHandler, MqConsumeFailureStore, Record/Status/Reason
│   └── idempotent/   MqIdempotentStore
└── exception/        MqProducerException, MqConsumerException
```
