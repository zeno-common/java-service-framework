---
name: "jsf-mq-core-doc"
description: "Use when coding with jsf-mq-core SDK, need API reference for RocketMQ producer/consumer, Outbox reliable send, consume idempotency and failure replay."
---

# jsf-mq-core SDK 参考

基于 RocketMQ Spring 的消息可靠性核心模块：统一生产者、Outbox 事务性可靠发送、消费者三态处理基类、消费幂等接口、消费失败落库与重放。存储实现通过接口倒置（由 jsf-mq-mongodb 等模块提供）。

## BOM 引入示例

推荐先导入 `jsf-dependencies` 聚合 BOM 统一管理版本，再声明本模块依赖（**省略 `<version>`**）：

```xml
<!-- 1. 导入 BOM -->
<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>io.soil.jsf</groupId>
            <artifactId>jsf-dependencies</artifactId>
            <version>jsf.0.1.2</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>

<!-- 2. 声明依赖（版本由 BOM 管理） -->
<dependencies>
    <dependency>
        <groupId>io.soil.jsf</groupId>
        <artifactId>jsf-mq-core</artifactId>
    </dependency>
</dependencies>
```

> 若为启动项目，也可直接继承 `io.soil.jsf:jsf-parent:jsf.0.1.2` 作为 `<parent>`，自动获得全部依赖版本管理与构建配置。

## 依赖引入

java maven 工程的依赖引入参考：'[jsf-bom/README.md](../../jsf-bom/README.md)'

| ArtifactId | 说明 |
|-----------|------|
| `jsf-mq-core` | RocketMQ 生产/消费核心、Outbox、幂等与失败重放（接口 + 编排） |

## 类索引

| 类 | 包 | 说明 | 文档 |
|----|-----|------|------|
| `MqProducerAutoConfig` | `io.soil.jsf.mq.config` | 生产者 + Outbox 编排自动配置 | [MqProducerAutoConfig](references/io.soil.jsf.mq.config/MqProducerAutoConfig.md) |
| `MqConsumerAutoConfig` | `io.soil.jsf.mq.config` | 消费失败处理器自动配置 | [MqConsumerAutoConfig](references/io.soil.jsf.mq.config/MqConsumerAutoConfig.md) |
| `MqProducerProperties` | `io.soil.jsf.mq.config.properties` | 生产者/Outbox 配置属性 | [MqProducerProperties](references/io.soil.jsf.mq.config.properties/MqProducerProperties.md) |
| `MqConsumerProperties` | `io.soil.jsf.mq.config.properties` | 消费者配置属性 | [MqConsumerProperties](references/io.soil.jsf.mq.config.properties/MqConsumerProperties.md) |
| `MqProducer` | `io.soil.jsf.mq.core` | 统一消息生产者 | [MqProducer](references/io.soil.jsf.mq.core/MqProducer.md) |
| `MqSendOptions` | `io.soil.jsf.mq.core` | 发送元信息选项（keys/属性） | [MqSendOptions](references/io.soil.jsf.mq.core/MqSendOptions.md) |
| `AbstractMqConsumer` | `io.soil.jsf.mq.core` | 消费者抽象基类（三态 + 幂等） | [AbstractMqConsumer](references/io.soil.jsf.mq.core/AbstractMqConsumer.md) |
| `ConsumeStatus` | `io.soil.jsf.mq.core` | 消费结果三态枚举 | [ConsumeStatus](references/io.soil.jsf.mq.core/ConsumeStatus.md) |
| `MqConsumeContext` | `io.soil.jsf.mq.core` | 消费元信息上下文 | [MqConsumeContext](references/io.soil.jsf.mq.core/MqConsumeContext.md) |
| `MqOutbox` | `io.soil.jsf.mq.core.outbox` | Outbox 编排入口（落库+立即投递） | [MqOutbox](references/io.soil.jsf.mq.core.outbox/MqOutbox.md) |
| `MqOutboxMessage` | `io.soil.jsf.mq.core.outbox` | Outbox 消息 POJO | [MqOutboxMessage](references/io.soil.jsf.mq.core.outbox/MqOutboxMessage.md) |
| `MqOutboxRelay` | `io.soil.jsf.mq.core.outbox` | Outbox 定时兜底补发器 | [MqOutboxRelay](references/io.soil.jsf.mq.core.outbox/MqOutboxRelay.md) |
| `MqOutboxStatus` | `io.soil.jsf.mq.core.outbox` | Outbox 状态枚举 | [MqOutboxStatus](references/io.soil.jsf.mq.core.outbox/MqOutboxStatus.md) |
| `MqOutboxStore` | `io.soil.jsf.mq.core.outbox` | Outbox 存储接口 | [MqOutboxStore](references/io.soil.jsf.mq.core.outbox/MqOutboxStore.md) |
| `MqConsumeFailureHandler` | `io.soil.jsf.mq.core.failure` | 消费失败落库/重放处理器 | [MqConsumeFailureHandler](references/io.soil.jsf.mq.core.failure/MqConsumeFailureHandler.md) |
| `MqConsumeFailureRecord` | `io.soil.jsf.mq.core.failure` | 消费失败记录 POJO | [MqConsumeFailureRecord](references/io.soil.jsf.mq.core.failure/MqConsumeFailureRecord.md) |
| `MqConsumeFailureReason` | `io.soil.jsf.mq.core.failure` | 失败原因枚举 | [MqConsumeFailureReason](references/io.soil.jsf.mq.core.failure/MqConsumeFailureReason.md) |
| `MqConsumeFailureStatus` | `io.soil.jsf.mq.core.failure` | 失败记录状态枚举 | [MqConsumeFailureStatus](references/io.soil.jsf.mq.core.failure/MqConsumeFailureStatus.md) |
| `MqConsumeFailureStore` | `io.soil.jsf.mq.core.failure` | 失败记录存储接口 | [MqConsumeFailureStore](references/io.soil.jsf.mq.core.failure/MqConsumeFailureStore.md) |
| `MqIdempotentStore` | `io.soil.jsf.mq.core.idempotent` | 消费幂等存储接口（三段式） | [MqIdempotentStore](references/io.soil.jsf.mq.core.idempotent/MqIdempotentStore.md) |
| `MqProducerException` | `io.soil.jsf.mq.exception` | 生产者侧统一异常 | [MqProducerException](references/io.soil.jsf.mq.exception/MqProducerException.md) |
| `MqConsumerException` | `io.soil.jsf.mq.exception` | 消费者侧统一异常 | [MqConsumerException](references/io.soil.jsf.mq.exception/MqConsumerException.md) |
