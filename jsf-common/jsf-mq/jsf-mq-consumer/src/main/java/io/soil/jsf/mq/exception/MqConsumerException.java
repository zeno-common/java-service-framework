package io.soil.jsf.mq.exception;

import io.soil.jsf.common.exception.BaseException;
import io.soil.jsf.common.exception.ExceptionType;

/**
 * jsf-mq 消费者侧统一异常。
 * <p>
 * 继承 {@link BaseException}，提供 {@code code()} 与 {@link java.text.MessageFormat} 风格的消息格式化能力。
 * 所有错误码采用 {@code [业务]-[编码]} 的命名规约，例如 {@code MQ-CONSUME-FAILED}。
 * </p>
 *
 * @author zeno.w
 * @see BaseException
 */
public class MqConsumerException extends BaseException {

    /** 消息消费失败 */
    public static final String MQ_CONSUME_FAILED = "MQ-CONSUME-FAILED";

    public MqConsumerException(String code, String msgPattern, Object... msgArgs) {
        super(code, null, msgPattern, msgArgs);
    }

    public MqConsumerException(String code, Throwable cause, String msgPattern, Object... msgArgs) {
        super(code, cause, msgPattern, msgArgs);
    }

    @Override
    public ExceptionType type() {
        return ExceptionType.SYS;
    }

    /** 构造消息消费失败异常（保留原始异常链） */
    public static MqConsumerException consumeFailed(Throwable cause, String msgPattern, Object... msgArgs) {
        return new MqConsumerException(MQ_CONSUME_FAILED, cause, msgPattern, msgArgs);
    }
}
