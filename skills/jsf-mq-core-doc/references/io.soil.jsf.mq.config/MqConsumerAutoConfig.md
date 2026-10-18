# MqConsumerAutoConfig

> jsf-mq 消费者侧自动配置：注册消费失败处理器（DISCARD 落库 + 重放编排）。

- **包**: `io.soil.jsf.mq.config`
- **注解**: `@Configuration` `@ConditionalOnClass(RocketMQTemplate.class)` `@ConditionalOnProperty("jsf.mq.consumer.enabled", matchIfMissing=true)` `@EnableConfigurationProperties(MqConsumerProperties.class)`

Store 实现由 jsf-mq-mongodb 等模块提供（依赖倒置）。

## 方法

### mqConsumeFailureHandler

`@Bean MqConsumeFailureHandler mqConsumeFailureHandler(MqConsumeFailureStore store)`

> 注册消费失败处理器，仅当容器存在 [MqConsumeFailureStore](#MqConsumeFailureStore) Bean 时启用。

- **相关**: [MqConsumeFailureHandler](#MqConsumeFailureHandler)
