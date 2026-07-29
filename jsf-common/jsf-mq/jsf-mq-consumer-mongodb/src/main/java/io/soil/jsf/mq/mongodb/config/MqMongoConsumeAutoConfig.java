package io.soil.jsf.mq.mongodb.config;

import io.soil.jsf.mq.config.MqConsumerAutoConfig;
import io.soil.jsf.mq.core.failure.MqConsumeFailureStore;
import io.soil.jsf.mq.core.idempotent.MqIdempotentStore;
import io.soil.jsf.mq.mongodb.store.MongoMqConsumeFailureStore;
import io.soil.jsf.mq.mongodb.store.MongoMqIdempotentStore;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

/**
 * jsf-mq 消费者侧可靠性组件（幂等去重、消费失败记录）的 MongoDB 实现自动配置。
 * <p>
 * {@code @AutoConfigureBefore(MqConsumerAutoConfig.class)} 保证 Store Bean 先于消费者核心装配，
 * 使核心的 {@code @ConditionalOnBean(MqConsumeFailureStore.class)} 条件成立。
 * 所有 Bean 均为 {@code @ConditionalOnMissingBean}，业务可自定义实现覆盖（依赖倒置）。
 * </p>
 *
 * @author zeno.w
 */
@Configuration
@ConditionalOnClass(MongoTemplate.class)
@ConditionalOnProperty(prefix = "jsf.mq.consumer", name = "enabled", havingValue = "true", matchIfMissing = true)
@AutoConfigureBefore(MqConsumerAutoConfig.class)
public class MqMongoConsumeAutoConfig {

    /** 消费幂等存储（MongoDB 实现） */
    @Bean
    @ConditionalOnMissingBean(MqIdempotentStore.class)
    public MqIdempotentStore mqIdempotentStore(MongoTemplate mongoTemplate) {
        return new MongoMqIdempotentStore(mongoTemplate);
    }

    /** 消费失败记录存储（MongoDB 实现） */
    @Bean
    @ConditionalOnMissingBean(MqConsumeFailureStore.class)
    public MqConsumeFailureStore mqConsumeFailureStore(MongoTemplate mongoTemplate) {
        return new MongoMqConsumeFailureStore(mongoTemplate);
    }
}
