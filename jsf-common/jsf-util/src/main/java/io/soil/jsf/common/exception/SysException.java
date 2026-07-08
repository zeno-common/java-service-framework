package io.soil.jsf.common.exception;

import java.text.MessageFormat;
import java.util.Collections;

/**
 * 系统异常，用于表示基础设施层捕获并包装的技术异常（如数据库访问失败、外部服务调用异常）。
 * <p>
 * 继承自 {@link BaseException}，{@link #type()} 固定返回 {@link ExceptionType#SYS}
 * </p>
 *
 * @author zeno
 */
public class SysException extends BaseException {

  protected SysException(String msg) {
    this( msg, Collections.emptyList());
  }

  protected SysException(String msgPattern, Object... msgArgs) {
    this(null, msgPattern, msgArgs);
  }

  protected SysException(Throwable throwable, String msg) {
    this(throwable, msg, Collections.emptyList());
  }

  protected SysException(Throwable throwable) {
    this(throwable, throwable.getMessage(), Collections.emptyList());
  }

  protected SysException(Throwable throwable, String msgPattern, Object... msgArgs) {
    this("UNDEFINED", throwable, msgPattern, msgArgs);
  }

  protected SysException(String code, String msg) {
    this( code, (String)null, msg, Collections.emptyList());
  }

  protected SysException(String code, Throwable throwable, String msg) {
    this(code, throwable, msg, Collections.emptyList());
  }

  protected SysException(String code, Throwable throwable, String msgPattern, Object... msgArgs) {
    super(code, throwable, MessageFormat.format(msgPattern, msgArgs));
  }

  @Override
  public ExceptionType type() {
    return ExceptionType.SYS;
  }
}
