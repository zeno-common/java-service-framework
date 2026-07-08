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

  /**
   * 根据错误定义创建系统异常
   *
   * @param errorDefine 错误定义，提供异常码和消息模板
   * @param msgArgs     消息模板参数，用于 {@link MessageFormat#format} 格式化
   * @return 系统异常对象
   */
  public static SysException of(ErrorDefine errorDefine, Object... msgArgs) {
    return of(null, errorDefine, msgArgs);
  }

  /**
   * 根据错误定义创建系统异常，可附带原始异常
   *
   * @param throwable   原始异常，可为 null
   * @param errorDefine 错误定义，提供异常码和消息模板
   * @param msgArgs     消息模板参数，用于 {@link MessageFormat#format} 格式化
   * @return 系统异常对象
   */
  public  static SysException of(Throwable throwable, ErrorDefine errorDefine, Object... msgArgs) {
    return new SysException(errorDefine.code(),throwable, errorDefine.msg(), msgArgs);
  }

  protected SysException(String msg) {
    super( msg);
  }

  protected SysException(String msgPattern, Object... msgArgs) {
    super(msgPattern, msgArgs);
  }

  protected SysException(Throwable throwable) {
    super(throwable);
  }

  protected SysException(Throwable throwable, String msg) {
    super(throwable, msg);
  }

  protected SysException(Throwable throwable, String msgPattern, Object... msgArgs) {
    super(throwable, msgPattern, msgArgs);
  }

  protected SysException(String code, Throwable throwable) {
    super(code, throwable);
  }

  protected SysException(String code, Throwable throwable, String msg) {
    super(code, throwable, msg);
  }

  protected SysException(String code, Throwable throwable, String msgPattern, Object... msgArgs) {
    super(code,throwable, MessageFormat.format(msgPattern, msgArgs));
  }

  @Override
  public ExceptionType type() {
    return ExceptionType.SYS;
  }
}
