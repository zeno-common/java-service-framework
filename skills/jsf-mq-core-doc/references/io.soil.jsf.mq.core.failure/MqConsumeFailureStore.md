# MqConsumeFailureStore

> 消费失败记录存储接口（依赖倒置：核心只定义协议，落库方式由实现模块提供，如 jsf-mq-mongodb）。

- **包**: `io.soil.jsf.mq.core.failure`
- **类型**: `interface`

## 方法

### save

`void save(MqConsumeFailureRecord record)`

> 持久化失败记录，实现方须回填 `record.setId(...)`。

### fetchPending

`List<MqConsumeFailureRecord> fetchPending(int limit)`

> 拉取待重放（PENDING）的失败记录，按失败时间升序。

**参数**: `limit` (int) — 最大条数

**返回**: List&lt;MqConsumeFailureRecord&gt;

### markReplayed

`void markReplayed(Long id)`

> 标记记录为已重放（REPLAYED）。

**参数**: `id` (Long) — 记录 ID

- **相关**: [MqConsumeFailureHandler](#MqConsumeFailureHandler)、[MqConsumeFailureRecord](#MqConsumeFailureRecord)
