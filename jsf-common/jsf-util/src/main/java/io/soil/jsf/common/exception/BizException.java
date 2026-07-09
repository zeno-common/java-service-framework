package io.soil.jsf.common.exception;

import java.text.MessageFormat;

/**
 * 业务异常，用于表示违反业务规则（如前置条件不满足、状态机流转非法）。
 * <p>
 * 继承自 {@link BizException}，{@link #type()} 固定返回 {@link ExceptionType#BIZ}，
 * </p>
 *
 * @author zeno
 */
public  class BizException extends BaseException {

  public BizException(String msg) {
    super( msg);
  }

  public BizException(String msgPattern, Object... msgArgs) {
    super(msgPattern, msgArgs);
  }

  public BizException(Throwable throwable) {
    super(throwable);
  }

  public BizException(Throwable throwable, String msg) {
    super(throwable, msg);
  }

  public BizException(Throwable throwable, String msgPattern, Object... msgArgs) {
    super(throwable, msgPattern, msgArgs);
  }

  public BizException(String code, Throwable throwable) {
    super(code, throwable);
  }

  public BizException(String code, Throwable throwable, String msg) {
    super(code, throwable, msg);
  }

  public BizException(String code, Throwable throwable, String msgPattern, Object... msgArgs) {
    super(code,throwable, MessageFormat.format(msgPattern, msgArgs));
  }

  @Override
  public ExceptionType type() {
    return ExceptionType.BIZ;
  }
}
