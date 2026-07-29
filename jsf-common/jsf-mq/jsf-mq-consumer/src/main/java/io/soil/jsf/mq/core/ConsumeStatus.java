package io.soil.jsf.mq.core;

/**
 * 消费结果状态，由 {@link AbstractMqConsumer#handleMessage(Object, MqConsumeContext)} 返回，
 * 基类据此决定 ack / broker 重试 / 失败落库。
 *
 * @author zeno.w
 */
public enum ConsumeStatus {

    /** 消费成功，正常确认（ack） */
    SUCCESS,

    /**
     * 稍后重试：抛出异常触发 RocketMQ broker 重试（指数退避，默认 16 次后进入
     * {@code %DLQ%+consumerGroup} 死信队列）。适用于瞬时故障（下游超时、连接异常等）。
     */
    RETRY_LATER,

    /**
     * 丢弃不重试：判定为不可重试的失败（数据格式错误、业务规则拒绝等），
     * 交由 {@code MqConsumeFailureHandler} 持久化失败记录，供人工/定时重放补偿。
     */
    DISCARD
}
