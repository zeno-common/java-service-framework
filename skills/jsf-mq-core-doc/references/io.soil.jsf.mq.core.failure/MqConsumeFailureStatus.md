# MqConsumeFailureStatus

> 消费失败记录状态枚举。

- **包**: `io.soil.jsf.mq.core.failure`
- **类型**: `enum`

## 枚举值

| 名称 | 说明 |
|------|------|
| `PENDING` | 待处理（刚落库，等待人工/定时重放） |
| `REPLAYED` | 已重放 |

- **相关**: [MqConsumeFailureRecord](#MqConsumeFailureRecord)、[MqConsumeFailureHandler](#MqConsumeFailureHandler)
