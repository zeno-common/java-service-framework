package io.soil.jsf.mq.core;

import org.apache.rocketmq.common.message.MessageExt;

import java.util.Collections;
import java.util.Map;

/**
 * 消费上下文，封装消息元信息（不侵入业务 payload）。
 * <p>
 * 由 {@link AbstractMqConsumer} 从 RocketMQ {@link MessageExt} 提取，供
 * {@code handleMessage(T, MqConsumeContext)} 或 {@code idempotentKey(T, MqConsumeContext)} 读取
 * msgId / keys / traceId 等元信息，用于幂等、链路追踪与失败排查。
 * </p>
 *
 * @author zeno.w
 */
public class MqConsumeContext {

    private final String messageId;

    private final String topic;

    private final String tag;

    private final long bornTimestamp;

    private final String keys;

    private final Map<String, String> properties;

    public MqConsumeContext(MessageExt messageExt) {
        this.messageId = messageExt.getMsgId();
        this.topic = messageExt.getTopic();
        this.tag = messageExt.getTags();
        this.bornTimestamp = messageExt.getBornTimestamp();
        this.keys = messageExt.getKeys();
        this.properties = messageExt.getProperties() == null
                ? Collections.emptyMap()
                : Collections.unmodifiableMap(messageExt.getProperties());
    }

    /** 消息 ID（RocketMQ msgId） */
    public String getMessageId() {
        return messageId;
    }

    /** 消息 topic */
    public String getTopic() {
        return topic;
    }

    /** 消息 tag（真实消息 tag，而非消费端 selector 表达式） */
    public String getTag() {
        return tag;
    }

    /** 消息 born 时间戳（epoch millis） */
    public long getBornTimestamp() {
        return bornTimestamp;
    }

    /** RocketMQ keys（若发送时设置了 {@code MqSendOptions.keys}） */
    public String getKeys() {
        return keys;
    }

    /** 原始属性表（含 RocketMQ 系统属性与发送方自定义用户属性） */
    public Map<String, String> getProperties() {
        return properties;
    }

    /** 读取自定义用户属性（如 traceId / eventType） */
    public String getProperty(String key) {
        return properties.get(key);
    }
}
