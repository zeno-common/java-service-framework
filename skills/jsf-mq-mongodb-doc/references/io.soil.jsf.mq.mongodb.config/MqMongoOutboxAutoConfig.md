# MqMongoOutboxAutoConfig

> Outbox 存储（MongoDB 实现）自动配置。

- **包**: `io.soil.jsf.mq.mongodb.config`
- **注解**: `@Configuration` `@ConditionalOnClass(MongoTemplate.class)` `@ConditionalOnProperty("jsf.mq.producer.enabled", matchIfMissing=true)` `@AutoConfigureBefore(MqProducerAutoConfig.class)`

`@AutoConfigureBefore` 保证 Store Bean 先于生产者核心装配，使核心的 `@ConditionalOnBean(MqOutboxStore.class)` 成立。Bean 均 `@ConditionalOnMissingBean`，业务可覆盖。

## 方法

### mqOutboxStore

`@Bean MqOutboxStore mqOutboxStore(MongoTemplate mongoTemplate)`

> 注册 [MongoMqOutboxStore](#MongoMqOutboxStore)，当容器无 `MqOutboxStore` 时生效。

- **相关**: `io.soil.jsf.mq.core.outbox.MqOutboxStore`、[MongoMqOutboxStore](#MongoMqOutboxStore)
