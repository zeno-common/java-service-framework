# MqMongoConsumeAutoConfig

> 消费者侧可靠性组件（幂等去重、失败记录）MongoDB 实现自动配置。

- **包**: `io.soil.jsf.mq.mongodb.config`
- **注解**: `@Configuration` `@ConditionalOnClass(MongoTemplate.class)` `@ConditionalOnProperty("jsf.mq.consumer.enabled", matchIfMissing=true)` `@AutoConfigureBefore(MqConsumerAutoConfig.class)`

`@AutoConfigureBefore` 保证 Store Bean 先于消费者核心装配，使核心 `@ConditionalOnBean(MqConsumeFailureStore.class)` 成立。Bean 均 `@ConditionalOnMissingBean`，业务可覆盖。

## 方法

### mqIdempotentStore

`@Bean MqIdempotentStore mqIdempotentStore(MongoTemplate mongoTemplate)`

> 注册 [MongoMqIdempotentStore](#MongoMqIdempotentStore)，当容器无 `MqIdempotentStore` 时生效。

### mqConsumeFailureStore

`@Bean MqConsumeFailureStore mqConsumeFailureStore(MongoTemplate mongoTemplate)`

> 注册 [MongoMqConsumeFailureStore](#MongoMqConsumeFailureStore)，当容器无 `MqConsumeFailureStore` 时生效。

- **相关**: `io.soil.jsf.mq.core.idempotent.MqIdempotentStore`、`io.soil.jsf.mq.core.failure.MqConsumeFailureStore`
