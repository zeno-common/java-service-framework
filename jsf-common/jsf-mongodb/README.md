# jsf-mongodb

基于 Spring Data MongoDB 的封装模块，提供文档基类与自动配置：Snowflake ID 自动生成、审计字段自动填充、多文档事务支持。引入即生效。

## 功能特性

- **文档基类 `BaseMongoDoc<U>`** — 统一封装 `id` / `createdAt` / `updatedAt` / `createdBy` / `updatedBy` 公共字段，减少样板代码
- **Snowflake ID 自动生成** — `MongoDocIdCallback` 在实体保存前（BeforeConvert 阶段）为 `id == null` 的文档生成 64 位 ID，ID 复用 [jsf-leaf-id](../../jsf-tools/jsf-leaf-id/README.md)
- **审计字段自动填充** — `@EnableMongoAuditing` 激活 `@CreatedDate` / `@LastModifiedDate` / `@CreatedBy` / `@LastModifiedBy`
- **多文档事务** — 自动注册 `MongoTransactionManager`，Service 层可直接使用 `@Transactional`
- **ID 精度安全** — Long 型 ID 通过 `@JsonSerialize(ToStringSerializer)` 序列化为 String，防止 JavaScript 精度丢失

## 引入依赖

前置条件：通过 [jsf-bom](../../jsf-bom/README.md) 管理依赖版本。

```xml
<dependency>
    <groupId>io.soil.jsf</groupId>
    <artifactId>jsf-mongodb</artifactId>
</dependency>
```

## 自动配置

引入依赖后自动加载 `MongoAutoConfig`（`@ConditionalOnClass(MongoTemplate)`），装配：

| Bean | 说明 |
|------|------|
| `MongoTransactionManager` | `@Transactional` 多文档事务（**需 MongoDB 副本集**，单机不支持） |
| `MongoDocIdCallback` | Snowflake ID 生成回调 |

所有 Bean 均 `@ConditionalOnMissingBean`，业务可自定义覆盖。

## 使用示例

### 定义文档

```java
// 审计人为 Long（默认）
@Document(collection = "users")
public class UserDoc extends BaseMongoDoc<Long> {
    @Field("name")
    private String name;
}

// 审计人为 String
@Document(collection = "orders")
public class OrderDoc extends BaseMongoDoc<String> {
    @Field("orderNo")
    private String orderNo;
}
```

保存时 `id` 自动生成，`createdAt` / `updatedAt` 由审计机制自动填充。

### 审计人填充

`createdBy` / `updatedBy` 的自动填充需要外部模块提供 `AuditorAware<U>` Bean，其泛型 `U` 必须与 `BaseMongoDoc<U>` 的审计人类型一致。

### 事务

```java
@Transactional
public void transfer(...) {
    // 多文档原子操作（需副本集）
}
```

## 关键设计

- ID 结构（与 jsf-leaf-id 一致）：`符号位(1) | 时间戳(42) | 数据中心(5) | 工作节点(8) | 毫秒内序号(8)`
- `version`（乐观锁）与 `deleted`（逻辑删除）**不在基类中**，由子类按需添加
- ID 仅在为 `null` 时生成，避免覆盖已有 ID

## 前置条件

- 引入 `spring-boot-starter-data-mongodb`
- 引入 `jsf-leaf-id`（Snowflake ID 来源）
- 配置 `spring.data.mongodb.uri`
- 提供 `AuditorAware` Bean（审计人自动填充时）

## 项目结构

```
io.soil.jsf.mongodb/
├── config/
│   └── MongoAutoConfig.java       # 自动配置：事务管理器 + ID 回调 + 审计
└── doc/
    ├── BaseMongoDoc.java          # 文档抽象基类
    └── MongoDocIdCallback.java    # Snowflake ID 生成回调
```
