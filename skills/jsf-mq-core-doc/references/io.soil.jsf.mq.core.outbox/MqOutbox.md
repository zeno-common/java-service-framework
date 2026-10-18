# MqOutbox

> Outbox 编排入口：事务内落库 + 事务提交后立即投递（immediate dispatch），relay 兜底。

- **包**: `io.soil.jsf.mq.core.outbox`
- **注解**: `@Slf4j`
- **构造**: `MqOutbox(MqOutboxStore store, MqProducer producer, ObjectMapper objectMapper, MqProducerProperties.Outbox props)`

时序：save 在业务事务内 insert（PENDING）→ afterCommit 执行 dispatch（claim→发送→markSent）→ 立即投递失败仅记 warn，由 [MqOutboxRelay](#MqOutboxRelay) 兜底。语义为 at-least-once，消费端必须配合幂等去重。

## 方法

### save

`MqOutboxMessage save(String topic, String tag, Object payload)`

> 落库 Outbox 消息（应在业务事务内调用），事务提交后自动立即投递（`immediateSend=true` 时）。

**参数**:
- `topic` (String) — 目标 topic
- `tag` (String) — 消息 tag（可空）
- `payload` (Object) — 业务消息体（序列化为 JSON 存储）

**返回**: MqOutboxMessage — 已落库消息（id 已回填）

### dispatch

`boolean dispatch(MqOutboxMessage msg)`

> 投递单条消息：原子认领 → 发送 → 标记结果。供立即投递与 relay 兜底共用。

**参数**: `msg` (MqOutboxMessage) — Outbox 消息

**返回**: boolean — true=发送成功；false=认领失败或发送失败（已按退避/终态落库）

**示例**:
```java
// 业务事务内
@Transactional
public void createOrder(OrderDto dto) {
    orderRepo.save(dto);
    mqOutbox.save("order-topic", "created", dto); // 提交后自动投递
}
```

- **相关**: [MqOutboxStore](#MqOutboxStore)、[MqOutboxMessage](#MqOutboxMessage)、[MqOutboxRelay](#MqOutboxRelay)
