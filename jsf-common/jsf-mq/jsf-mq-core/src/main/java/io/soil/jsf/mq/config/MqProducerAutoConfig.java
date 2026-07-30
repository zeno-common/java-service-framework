package io.soil.jsf.mq.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.soil.jsf.mq.config.properties.MqProducerProperties;
import io.soil.jsf.mq.core.MqProducer;
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
 * jsf-mq 生产者侧自动配置：注册统一生产者，以及 Outbox 可靠发送的编排与兜底 relay。
 * <ul>
 *   <li>容器存在 {@link MqOutboxStore} → 注册 {@link MqOutbox}（立即投递）与 {@link MqOutboxRelay}（兜底补发）；</li>
 * </ul>
 * 全部基于接口条件装配（依赖倒置），Store 实现由 jsf-mq-mongodb 模块提供。
 *
 * @author zeno.w
 */
@Configuration
@ConditionalOnClass(RocketMQTemplate.class)
@ConditionalOnProperty(prefix = "jsf.mq.producer", name = "enabled", havingValue = "true", matchIfMissing = true)
@EnableConfigurationProperties(MqProducerProperties.class)
public class MqProducerAutoConfig {

    /** 注册统一消息生产者。 */
    @Bean
    @ConditionalOnMissingBean
    public MqProducer mqProducer(RocketMQTemplate rocketMQTemplate) {
        return new MqProducer(rocketMQTemplate);
    }

    /** 注册 Outbox 编排入口（事务内落库 + 提交后立即投递）。 */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBean(MqOutboxStore.class)
    public MqOutbox mqOutbox(MqOutboxStore store, MqProducer producer,
                             ObjectProvider<ObjectMapper> objectMapper, MqProducerProperties properties) {
        return new MqOutbox(store, producer, objectMapper.getIfAvailable(ObjectMapper::new), properties.getOutbox());
    }

    /** 注册 Outbox relay 兜底补发器（需应用开启 {@code @EnableScheduling}）。 */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBean(MqOutbox.class)
    @ConditionalOnProperty(prefix = "jsf.mq.producer.outbox.relay", name = "enabled", havingValue = "true", matchIfMissing = true)
    public MqOutboxRelay mqOutboxRelay(MqOutboxStore store, MqOutbox outbox, MqProducerProperties properties) {
        return new MqOutboxRelay(store, outbox, properties.getOutbox());
    }
}
