# MqOutboxStatus

> Outbox 消息状态枚举。

- **包**: `io.soil.jsf.mq.core.outbox`
- **类型**: `enum`

状态机：`PENDING → SENDING → SENT`；发送失败且未耗尽重试回到 PENDING（带退避），重试耗尽进入终态 FAILED。

## 枚举值

| 名称 | 说明 |
|------|------|
| `PENDING` | 待发送（含发送失败后等待退避重试） |
| `SENDING` | 已认领、发送中（带 lockExpireAt，锁过期视为僵尸行可重新认领） |
| `SENT` | 发送成功（终态） |
| `FAILED` | 重试耗尽，终态失败（需人工介入） |

- **相关**: [MqOutboxMessage](#MqOutboxMessage)、[MqOutboxStore](#MqOutboxStore)
