package io.soil.jsf.mq.core.outbox;

import lombok.Getter;
import lombok.Setter;

/**
 * Outbox 消息（核心 POJO，与具体存储无关）。
 *
 * @author zeno.w
 */
@Getter
@Setter
public class MqOutboxMessage {

    /** 消息 ID（由存储实现生成并回填） */
    private Long id;

    /** 目标 topic */
    private String topic;

    /** 消息 tag（可空） */
    private String tag;

    /** 业务消息体 JSON */
    private String payload;

    /** 状态 */
    private MqOutboxStatus status = MqOutboxStatus.PENDING;

    /** 已尝试发送次数 */
    private int attempt;

    /** 下次可重试时间（epoch millis），PENDING 行仅在 nextRetryAt<=now 时会被 relay 捞取 */
    private long nextRetryAt;

    /** 认领锁过期时间（epoch millis），SENDING 行锁过期视为僵尸行 */
    private long lockExpireAt;

    /**
     * 发送目的地：{@code topic} 或 {@code topic:tag}。
     */
    public String destination() {
        return (tag == null || tag.isEmpty()) ? topic : topic + ":" + tag;
    }
}
