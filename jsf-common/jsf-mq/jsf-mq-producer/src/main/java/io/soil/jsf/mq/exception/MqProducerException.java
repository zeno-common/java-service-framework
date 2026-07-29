package io.soil.jsf.mq.exception;

import io.soil.jsf.common.exception.BaseException;
import io.soil.jsf.common.exception.ExceptionType;

/**
 * jsf-mq 生产者侧统一异常。
 * <p>
 * 继承 {@link BaseException}，提供 {@code code()} 与 {@link java.text.MessageFormat} 风格的消息格式化能力。
 * 所有错误码采用 {@code [业务]-[编码]} 的命名规约，例如 {@code MQ-SEND-FAILED}。
 * </p>
 *
 * @author zeno.w
 * @see BaseException
 */
public class MqProducerException extends BaseException {

    /** 消息发送失败 */
    public static final String MQ_SEND_FAILED = "MQ-SEND-FAILED";
    /** MqProducer 不可用（RocketMQ 未初始化 / RocketMQTemplate 缺失） */
    public static final String MQ_PRODUCER_NOT_AVAILABLE = "MQ-PRODUCER-NOT-AVAILABLE";

    public MqProducerException(String code, String msgPattern, Object... msgArgs) {
        super(code, null, msgPattern, msgArgs);
    }

    public MqProducerException(String code, Throwable cause, String msgPattern, Object... msgArgs) {
        super(code, cause, msgPattern, msgArgs);
    }

    @Override
    public ExceptionType type() {
        return ExceptionType.SYS;
    }

    /** 构造消息发送失败异常（保留原始异常链） */
    public static MqProducerException sendFailed(Throwable cause, String msgPattern, Object... msgArgs) {
        return new MqProducerException(MQ_SEND_FAILED, cause, msgPattern, msgArgs);
    }

    /** 构造生产者不可用异常 */
    public static MqProducerException producerNotAvailable(String msgPattern, Object... msgArgs) {
        return new MqProducerException(MQ_PRODUCER_NOT_AVAILABLE, msgPattern, msgArgs);
    }
}
