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

  protected ParamException(String msg) {
    this( msg, Collections.emptyList());
  }

  protected ParamException(String msgPattern, Object... msgArgs) {
    this(null, msgPattern, msgArgs);
  }

  protected ParamException(Throwable throwable, String msg) {
    this(throwable, msg, Collections.emptyList());
  }

  protected ParamException(Throwable throwable) {
    this(throwable, throwable.getMessage(), Collections.emptyList());
  }

  protected ParamException(Throwable throwable, String msgPattern, Object... msgArgs) {
    this("UNDEFINED", throwable, msgPattern, msgArgs);
  }

  protected ParamException(String code, String msg) {
    this( code, (String)null, msg, Collections.emptyList());
  }

  protected ParamException(String code, Throwable throwable, String msg) {
    this(code, throwable, msg, Collections.emptyList());
  }

  protected ParamException(String code, Throwable throwable, String msgPattern, Object... msgArgs) {
    super(code, throwable, MessageFormat.format(msgPattern, msgArgs));
  }


  @Override
  public ExceptionType type() {
    return ExceptionType.PARAM;
  }
}
