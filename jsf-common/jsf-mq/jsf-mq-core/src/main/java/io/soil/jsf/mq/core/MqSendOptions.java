package io.soil.jsf.mq.core;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

/**
 * 发送元信息选项，承载消息级元信息（不污染业务 payload）。
 * <p>
 * 元信息通过 RocketMQ 的 keys / 用户属性（user properties）承载，而非把业务 payload 包成信封上链，
 * 以保持链路字节为纯业务 JSON，兼容 Outbox 存储与既有消费者。consumer 侧通过 {@link io.soil.jsf.mq.core.MqConsumeContext} 读取。
 * </p>
 *
 * @author zeno.w
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MqSendOptions {

    /**
     * RocketMQ keys（业务键 / 幂等键），consumer 侧 {@code MqConsumeContext.getKeys()} 可读。
     */
    private String keys;

    /**
     * 自定义用户属性（如 traceId / eventType / schemaVersion），consumer 侧 {@code MqConsumeContext.getProperty(k)} 可读。
     */
    private Map<String, String> properties;

    /** 空选项单例（等同不携带任何元信息） */
    public static final MqSendOptions EMPTY = MqSendOptions.builder().build();
}
