---
name: "jsf-mq-mongodb-doc"
description: "Use when coding with jsf-mq-mongodb SDK, need API reference for MongoDB-backed Outbox store, consume idempotent store and consume failure store implementations."
---

# jsf-mq-mongodb SDK 参考

jsf-mq-core 存储接口的 MongoDB 实现模块：Outbox 可靠发送存储、消费幂等存储、消费失败记录存储，及对应文档模型与自动配置。引入即通过 `@AutoConfigureBefore` 让 Store Bean 先于 mq-core 装配，触发核心条件装配。

## BOM 引入示例

推荐先导入 `jsf-dependencies` 聚合 BOM 统一管理版本，再声明本模块依赖（**省略 `<version>`**）：

```xml
<!-- 1. 导入 BOM -->
<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>io.soil.jsf</groupId>
            <artifactId>jsf-dependencies</artifactId>
            <version>jsf.0.2.1</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>

<!-- 2. 声明依赖（版本由 BOM 管理） -->
<dependencies>
    <dependency>
        <groupId>io.soil.jsf</groupId>
        <artifactId>jsf-mq-mongodb</artifactId>
    </dependency>
</dependencies>
```

> 若为启动项目，也可直接继承 `io.soil.jsf:jsf-parent:jsf.0.2.1` 作为 `<parent>`，自动获得全部依赖版本管理与构建配置。

## 依赖引入

java maven 工程的依赖引入参考：'[jsf-bom/README.md](../../jsf-bom/README.md)'

| ArtifactId | 说明 |
|-----------|------|
| `jsf-mq-mongodb` | mq-core 三类存储接口的 MongoDB 实现 |

## 类索引

| 类 | 包 | 说明 | 文档 |
|----|-----|------|------|
| `MqMongoOutboxAutoConfig` | `io.soil.jsf.mq.mongodb.config` | Outbox 存储自动配置 | [MqMongoOutboxAutoConfig](references/io.soil.jsf.mq.mongodb.config/MqMongoOutboxAutoConfig.md) |
| `MqMongoConsumeAutoConfig` | `io.soil.jsf.mq.mongodb.config` | 幂等/失败存储自动配置 | [MqMongoConsumeAutoConfig](references/io.soil.jsf.mq.mongodb.config/MqMongoConsumeAutoConfig.md) |
| `MqOutboxDoc` | `io.soil.jsf.mq.mongodb.doc` | Outbox 消息文档（mq_outbox） | [MqOutboxDoc](references/io.soil.jsf.mq.mongodb.doc/MqOutboxDoc.md) |
| `MqIdempotentDoc` | `io.soil.jsf.mq.mongodb.doc` | 消费幂等记录文档（mq_idempotent） | [MqIdempotentDoc](references/io.soil.jsf.mq.mongodb.doc/MqIdempotentDoc.md) |
| `MqConsumeFailureDoc` | `io.soil.jsf.mq.mongodb.doc` | 消费失败记录文档（mq_consume_failure） | [MqConsumeFailureDoc](references/io.soil.jsf.mq.mongodb.doc/MqConsumeFailureDoc.md) |
| `MongoMqOutboxStore` | `io.soil.jsf.mq.mongodb.store` | MqOutboxStore 的 MongoDB 实现 | [MongoMqOutboxStore](references/io.soil.jsf.mq.mongodb.store/MongoMqOutboxStore.md) |
| `MongoMqIdempotentStore` | `io.soil.jsf.mq.mongodb.store` | MqIdempotentStore 的 MongoDB 实现 | [MongoMqIdempotentStore](references/io.soil.jsf.mq.mongodb.store/MongoMqIdempotentStore.md) |
| `MongoMqConsumeFailureStore` | `io.soil.jsf.mq.mongodb.store` | MqConsumeFailureStore 的 MongoDB 实现 | [MongoMqConsumeFailureStore](references/io.soil.jsf.mq.mongodb.store/MongoMqConsumeFailureStore.md) |

> 接口协议详见 jsf-mq-core-doc：`MqOutboxStore`、`MqIdempotentStore`、`MqConsumeFailureStore`。
