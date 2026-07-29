package io.soil.jsf.mq.mongodb.config;

import io.soil.jsf.mq.config.MqProducerAutoConfig;
import io.soil.jsf.mq.core.outbox.MqOutboxStore;
import io.soil.jsf.mq.mongodb.store.MongoMqOutboxStore;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

/**
 * jsf-mq 生产者侧可靠性组件（Outbox）的 MongoDB 实现自动配置。
 * <p>
 * {@code @AutoConfigureBefore(MqProducerAutoConfig.class)} 保证 Store Bean 先于生产者核心装配，
 * 使核心的 {@code @ConditionalOnBean(MqOutboxStore.class)} 条件成立。
 * 所有 Bean 均为 {@code @ConditionalOnMissingBean}，业务可自定义实现覆盖（依赖倒置）。
 * </p>
 *
 * @author zeno.w
 */
@Configuration
@ConditionalOnClass(MongoTemplate.class)
@AutoConfigureBefore(MqProducerAutoConfig.class)
public class MqMongoOutboxAutoConfig {

    /** Outbox 存储（MongoDB 实现） */
    @Bean
    @ConditionalOnMissingBean(MqOutboxStore.class)
    public MqOutboxStore mqOutboxStore(MongoTemplate mongoTemplate) {
        return new MongoMqOutboxStore(mongoTemplate);
    }
}
