# AbstractMqConsumer<T>

> jsf-mq 抽象消息消费者基类，实现 `RocketMQListener<MessageExt>`，统一处理反序列化、三态派发、幂等与失败落库。

- **包**: `io.soil.jsf.mq.core`
- **修饰**: `abstract`，泛型 `T` 为消息体类型
- **注解**: `@Slf4j`

子类以 `@RocketMQMessageListener` 声明 topic/group，实现 [handleMessage](#handlemessage)。基类自动：反序列化 body→T、提取元信息封装 [MqConsumeContext](#MqConsumeContext)、按返回的 [ConsumeStatus](#ConsumeStatus) 决定 ack/重试/落库。RocketMQ 为 at-least-once，强烈建议覆写 `idempotentKey` 启用幂等。

## 方法

### onMessage

`public void onMessage(MessageExt messageExt)`

> 消费入口（框架回调）。执行幂等 claim → handleMessage → 按状态 ack / 抛重试 / 落库。

**参数**: `messageExt` (MessageExt) — 原始消息

### handleMessage

`protected abstract ConsumeStatus handleMessage(T message, MqConsumeContext ctx) throws Exception`

> 业务处理唯一入口，由子类实现。返回 null 视为 SUCCESS；抛出异常默认按 RETRY_LATER 处理。

**参数**:
- `message` (T) — 反序列化后的消息体
- `ctx` (MqConsumeContext) — 元信息上下文

**返回**: ConsumeStatus — 三态结果

### idempotentKey

`protected String idempotentKey(T message, MqConsumeContext ctx)`

> 幂等键，默认返回 null（不启用）。返回非空且容器存在 [MqIdempotentStore](#MqIdempotentStore) 时自动启用去重。

**返回**: String — 幂等键

### idempotentTtlSeconds

`protected long idempotentTtlSeconds()`

> 幂等声明有效期（秒），默认 300。防消费者崩溃后声明永久占用。

**返回**: long

### setIdempotentStore / setFailureHandler

`public void setIdempotentStore(MqIdempotentStore idempotentStore)`
`public void setFailureHandler(MqConsumeFailureHandler failureHandler)`

> 可选依赖注入（`@Autowired(required=false)`），容器无对应 Bean 时为 null。

**示例**:
```java
@RocketMQMessageListener(topic = "order-topic", consumerGroup = "order-group")
public class OrderConsumer extends AbstractMqConsumer<OrderDto> {
    @Override
    protected ConsumeStatus handleMessage(OrderDto msg, MqConsumeContext ctx) {
        // 业务处理
        return ConsumeStatus.SUCCESS;
    }
    @Override
    protected String idempotentKey(OrderDto msg, MqConsumeContext ctx) {
        return ctx.getKeys(); // 用业务键去重
    }
}
```

- **相关**: [ConsumeStatus](#ConsumeStatus)、[MqConsumeContext](#MqConsumeContext)、[MqIdempotentStore](#MqIdempotentStore)、[MqConsumeFailureHandler](#MqConsumeFailureHandler)
