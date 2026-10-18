# MqOutboxMessage

> Outbox 消息核心 POJO，与具体存储无关。

- **包**: `io.soil.jsf.mq.core.outbox`
- **Lombok**: `@Getter` `@Setter`

## 字段

| 字段 | 类型 | 说明 |
|------|------|------|
| `id` | Long | 消息 ID（由存储实现生成并回填） |
| `topic` | String | 目标 topic |
| `tag` | String | 消息 tag（可空） |
| `payload` | String | 业务消息体 JSON |
| `status` | MqOutboxStatus | 状态（默认 PENDING） |
| `attempt` | int | 已尝试发送次数 |
| `nextRetryAt` | long | 下次可重试时间（epoch millis），PENDING 行仅 nextRetryAt<=now 时被 relay 捞取 |
| `lockExpireAt` | long | 认领锁过期时间（epoch millis），SENDING 锁过期视为僵尸行 |

> 所有字段均有 getter/setter [Lombok 生成]。

## 方法

### destination

`String destination()`

> 发送目的地：tag 为空返回 `topic`，否则 `topic:tag`。

**返回**: String

- **相关**: [MqOutboxStatus](#MqOutboxStatus)、[MqOutbox](#MqOutbox)
