# MqIdempotentStore

> 消费幂等存储接口（依赖倒置：核心只定义协议，落库方式由实现模块提供，如 jsf-mq-mongodb）。

- **包**: `io.soil.jsf.mq.core.idempotent`
- **类型**: `interface`

三段式协议 claim / markProcessed / release：消费前 tryClaim 去重，成功 markProcessed（终态），失败 release（允许重试再进入）。声明自带 ttl，避免消费者崩溃后永久占用导致消息丢失。

## 方法

### tryClaim

`boolean tryClaim(String key, long ttlSeconds)`

> 原子声明消息处理权。

**参数**:
- `key` (String) — 幂等键（如 messageId / bizKey）
- `ttlSeconds` (long) — 声明有效期（秒），超时未标记完成视为失效可被重新声明

**返回**: boolean — true=首次声明成功可处理；false=已被声明或已完成，应跳过

### markProcessed

`void markProcessed(String key)`

> 处理成功后标记为已完成（终态，后续重复投递直接跳过）。

### release

`void release(String key)`

> 处理失败时释放声明，允许后续重试再次进入处理。

- **相关**: [AbstractMqConsumer](#AbstractMqConsumer)
