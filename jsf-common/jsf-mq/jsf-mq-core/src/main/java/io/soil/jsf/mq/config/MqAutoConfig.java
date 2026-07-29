package io.soil.jsf.mq.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.soil.jsf.mq.config.properties.MqProperties;
import io.soil.jsf.mq.core.MqProducer;
import io.soil.jsf.mq.core.failure.MqConsumeFailureHandler;
import io.soil.jsf.mq.core.failure.MqConsumeFailureStore;
import io.soil.jsf.mq.core.outbox.MqOutbox;
import io.soil.jsf.mq.core.outbox.MqOutboxRelay;
import io.soil.jsf.mq.core.outbox.MqOutboxStore;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * jsf-mq 自动配置类。
 * <p>
 * 当 classpath 下存在 {@link RocketMQTemplate}（即引入了 rocketmq-spring-boot-starter）时自动激活，
 * 向容器注册 {@link MqProducer} 与 {@link MqProperties}。
 * </p>
 * <p>
 * 可靠性组件按 Store Bean 存在性条件装配（依赖倒置，Store 实现由 jsf-mq-mongodb 等模块提供）：
 * <ul>
 *   <li>容器存在 {@link MqOutboxStore} → 注册 {@link MqOutbox}（立即投递）与 {@link MqOutboxRelay}（兜底补发）；</li>
 *   <li>容器存在 {@link MqConsumeFailureStore} → 注册 {@link MqConsumeFailureHandler}（失败落库/重放）。</li>
 * </ul>
 * </p>
 *
 * @author zeno.w
 */
@Configuration
@ConditionalOnClass(RocketMQTemplate.class)
@EnableConfigurationProperties(MqProperties.class)
public class MqAutoConfig {

    /**
     * 注册统一消息生产者。
     *
     * @param rocketMQTemplate RocketMQ Spring Starter 提供的模板（由 starter 自动配置）
     * @return MqProducer 实例
     */
    @Bean
    @ConditionalOnMissingBean
    public MqProducer mqProducer(RocketMQTemplate rocketMQTemplate) {
        return new MqProducer(rocketMQTemplate);
    }

    /**
     * 注册 Outbox 编排入口（事务内落库 + 提交后立即投递）。
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBean(MqOutboxStore.class)
    public MqOutbox mqOutbox(MqOutboxStore store, MqProducer producer,
                             ObjectProvider<ObjectMapper> objectMapper, MqProperties properties) {
        return new MqOutbox(store, producer, objectMapper.getIfAvailable(ObjectMapper::new), properties.getOutbox());
    }

    /**
     * 注册 Outbox relay 兜底补发器（需应用开启 {@code @EnableScheduling}）。
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBean(MqOutbox.class)
    @ConditionalOnProperty(prefix = "jsf.mq.outbox.relay", name = "enabled", havingValue = "true", matchIfMissing = true)
    public MqOutboxRelay mqOutboxRelay(MqOutboxStore store, MqOutbox outbox, MqProperties properties) {
        return new MqOutboxRelay(store, outbox, properties.getOutbox());
    }

    /**
     * 注册消费失败处理器（DISCARD 落库 + 重放编排）。
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBean(MqConsumeFailureStore.class)
    public MqConsumeFailureHandler mqConsumeFailureHandler(MqConsumeFailureStore store) {
        return new MqConsumeFailureHandler(store);
    }
}
