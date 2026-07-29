package io.soil.jsf.mq.core.failure;

/**
 * 消费失败记录状态。
 *
 * @author zeno.w
 */
public enum MqConsumeFailureStatus {

    /** 待处理（刚落库，等待人工/定时重放） */
    PENDING,

    /** 已重放 */
    REPLAYED
}
