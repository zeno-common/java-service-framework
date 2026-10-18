# MongoMqOutboxStore

> `MqOutboxStore` 的 MongoDB 实现。

- **包**: `io.soil.jsf.mq.mongodb.store`
- **实现**: `io.soil.jsf.mq.core.outbox.MqOutboxStore`
- **构造**: `MongoMqOutboxStore(MongoTemplate mongoTemplate)`

`claim` 使用 findAndModify 原子完成 `PENDING → SENDING`（或接管锁过期僵尸行），集群多实例下单行仅被一个实例认领。`insert` 与业务写库的原子性依赖调用方处于同一 Mongo 事务（`MongoTransactionManager` + 副本集）。

## 方法

均实现自 [MqOutboxStore](#MqOutboxStore) 接口协议：

| 方法 | 说明 |
|------|------|
| `void insert(MqOutboxMessage msg)` | insert `MqOutboxDoc` 并回填 `msg.id` |
| `List<MqOutboxMessage> fetchPending(int limit, long nowMillis)` | 查 `PENDING 且 nextRetryAt<=now` 或 `SENDING 且 lockExpireAt<now`，按 nextRetryAt 升序 |
| `boolean claim(Long id, long lockExpireAt)` | findAndModify 原子置 SENDING，返回是否认领成功 |
| `void markSent(Long id)` | updateFirst 置 SENT |
| `void markFailed(Long id, int attempt, long nextRetryAt)` | updateFirst 置回 PENDING + attempt + nextRetryAt |
| `void markDead(Long id, int attempt)` | updateFirst 置 FAILED + attempt |

- **相关**: [MqOutboxDoc](#MqOutboxDoc)、`io.soil.jsf.mq.core.outbox.MqOutboxStore`
