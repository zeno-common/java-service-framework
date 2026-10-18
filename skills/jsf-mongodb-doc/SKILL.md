---
name: "jsf-mongodb-doc"
description: "Use when coding with jsf-mongodb SDK, need API reference for MongoDB document base class, Snowflake ID auto-generation, auditing and transaction support."
---

# jsf-mongodb SDK 参考

基于 Spring Data MongoDB 的封装模块，提供文档基类与自动配置：Snowflake ID 自动生成、审计字段填充、多文档事务。引入即生效。

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
        <artifactId>jsf-mongodb</artifactId>
    </dependency>
</dependencies>
```

> 若为启动项目，也可直接继承 `io.soil.jsf:jsf-parent:jsf.0.1.2` 作为 `<parent>`，自动获得全部依赖版本管理与构建配置。

## 依赖引入

java maven 工程的依赖引入参考：'[jsf-bom/README.md](../../jsf-bom/README.md)'

| ArtifactId | 说明 |
|-----------|------|
| `jsf-mongodb` | MongoDB 文档基类与自动配置（Snowflake ID、审计、事务） |

## 类索引

| 类 | 包 | 说明 | 文档 |
|----|-----|------|------|
| `MongoAutoConfig` | `io.soil.jsf.mongodb.config` | 自动配置：事务管理器 + ID 回调 + 审计 | [MongoAutoConfig](references/io.soil.jsf.mongodb.config/MongoAutoConfig.md) |
| `BaseMongoDoc` | `io.soil.jsf.mongodb.doc` | MongoDB 文档抽象基类（id + 审计字段） | [BaseMongoDoc](references/io.soil.jsf.mongodb.doc/BaseMongoDoc.md) |
| `MongoDocIdCallback` | `io.soil.jsf.mongodb.doc` | Snowflake ID 自动生成回调 | [MongoDocIdCallback](references/io.soil.jsf.mongodb.doc/MongoDocIdCallback.md) |
