package io.soil.jsf.common.exception;

import java.text.MessageFormat;
import java.util.Collections;

/**
 * 参数校验异常，用于表示请求入参不合法（如非空、格式校验失败等）。在 Adapter 层进行参数校验时抛出。
 * <p>
 * 继承自 {@link BaseException}，{@link #type()} 固定返回 {@link ExceptionType#PARAM}，
 * </p>
 *
 * @author zeno
 */
public class ParamException extends BaseException {

  /**
   * 根据错误定义创建参数校验异常
   *
   * @param errorDefine 错误定义，提供异常码和消息模板
   * @param msgArgs     消息模板参数，用于 {@link MessageFormat#format} 格式化
   * @return 参数校验异常对象
   */
  public static ParamException of(ErrorDefine errorDefine, Object... msgArgs) {
    return of(null, errorDefine, msgArgs);
  }

  /**
   * 根据错误定义创建参数校验异常，可附带原始异常
   *
   * @param throwable   原始异常，可为 null
   * @param errorDefine 错误定义，提供异常码和消息模板
   * @param msgArgs     消息模板参数，用于 {@link MessageFormat#format} 格式化
   * @return 参数校验异常对象
   */
  public  static ParamException of(Throwable throwable, ErrorDefine errorDefine, Object... msgArgs) {
    return new ParamException(errorDefine.code(),throwable, errorDefine.msg(), msgArgs);
  }


  protected ParamException(String msg) {
    super( msg);
  }

  protected ParamException(String msgPattern, Object... msgArgs) {
    super(msgPattern, msgArgs);
  }

  protected ParamException(Throwable throwable) {
    super(throwable);
  }

  protected ParamException(Throwable throwable, String msg) {
    super(throwable, msg);
  }

  protected ParamException(Throwable throwable, String msgPattern, Object... msgArgs) {
    super(throwable, msgPattern, msgArgs);
  }

  protected ParamException(String code, Throwable throwable) {
    super(code, throwable);
  }

  protected ParamException(String code, Throwable throwable, String msg) {
    super(code, throwable, msg);
  }

  protected ParamException(String code, Throwable throwable, String msgPattern, Object... msgArgs) {
    super(code,throwable, MessageFormat.format(msgPattern, msgArgs));
  }

  @Override
  public ExceptionType type() {
    return ExceptionType.PARAM;
  }
}
