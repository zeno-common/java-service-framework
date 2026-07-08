package io.soil.jsf.common.exception;

import java.text.MessageFormat;
import java.util.Collections;

/**
 * 业务异常，用于表示违反业务规则（如前置条件不满足、状态机流转非法）。
 * <p>
 * 继承自 {@link BizException}，{@link #type()} 固定返回 {@link ExceptionType#BIZ}，
 * </p>
 *
 * @author zeno
 */
public class BizException extends BaseException {

  /**
   * 根据错误定义创建业务异常
   *
   * @param errorDefine 错误定义，提供异常码和消息
   * @return 业务异常对象
   */
  public static BizException of(ErrorDefine errorDefine, Object... msgArgs) {
    return of(null, errorDefine, msgArgs);
  }

  /**
   * 根据错误定义创建业务异常，可附带原始异常
   *
   * @param throwable   原始异常，可为 null
   * @param errorDefine 错误定义，提供异常码和消息
   * @return 业务异常对象
   */
  public  static BizException of(Throwable throwable, ErrorDefine errorDefine, Object... msgArgs) {
    return new BizException(errorDefine.code(),throwable, errorDefine.msg(), msgArgs);
  }

  protected BizException(String msg) {
    super( msg);
  }

  protected BizException(String msgPattern, Object... msgArgs) {
    super(msgPattern, msgArgs);
  }

  protected BizException(Throwable throwable) {
    super(throwable);
  }

  protected BizException(Throwable throwable, String msg) {
    super(throwable, msg);
  }

  protected BizException(Throwable throwable, String msgPattern, Object... msgArgs) {
    super(throwable, msgPattern, msgArgs);
  }

  protected BizException(String code, Throwable throwable) {
    super(code, throwable);
  }

  protected BizException(String code, Throwable throwable, String msg) {
    super(code, throwable, msg);
  }

  protected BizException(String code, Throwable throwable, String msgPattern, Object... msgArgs) {
    super(code,throwable, MessageFormat.format(msgPattern, msgArgs));
  }

  @Override
  public ExceptionType type() {
    return ExceptionType.BIZ;
  }
}
