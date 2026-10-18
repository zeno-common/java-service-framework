# jsf-mq-mongodb

jsf-mq 可靠性组件的 **MongoDB 落库实现**。为 [jsf-mq-core](../jsf-mq-core/README.md) 定义的 `MqOutboxStore` / `MqConsumeFailureStore` / `MqIdempotentStore` 接口提供基于 MongoDB 的具体实现。引入后，Outbox 可靠发送、消费幂等、失败重放才会被条件装配激活。整体设计见 [jsf-mq/README.md](../../README.md)。

## 提供的实现

| 实现的接口（core） | MongoDB 实现 | 落库文档 | 用途 |
|--------------------|--------------|----------|------|
| `MqOutboxStore` | `MongoMqOutboxStore` | `MqOutboxDoc` | Outbox 待发消息事务落库 + relay 认领补发 |
| `MqConsumeFailureStore` | `MongoMqConsumeFailureStore` | `MqConsumeFailureDoc` | DISCARD / 重试耗尽记录落库与重放 |
| `MqIdempotentStore` | `MongoMqIdempotentStore` | `MqIdempotentDoc` | 消费去重（幂等键） |

文档类继承自 [jsf-mongodb](../../jsf-mongodb/README.md) 的 `BaseMongoDoc`，复用 Snowflake ID 与审计字段能力。

## 引入依赖

前置条件：通过 [jsf-bom](../../../jsf-bom/README.md) 管理依赖版本。通常与 `jsf-mq-core` 搭配引入：

```xml
<dependency>
    <groupId>io.soil.jsf</groupId>
    <artifactId>jsf-mq-core</artifactId>
</dependency>
<dependency>
    <groupId>io.soil.jsf</groupId>
    <artifactId>jsf-mq-mongodb</artifactId>
</dependency>
```

## 自动配置

| 自动配置类 | 装配内容 |
|------------|----------|
| `MqMongoOutboxAutoConfig` | 注册 `MqOutboxStore` 实现，触发 core 侧 `MqOutbox` + `MqOutboxRelay` 装配 |
| `MqMongoConsumeFailureAutoConfig` | 注册 `MqConsumeFailureStore` 实现，触发 core 侧 `MqConsumeFailureHandler` 装配 |

> relay 兜底轮询需应用启用 `@EnableScheduling`。

## 前置条件

- 已引入并使用 `jsf-mq-core`
- 配置 MongoDB 连接（`spring.data.mongodb.uri`）
- Outbox 依赖事务同步（`spring-tx`），"事务内落库 + 提交后投递" 需业务在 `@Transactional` 上下文中调用 `MqOutbox.save(...)`

## 项目结构

```
io.soil.jsf.mq.mongodb/
├── config/   MqMongoOutboxAutoConfig, MqMongoConsumeAutoConfig
├── doc/      MqOutboxDoc, MqConsumeFailureDoc, MqIdempotentDoc
└── store/    MongoMqOutboxStore, MongoMqConsumeFailureStore, MongoMqIdempotentStore
```
