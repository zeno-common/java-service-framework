# jsf-common-dependencies

JSF 组件模块依赖管理，统一管理 `jsf-common` 下组件的版本。

## 功能说明

管理 JSF 框架可插拔组件（MongoDB 持久化、消息队列）的依赖版本，供 [jsf-dependencies](../jsf-dependencies/README.md) 聚合导入。

## 管理的依赖

| 依赖 | 说明 |
|------|------|
| jsf-mongodb | MongoDB 文档基类与自动配置（Snowflake ID、审计、事务） |
| jsf-mq-core | 消息队列核心：生产者 API、消费者基类、Outbox / 幂等 / 失败重放抽象 |
| jsf-mq-mongodb | 消息队列可靠性组件的 MongoDB 落库实现 |

## 使用方式

通常由 [jsf-dependencies](../jsf-dependencies/README.md) 聚合导入，无需单独引用。若需单独导入：

```xml
<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>io.soil.jsf</groupId>
            <artifactId>jsf-common-dependencies</artifactId>
            <version>jsf.0.1.2</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>
```

导入后声明组件依赖时省略 `<version>`：

```xml
<dependency>
    <groupId>io.soil.jsf</groupId>
    <artifactId>jsf-mongodb</artifactId>
</dependency>
```
