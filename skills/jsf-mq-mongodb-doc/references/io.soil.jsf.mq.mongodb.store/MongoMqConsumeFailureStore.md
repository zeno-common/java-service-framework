# MongoMqConsumeFailureStore

> `MqConsumeFailureStore` 的 MongoDB 实现，collection `mq_consume_failure`。

- **包**: `io.soil.jsf.mq.mongodb.store`
- **实现**: `io.soil.jsf.mq.core.failure.MqConsumeFailureStore`
- **构造**: `MongoMqConsumeFailureStore(MongoTemplate mongoTemplate)`

## 方法

| 方法 | 说明 |
|------|------|
| `void save(MqConsumeFailureRecord record)` | insert `MqConsumeFailureDoc` 并回填 `record.id` |
| `List<MqConsumeFailureRecord> fetchPending(int limit)` | 查 status=PENDING，按 failedAt 升序，限 limit 条 |
| `void markReplayed(Long id)` | updateFirst 置 status=REPLAYED + replayedAt=now |

- **相关**: [MqConsumeFailureDoc](#MqConsumeFailureDoc)、`io.soil.jsf.mq.core.failure.MqConsumeFailureStore`
