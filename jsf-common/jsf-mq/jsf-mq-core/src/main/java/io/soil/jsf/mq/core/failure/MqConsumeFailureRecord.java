package io.soil.jsf.mq.core.failure;

import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;

/**
 * 消费失败记录（核心 POJO，与具体存储无关）。
 * <p>
 * 消费者判定 {@code DISCARD} 时由 {@link MqConsumeFailureHandler} 持久化，
 * 保留完整的消息上下文（payload + 异常 + 元数据），供后续人工排查与重放补偿。
 * </p>
 *
 * @author zeno.w
 */
@Getter
@Setter
public class MqConsumeFailureRecord {

    /** 记录 ID（由存储实现生成并回填） */
    private Long id;

    /** 消息 topic */
    private String topic;

    /** 消息 tag */
    private String tag;

    /** 消费者组 */
    private String consumerGroup;

    /** 消费者类全名 */
    private String consumerClass;

    /** 消息 ID（如有） */
    private String messageId;

    /** 业务键 / 幂等键（如有） */
    private String bizKey;

    /** 消息体 JSON */
    private String payload;

    /** 异常摘要 */
    private String errorMsg;

    /** 异常堆栈 */
    private String stackTrace;

    /** 记录状态 */
    private MqConsumeFailureStatus status = MqConsumeFailureStatus.PENDING;

    /** 失败发生时间 */
    private OffsetDateTime failedAt;

    /** 重放时间 */
    private OffsetDateTime replayedAt;
}
