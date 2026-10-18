# MqProducerProperties

> jsf-mq 生产者侧配置属性，前缀 `jsf.mq.producer`。

- **包**: `io.soil.jsf.mq.config.properties`
- **注解**: `@Data` `@ConfigurationProperties("jsf.mq.producer")`

NameServer 默认读取 `rocketmq.name-server`，未配置回退 `127.0.0.1:9876`。

## 字段

| 字段 | 类型 | 默认 | 说明 |
|------|------|------|------|
| `enabled` | boolean | true | 是否启用生产者（本地开发可关闭） |
| `group` | String | `jsf-mq-producer` | 默认生产者组 |
| `outbox` | Outbox | — | Outbox 可靠发送配置（前缀 `jsf.mq.producer.outbox`） |

> 所有字段均有 getter/setter [Lombok 生成]。

## 嵌套类型 Outbox（`@Data`）

| 字段 | 类型 | 默认 | 说明 |
|------|------|------|------|
| `immediateSend` | boolean | true | 事务提交后立即投递（false 则纯 relay 轮询） |
| `maxAttempts` | int | 16 | 最大发送尝试次数，耗尽后置 FAILED |
| `lockSeconds` | long | 60 | 认领锁时长（秒），超时视为僵尸行 |
| `initialBackoffSeconds` | long | 10 | 首次退避（秒），兼作 insert 时 nextRetryAt 缓冲 |
| `maxBackoffSeconds` | long | 3600 | 最大退避（秒） |
| `relay` | Relay | — | relay 兜底补发配置（前缀 `jsf.mq.producer.outbox.relay`） |

## 嵌套类型 Relay（`@Data`，前缀 `jsf.mq.producer.outbox.relay`）

| 字段 | 类型 | 默认 | 说明 |
|------|------|------|------|
| `enabled` | boolean | true | 是否启用 relay 定时兜底（需 `@EnableScheduling`） |
| `interval` | long | 5000 | 扫描间隔（毫秒） |
| `batchSize` | int | 100 | 单轮扫描批量条数 |

- **相关**: [MqProducerAutoConfig](#MqProducerAutoConfig)、[MqOutbox](#MqOutbox)
