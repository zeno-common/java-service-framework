# MqConsumerProperties

> jsf-mq 消费者侧配置属性，前缀 `jsf.mq.consumer`。

- **包**: `io.soil.jsf.mq.config.properties`
- **注解**: `@Data` `@ConfigurationProperties("jsf.mq.consumer")`

NameServer 默认读取 `rocketmq.name-server`，未配置回退 `127.0.0.1:9876`。

## 字段

| 字段 | 类型 | 默认 | 说明 |
|------|------|------|------|
| `enabled` | boolean | true | 是否启用消费者（本地开发可关闭） |

> 所有字段均有 getter/setter [Lombok 生成]。

- **相关**: [MqConsumerAutoConfig](#MqConsumerAutoConfig)
