# MqIdempotentDoc

> 消费幂等记录文档，collection `mq_idempotent`。

- **包**: `io.soil.jsf.mq.mongodb.doc`
- **父类**: `io.soil.jsf.mongodb.doc.BaseMongoDoc<Long>`
- **注解**: `@Getter` `@Setter` `@Document("mq_idempotent")`

`key` 唯一索引保证 tryClaim 原子性（重复 insert 触发 DuplicateKeyException）；`expireAt` TTL 索引自动清理历史（需开启 `spring.data.mongodb.auto-index-creation` 或手工建索引）。

## 常量

| 常量 | 值 | 说明 |
|------|----|------|
| `STATUS_CLAIMED` | `CLAIMED` | 声明中 |
| `STATUS_PROCESSED` | `PROCESSED` | 已处理完成（终态） |

## 字段

| 字段 | 类型 | 映射 | 说明 |
|------|------|------|------|
| `key` | String | key（唯一索引） | 幂等键 |
| `status` | String | status | 状态：CLAIMED / PROCESSED |
| `claimExpireAt` | long | claimExpireAt | 声明过期时间（epoch millis），CLAIMED 超时视为失效可被接管 |
| `expireAt` | Date | expireAt（TTL `expireAfter=0s`） | 文档 TTL 清理时间 |

> 所有字段均有 getter/setter [Lombok 生成]。

- **相关**: [MongoMqIdempotentStore](#MongoMqIdempotentStore)
