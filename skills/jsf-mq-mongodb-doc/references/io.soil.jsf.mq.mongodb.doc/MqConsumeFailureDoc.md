# MqConsumeFailureDoc

> 消费失败记录文档，collection `mq_consume_failure`。

- **包**: `io.soil.jsf.mq.mongodb.doc`
- **父类**: `io.soil.jsf.mongodb.doc.BaseMongoDoc<Long>`
- **注解**: `@Getter` `@Setter` `@Document("mq_consume_failure")`

状态字段按枚举 `name()` 存字符串，可直接按 `status: "PENDING"` 查询。

## 字段

| 字段 | 类型 | 映射 | 说明 |
|------|------|------|------|
| `topic` | String | topic | 消息 topic |
| `tag` | String | tag | 消息 tag |
| `consumerGroup` | String | consumerGroup | 消费者组 |
| `consumerClass` | String | consumerClass | 消费者类全名 |
| `messageId` | String | messageId | 消息 ID |
| `bizKey` | String | bizKey | 业务键/幂等键 |
| `reason` | MqConsumeFailureReason | reason | 失败原因（枚举存字符串） |
| `payload` | String | payload | 消息体 JSON |
| `errorMsg` | String | errorMsg | 异常摘要 |
| `stackTrace` | String | stackTrace | 异常堆栈 |
| `status` | MqConsumeFailureStatus | status（索引） | 记录状态（枚举存字符串） |
| `failedAt` | OffsetDateTime | failedAt | 失败发生时间 |
| `replayedAt` | OffsetDateTime | replayedAt | 重放时间 |

> 所有字段均有 getter/setter [Lombok 生成]。

- **相关**: [MongoMqConsumeFailureStore](#MongoMqConsumeFailureStore)
