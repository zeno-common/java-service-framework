package io.soil.jsf.mail.sender.exception;

/**
 * 邮件模块运行时异常, 用于包装 SMTP 发送过程中的技术异常(地址解析失败、发送失败等).
 * <p>
 * 携带错误码 {@link #code()}, 便于在日志与监控中快速定位邮件相关错误来源.
 *
 * @author zeno
 */
public class MailException extends RuntimeException {

    /** 收件人为空 */
    public static final String RECIPIENT_EMPTY = "MAIL-RECIPIENT-EMPTY";
    /** 发件人未配置 */
    public static final String FROM_NOT_CONFIGURED = "MAIL-FROM-NOT-CONFIGURED";
    /** 邮件发送失败 */
    public static final String SEND_FAILED = "MAIL-SEND-FAILED";

    private final String code;

    public MailException(String code, String message) {
        super(message);
        this.code = code;
    }

    public MailException(String code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    /**
     * 获取错误码
     *
     * @return 错误码
     */
    public String code() {
        return code;
    }
}
