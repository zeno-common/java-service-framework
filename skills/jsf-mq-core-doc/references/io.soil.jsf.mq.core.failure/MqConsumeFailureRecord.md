# MqConsumeFailureRecord

> 消费失败记录核心 POJO，与具体存储无关。

- **包**: `io.soil.jsf.mq.core.failure`
- **Lombok**: `@Getter` `@Setter`

消费者判定 DISCARD 或 broker 重试耗尽（即将进死信）时由 [MqConsumeFailureHandler](#MqConsumeFailureHandler) 持久化，保留完整消息上下文供人工排查与重放。

## 字段

| 字段 | 类型 | 说明 |
|------|------|------|
| `id` | Long | 记录 ID（存储实现回填） |
| `topic` | String | 消息 topic |
| `tag` | String | 消息 tag |
| `consumerGroup` | String | 消费者组 |
| `consumerClass` | String | 消费者类全名 |
| `messageId` | String | 消息 ID（如有） |
| `keys` | String | 消息 keys（业务键/幂等键） |
| `bizKey` | String | 业务键/幂等键（如有） |
| `reason` | MqConsumeFailureReason | 失败原因（DISCARDED / RETRY_EXHAUSTED） |
| `payload` | String | 消息体 JSON |
| `errorMsg` | String | 异常摘要 |
| `stackTrace` | String | 异常堆栈 |
| `status` | MqConsumeFailureStatus | 记录状态（默认 PENDING） |
| `failedAt` | OffsetDateTime | 失败发生时间 |
| `replayedAt` | OffsetDateTime | 重放时间 |

> 所有字段均有 getter/setter [Lombok 生成]。

- **相关**: [MqConsumeFailureReason](#MqConsumeFailureReason)、[MqConsumeFailureStatus](#MqConsumeFailureStatus)
