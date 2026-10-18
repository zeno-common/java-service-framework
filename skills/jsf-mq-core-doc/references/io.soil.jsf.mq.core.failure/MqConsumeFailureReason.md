# MqConsumeFailureReason

> 消费失败原因枚举，标识失败记录如何产生，供排查与重放策略区分。

- **包**: `io.soil.jsf.mq.core.failure`
- **类型**: `enum`

## 枚举值

| 名称 | 说明 |
|------|------|
| `DISCARDED` | 业务主动 DISCARD：判定不可重试（格式错误、业务拒绝），无异常或携带业务自定义异常 |
| `RETRY_EXHAUSTED` | broker 重试耗尽（默认 16 次）后仍未成功，即将进入死信队列（%DLQ%）前由框架捕获落库 |

- **相关**: [MqConsumeFailureRecord](#MqConsumeFailureRecord)
