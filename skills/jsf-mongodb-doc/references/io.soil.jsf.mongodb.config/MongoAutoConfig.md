# MongoAutoConfig

> jsf-mongodb 自动配置类，当 classpath 存在 `MongoTemplate` 时激活。

- **包**: `io.soil.jsf.mongodb.config`
- **注解**: `@Configuration` `@ConditionalOnClass(MongoTemplate.class)` `@EnableMongoAuditing`

激活审计注解（`@CreatedDate` / `@LastModifiedDate` / `@CreatedBy` / `@LastModifiedBy`），注册事务管理器与 Snowflake ID 回调。所有 Bean 均 `@ConditionalOnMissingBean`，业务可覆盖。

## 方法

### transactionManager

`MongoTransactionManager transactionManager(MongoDatabaseFactory mongoDatabaseFactory)`

> 注册 MongoDB 事务管理器，启用 `@Transactional` 多文档事务。**需副本集**，单机不支持。

**参数**:
- `mongoDatabaseFactory` (MongoDatabaseFactory) — MongoDB 数据库工厂

**返回**: MongoTransactionManager — 事务管理器

### leafIdCallback

`MongoDocIdCallback leafIdCallback()`

> 注册 Snowflake ID 生成回调，保存前为 `id == null` 的 [BaseMongoDoc](#BaseMongoDoc) 子类生成 ID。

**返回**: MongoDocIdCallback — Entity Callback 实例

## 前置条件

- 引入 `spring-boot-starter-data-mongodb` 与 `jsf-leaf-id`
- 配置 `spring.data.mongodb.uri`
- 提供 `AuditorAware<U>` Bean（审计人自动填充时）
