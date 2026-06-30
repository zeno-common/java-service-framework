# jsf-mongodb 模块设计规格

## 目标

在 `jsf-components` 下创建 `jsf-mongodb` 模块，基于 `spring-mongodb-conventions` skill 规范，提供 Spring Data MongoDB 的通用公共类，使业务项目引入后即可获得：Snowflake ID 主键、审计字段自动填充、事务管理。

## 核心类

### BaseMongoDoc<U>（`io.soil.jsf.mongodb.doc.BaseMongoDoc`）

所有 Document 类的抽象基类，封装 ID 生成和审计字段。

泛型参数 `<U>` 定义审计人（createdBy / updatedBy）的字段类型，默认使用 `Long`，子类可按需指定其他类型（如 `String`），以适配不同的 AuditorAware 实现。

| 字段 | 类型 | 注解 | 说明 |
|------|------|------|------|
| `id` | `Long` | `@Id` | Snowflake ID，通过 MongoDocIdCallback 自动生成 |
| `createdAt` | `OffsetDateTime` | `@CreatedDate` + `@Field("createdAt")` | 创建时间（携带时区偏移） |
| `updatedAt` | `OffsetDateTime` | `@LastModifiedDate` + `@Field("updatedAt")` | 更新时间（携带时区偏移） |
| `createdBy` | `U` | `@CreatedBy` + `@Field("createdBy")` | 创建人，类型由泛型参数决定 |
| `updatedBy` | `U` | `@LastModifiedBy` + `@Field("updatedBy")` | 更新人，类型由泛型参数决定 |

ID 生成通过 `MongoDocIdCallback`（Entity Callback）在保存前自动完成，替代旧版 `@PrePersist` 注解机制。

- `version` 和 `deleted` 不放入 BaseMongoDoc，由子类按需添加
- `OffsetDateTime` 替代 `LocalDateTime`，消除分布式部署中的时区歧义

### MongoDocIdCallback（`io.soil.jsf.mongodb.doc.MongoDocIdCallback`）

实现 `BeforeConvertCallback<BaseMongoDoc>`，在 Document 转换为 BSON 之前自动为 ID 为 null 的 BaseMongoDoc 子类生成 Snowflake ID。

### MongoAutoConfig（`io.soil.jsf.mongodb.config.MongoAutoConfig`）

Spring Boot 自动配置类，注册以下 Bean：

1. `@EnableMongoAuditing` — 激活审计注解
2. `MongoTransactionManager` — 启用 `@Transactional`（`@ConditionalOnMissingBean`）
3. `MongoDocIdCallback` — Snowflake ID 自动生成回调

条件：`@ConditionalOnClass(MongoTemplate.class)`

审计人（createdBy / updatedBy）的自动填充需要外部模块提供 `AuditorAware<U>` Bean。`BaseMongoDoc<U>` 的泛型参数 `<U>` 决定审计人字段类型，`AuditorAware<U>` 的泛型必须与之匹配。

## 依赖

```xml
<dependency>
  <groupId>io.soil.jsf</groupId>
  <artifactId>jsf-leaf-id</artifactId>
</dependency>
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-data-mongodb</artifactId>
</dependency>
<dependency>
  <groupId>com.fasterxml.jackson.core</groupId>
  <artifactId>jackson-databind</artifactId>
  <scope>provided</scope>
</dependency>
```

- `jsf-leaf-id` 作为 Snowflake ID 来源
- `spring-boot-starter-data-mongodb` 为 compile 范围（核心注解依赖）
- `jackson-databind` 为 provided（由 spring-boot-starter-web 传递）

## 包结构

```
io.soil.jsf.mongodb
├── config
│   └── MongoAutoConfig.java
└── doc
    ├── BaseMongoDoc.java
    └── MongoDocIdCallback.java
```

## 不包含（YAGNI）

- ❌ `DefaultAuditorAware` — 由外部模块实现 AuditorAware
- ❌ `MongoProperties` — 无需额外配置属性
- ❌ `SoftDeleteRepository` — 无明确需求
- ❌ 自定义 Converter 注册 — 无通用场景
- ❌ Jackson 全局 Long→String 配置 — 由使用方按需配置
- ❌ 索引迁移脚本工具 — 由使用方选择 Mongock 等方案

## 使用方式

### 引入依赖

```xml
<dependency>
  <groupId>io.soil.jsf</groupId>
  <artifactId>jsf-mongodb</artifactId>
</dependency>
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-data-mongodb</artifactId>
</dependency>
```

### 定义 Document

```java
// 审计人为 Long 类型（默认，配合 AuditorAware<Long>）
@Document(collection = "users")
public class UserDoc extends BaseMongoDoc<Long> {
    @Field("name")
    private String name;
}

// 审计人为 String 类型（配合 AuditorAware<String>）
@Document(collection = "orders")
public class OrderDoc extends BaseMongoDoc<String> {
    @Field("orderNo")
    private String orderNo;
}
```

### 提供 AuditorAware Bean（外部模块）

```java
@Bean
public AuditorAware<Long> auditorAware() {
    return () -> {
        // 从 SecurityContext 或其他上下文获取当前用户 ID
        return Optional.of(currentUserId);
    };
}
```

## BOM 注册

在 `jsf-components-dependencies/pom.xml` 中添加 `jsf-mongodb` 版本管理。
在 `jsf-components/pom.xml` 中添加 `jsf-mongodb` 模块。