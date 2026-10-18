# MqProducer

> jsf-mq 统一消息生产者，封装 `RocketMQTemplate` 常用发送能力。

- **包**: `io.soil.jsf.mq.core`
- **构造**: `MqProducer(RocketMQTemplate rocketMQTemplate)`

提供同步、异步（带回调）、单向、延迟四类 API。所有发送异常统一包装为 [MqProducerException](#MqProducerException) 并保留异常链。destination 支持 `topic` 与 `topic:tag`。元信息经 [MqSendOptions](#MqSendOptions) 写入 RocketMQ keys/用户属性，不污染业务 payload。

## 方法

### send

`SendResult send(String destination, Object payload)` — 同步发送（无元信息）
`SendResult send(String destination, Object payload, MqSendOptions options)` — 同步发送（携带元信息）

### sendAsync

`void sendAsync(String destination, Object payload, Consumer<SendResult> onSuccess)` — 仅成功回调（异常抛出）
`void sendAsync(String destination, Object payload, Consumer<SendResult> onSuccess, Consumer<Throwable> onError)` — 成功/失败分离
`void sendAsync(String destination, Object payload, MqSendOptions options, Consumer<SendResult> onSuccess)` — 携带元信息，仅成功回调
`void sendAsync(String destination, Object payload, MqSendOptions options, Consumer<SendResult> onSuccess, Consumer<Throwable> onError)` — 携带元信息，成功/失败分离

### sendOneway

`void sendOneway(String destination, Object payload)` — 单向发送（不保证可靠）
`void sendOneway(String destination, Object payload, MqSendOptions options)` — 携带元信息

### sendDelay

`SendResult sendDelay(String destination, Object payload, int delayLevel)` — 同步延迟消息
`SendResult sendDelay(String destination, Object payload, MqSendOptions options, int delayLevel)` — 携带元信息

> 延迟级别 `delayLevel` 取值 1~18（RocketMQ 延迟等级）；默认发送超时 3000ms。

**参数**:
- `destination` (String) — topic 或 topic:tag
- `payload` (Object) — 消息体（对象自动序列化）
- `options` (MqSendOptions) — 元信息（keys / 自定义属性）
- `onSuccess` / `onError` — 异步回调

**返回**: `SendResult`（send/sendDelay）或 void

**异常**: `MqProducerException` — 发送失败

**示例**:
```java
mqProducer.send("order-topic:created", orderDto);
mqProducer.sendDelay("delay-topic", dto, 3); // 延迟级别 3
```

- **相关**: [MqSendOptions](#MqSendOptions)、[MqProducerException](#MqProducerException)
