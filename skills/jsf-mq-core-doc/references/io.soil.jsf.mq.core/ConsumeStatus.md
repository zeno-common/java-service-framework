# ConsumeStatus

> 消费结果状态枚举，由 [AbstractMqConsumer#handleMessage](#AbstractMqConsumer) 返回，基类据此决定 ack / broker 重试 / 失败落库。

- **包**: `io.soil.jsf.mq.core`
- **类型**: `enum`

## 枚举值

| 名称 | 说明 |
|------|------|
| `SUCCESS` | 消费成功，正常确认（ack） |
| `RETRY_LATER` | 稍后重试：抛异常触发 broker 重试（指数退避，默认 16 次后进 `%DLQ%+consumerGroup` 死信队列）。适用瞬时故障 |
| `DISCARD` | 丢弃不重试：判定不可重试（格式错误、业务拒绝），交由 MqConsumeFailureHandler 落库供重放 |

- **相关**: [AbstractMqConsumer](#AbstractMqConsumer)
