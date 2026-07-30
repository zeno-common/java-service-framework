package io.soil.jsf.mq.config.properties;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * jsf-mq 消费者侧配置属性，前缀 {@code jsf.mq.consumer}。
 * <p>
 * NameServer 默认读取 RocketMQ Spring Starter 的 {@code rocketmq.name-server} 配置，
 * 未配置时回退到 {@code 127.0.0.1:9876}。
 * </p>
 *
 * @author zeno.w
 */
@Data
@ConfigurationProperties(prefix = "jsf.mq.consumer")
public class MqConsumerProperties {

    /** 是否启用消费者（本地开发可关闭以避免连接 RocketMQ） */
    private boolean enabled = true;
}
