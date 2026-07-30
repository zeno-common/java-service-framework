package io.soil.jsf.mq.config;

import io.soil.jsf.mq.config.properties.MqConsumerProperties;
import io.soil.jsf.mq.core.failure.MqConsumeFailureHandler;
import io.soil.jsf.mq.core.failure.MqConsumeFailureStore;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * jsf-mq 消费者侧自动配置：注册消费失败处理器（DISCARD 落库 + 重放编排）。
 * <p>
 * 容器存在 {@link MqConsumeFailureStore} 时启用（依赖倒置），Store 实现由 jsf-mq-mongodb 模块提供。
 * </p>
 *
 * @author zeno.w
 */
@Configuration
@ConditionalOnClass(RocketMQTemplate.class)
@ConditionalOnProperty(prefix = "jsf.mq.consumer", name = "enabled", havingValue = "true", matchIfMissing = true)
@EnableConfigurationProperties(MqConsumerProperties.class)
public class MqConsumerAutoConfig {

    /** 注册消费失败处理器（DISCARD 落库 / 重放编排）。 */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBean(MqConsumeFailureStore.class)
    public MqConsumeFailureHandler mqConsumeFailureHandler(MqConsumeFailureStore store) {
        return new MqConsumeFailureHandler(store);
    }
}
