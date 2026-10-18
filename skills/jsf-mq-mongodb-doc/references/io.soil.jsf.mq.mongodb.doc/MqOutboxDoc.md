# MqOutboxDoc

> Outbox 消息文档，collection `mq_outbox`。

- **包**: `io.soil.jsf.mq.mongodb.doc`
- **父类**: `io.soil.jsf.mongodb.doc.BaseMongoDoc<Long>`（含 id/审计字段）
- **注解**: `@Getter` `@Setter` `@Document("mq_outbox")` `@CompoundIndex(name="idx_status_next_retry", def="{'status':1,'nextRetryAt':1}")`

复合索引 `(status, nextRetryAt)` 支撑 relay 的 fetchPending 扫描；状态字段按枚举 `name()` 存字符串。

## 字段

| 字段 | 类型 | 映射 | 说明 |
|------|------|------|------|
| `topic` | String | topic | 目标 topic |
| `tag` | String | tag | 消息 tag |
| `payload` | String | payload | 业务消息体 JSON |
| `status` | MqOutboxStatus | status | 状态（枚举存字符串） |
| `attempt` | int | attempt | 已尝试发送次数 |
| `nextRetryAt` | long | nextRetryAt | 下次可重试时间（epoch millis） |
| `lockExpireAt` | long | lockExpireAt | 认领锁过期时间（epoch millis） |

> 所有字段均有 getter/setter [Lombok 生成]。

- **相关**: [MongoMqOutboxStore](#MongoMqOutboxStore)、`io.soil.jsf.mq.core.outbox.MqOutboxStatus`
