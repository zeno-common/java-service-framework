# MqProducerAutoConfig

> jsf-mq 生产者侧自动配置：注册统一生产者与 Outbox 可靠发送的编排、兜底 relay。

- **包**: `io.soil.jsf.mq.config`
- **注解**: `@Configuration` `@ConditionalOnClass(RocketMQTemplate.class)` `@ConditionalOnProperty("jsf.mq.producer.enabled", matchIfMissing=true)` `@EnableConfigurationProperties(MqProducerProperties.class)`

基于接口条件装配（依赖倒置），Store 实现由 jsf-mq-mongodb 等模块提供。

## 方法

### mqProducer

`@Bean MqProducer mqProducer(RocketMQTemplate rocketMQTemplate)`

> 注册统一消息生产者（`@ConditionalOnMissingBean`）。

### mqOutbox

`@Bean MqOutbox mqOutbox(MqOutboxStore store, MqProducer producer, ObjectProvider<ObjectMapper> objectMapper, MqProducerProperties properties)`

> 注册 Outbox 编排入口，仅当容器存在 [MqOutboxStore](#MqOutboxStore) Bean 时启用。

### mqOutboxRelay

`@Bean MqOutboxRelay mqOutboxRelay(MqOutboxStore store, MqOutbox outbox, MqProducerProperties properties)`

> 注册 Outbox relay 兜底补发器，需容器有 [MqOutbox](#MqOutbox) 且 `jsf.mq.producer.outbox.relay.enabled=true`（默认）。

- **相关**: [MqProducer](#MqProducer)、[MqOutbox](#MqOutbox)、[MqOutboxRelay](#MqOutboxRelay)
