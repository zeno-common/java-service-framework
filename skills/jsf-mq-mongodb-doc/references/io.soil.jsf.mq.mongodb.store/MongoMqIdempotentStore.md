# MongoMqIdempotentStore

> `MqIdempotentStore` 的 MongoDB 实现。

- **包**: `io.soil.jsf.mq.mongodb.store`
- **实现**: `io.soil.jsf.mq.core.idempotent.MqIdempotentStore`
- **构造**: `MongoMqIdempotentStore(MongoTemplate mongoTemplate)`（保留期默认 7 天）
- **构造**: `MongoMqIdempotentStore(MongoTemplate mongoTemplate, long retentionMillis)`（自定义保留期）

原子性：`tryClaim` 依赖 `key` 唯一索引，insert 成功即声明；DuplicateKey 时 findAndModify 原子接管"已过期 CLAIMED"记录（消费者崩溃场景）。`release` 仅删 CLAIMED，不误删 PROCESSED 终态。历史由 `expireAt` TTL 清理。

## 方法

| 方法 | 说明 |
|------|------|
| `boolean tryClaim(String key, long ttlSeconds)` | insert CLAIMED 记录；冲突则原子接管过期声明 |
| `void markProcessed(String key)` | updateFirst 置 status=PROCESSED（终态） |
| `void release(String key)` | 删除 status=CLAIMED 的记录 |

- **相关**: [MqIdempotentDoc](#MqIdempotentDoc)、`io.soil.jsf.mq.core.idempotent.MqIdempotentStore`
