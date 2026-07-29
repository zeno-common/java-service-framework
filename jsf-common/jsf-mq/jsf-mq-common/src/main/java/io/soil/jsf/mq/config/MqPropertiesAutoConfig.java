package io.soil.jsf.mq.config;

import io.soil.jsf.mq.config.properties.MqProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 启用 jsf-mq 配置属性 {@link MqProperties}（前缀 {@code jsf.mq}）。
 * <p>
 * 仅注册配置属性 Bean，不依赖 RocketMQ，可随 {@code jsf-mq-common} 单独引入用于配置托管。
 * </p>
 */
@Configuration
@EnableConfigurationProperties(MqProperties.class)
public class MqPropertiesAutoConfig {
}
