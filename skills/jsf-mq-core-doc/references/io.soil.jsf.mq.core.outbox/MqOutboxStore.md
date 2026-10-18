# MqOutboxStore

> Outbox 存储接口（依赖倒置：核心只定义协议，落库方式由实现模块提供，如 jsf-mq-mongodb）。

- **包**: `io.soil.jsf.mq.core.outbox`
- **类型**: `interface`

实现方须保证 [claim](#claim) 的**原子性**（如 MongoDB findAndModify、SQL `UPDATE ... WHERE status='PENDING'` 判断影响行数），支撑集群多实例并发安全。

## 方法

### insert

`void insert(MqOutboxMessage msg)`

> 插入 Outbox 消息（应在业务事务内调用），实现方须回填 `msg.setId(...)`。

### fetchPending

`List<MqOutboxMessage> fetchPending(int limit, long nowMillis)`

> 拉取可投递消息：`PENDING 且 nextRetryAt<=now`，或 `SENDING 且 lockExpireAt<now`（僵尸行）。按 nextRetryAt 升序。

**参数**:
- `limit` (int) — 最大条数
- `nowMillis` (long) — 当前时间（epoch millis）

**返回**: List&lt;MqOutboxMessage&gt;

### claim

`boolean claim(Long id, long lockExpireAt)`

> 原子认领：`PENDING → SENDING`（或接管锁过期僵尸行）并写入 lockExpireAt。

**返回**: boolean — true=认领成功可发送

### markSent

`void markSent(Long id)`

> 标记发送成功（终态 SENT）。

### markFailed

`void markFailed(Long id, int attempt, long nextRetryAt)`

> 标记单次发送失败：回到 PENDING 并设置退避后的下次重试时间。

### markDead

`void markDead(Long id, int attempt)`

> 标记重试耗尽（终态 FAILED，需人工介入）。

- **相关**: [MqOutbox](#MqOutbox)、[MqOutboxMessage](#MqOutboxMessage)
