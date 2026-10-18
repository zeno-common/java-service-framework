# jsf-common

JSF 组件化功能模块聚合，封装面向具体基础设施的可插拔能力（MongoDB 持久化、消息队列）。各组件通过 Spring Boot 自动配置装配，引入依赖即生效。

## 模块结构

| 模块 | 说明 |
|------|------|
| [jsf-mongodb](./jsf-mongodb/README.md) | MongoDB 文档基类与自动配置：Snowflake ID 自动生成、审计字段填充、多文档事务 |
| [jsf-mq](./jsf-mq/README.md) | 基于 RocketMQ 5.5 封装的消息中间件：统一生产者、消费者基类、Outbox 可靠发送、消费幂等与失败重放 |

## 依赖版本管理

前置条件：通过 [jsf-bom](../jsf-bom/README.md) 管理依赖版本（继承 `jsf-parent` 或导入 `jsf-dependencies` BOM）。JSF 组件的版本统一由 [jsf-common-dependencies](../jsf-bom/jsf-common-dependencies/README.md) 声明。

```xml
<dependency>
    <groupId>io.soil.jsf</groupId>
    <artifactId>jsf-mongodb</artifactId>
    <!-- 无 <version>，由 BOM 管理 -->
</dependency>
```

## 自动配置约定

各组件均通过 `META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports` 注册自动配置类，并按 Bean 存在性条件装配（`@ConditionalOnMissingBean` / `@ConditionalOnClass`），业务方可自定义实现覆盖。具体装配规则见各组件 README。
