package io.soil.jsf.common.exception;

import java.text.MessageFormat;
import java.util.Collections;

/**
 * 业务异常，用于表示违反业务规则（如前置条件不满足、状态机流转非法）。
 * <p>
 * 继承自 {@link BaseException}，{@link #type()} 固定返回 {@link ExceptionType#BIZ}，
 * </p>
 *
 * @author zeno
 */
public class BizException extends BaseException {

  public BizException(String msg) {
    this( msg, Collections.emptyList());
  }

  public BizException(String msgPattern, Object... msgArgs) {
    this(null, msgPattern, msgArgs);
  }

  public BizException(Throwable throwable, String msg) {
    this(throwable, msg, Collections.emptyList());
  }

  public BizException(Throwable throwable) {
    this(throwable, throwable.getMessage(), Collections.emptyList());
  }

  public BizException(Throwable throwable, String msgPattern, Object... msgArgs) {
    this("UNDEFINED", throwable, msgPattern, msgArgs);
  }

  public BizException(String code, String msg) {
    this( code, (String)null, msg, Collections.emptyList());
  }

  public BizException(String code, Throwable throwable, String msg) {
    this(code, throwable, msg, Collections.emptyList());
  }

  public BizException(String code, Throwable throwable, String msgPattern, Object... msgArgs) {
    super(code, throwable, MessageFormat.format(msgPattern, msgArgs));
  }

  @Override
  public ExceptionType type() {
    return ExceptionType.BIZ;
  }
}
