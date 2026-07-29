package io.soil.jsf.mq.core.failure;

/**
 * 消费失败原因，标识一条失败记录是如何产生的，供排查与重放策略区分。
 *
 * @author zeno.w
 */
public enum MqConsumeFailureReason {

    /** 业务主动 {@code DISCARD}：判定不可重试（格式错误、业务拒绝等），无异常或携带业务自定义异常。 */
    DISCARDED,

    /** broker 重试耗尽（默认 16 次）后仍未成功，消息即将进入死信队列（%DLQ%）前由框架捕获落库。 */
    RETRY_EXHAUSTED
}
