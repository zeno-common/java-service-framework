package io.soil.jsf.mq.mongodb.config;

import io.soil.jsf.mq.config.MqAutoConfig;
import io.soil.jsf.mq.core.failure.MqConsumeFailureStore;
import io.soil.jsf.mq.core.idempotent.MqIdempotentStore;
import io.soil.jsf.mq.core.outbox.MqOutboxStore;
import io.soil.jsf.mq.mongodb.store.MongoMqConsumeFailureStore;
import io.soil.jsf.mq.mongodb.store.MongoMqIdempotentStore;
import io.soil.jsf.mq.mongodb.store.MongoMqOutboxStore;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

/**
 * jsf-mq-mongodb 自动配置类：向容器注册 jsf-mq 三个 Store 接口的 MongoDB 实现。
 * <p>
 * {@code @AutoConfigureBefore(MqAutoConfig.class)} 保证 Store Bean 先于 jsf-mq 核心装配，
 * 使核心的 {@code @ConditionalOnBean(XxxStore.class)} 条件成立。
 * 所有 Bean 均为 {@code @ConditionalOnMissingBean}，业务可自定义实现覆盖（依赖倒置）。
 * </p>
 *
 * @author zeno.w
 */
@Configuration
@ConditionalOnClass(MongoTemplate.class)
@AutoConfigureBefore(MqAutoConfig.class)
public class MqMongoAutoConfig {

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

    /** Outbox 存储（MongoDB 实现） */
    @Bean
    @ConditionalOnMissingBean(MqOutboxStore.class)
    public MqOutboxStore mqOutboxStore(MongoTemplate mongoTemplate) {
        return new MongoMqOutboxStore(mongoTemplate);
    }
}
