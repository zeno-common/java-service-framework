package io.soil.jsf.common.exception;

/**
 * 参数校验异常，用于 adapter layer 层请求入参不合法（如非空、格式校验失败等）。在 Adapter 层进行参数校验时抛出。
 * <p>
 * 继承自 {@link BaseException}，{@link #type()} 固定返回 {@link ExceptionType#PARAM}，
 * 由 Adapter 层抛出，由全局异常处理器统一捕获转换为标准错误响应。
 * <p>
 * 推荐通过 {@link #of(Error, Object...)} 静态工厂方法创建实例，配合实现 {@link Error} 接口的枚举使用：
 * <pre>{@code
 *   throw ParamException.of(ParamError.INVALID, paramName);
 * }</pre>
 *
 * @author zeno
 * @see Error
 */
public final class ParamException extends BaseException {

  /**
   * 根据错误定义和描述参数构造参数校验异常（无原始原因）
   *
   * @param error    错误定义（提供 code 和 desc 模板）
   * @param descArgs 描述模板参数（MessageFormat 格式化）
   * @return 参数校验异常实例
   */
  public static ParamException of(Error error, Object... descArgs) {
    return of(null, error, descArgs);
  }

  /**
   * 根据错误定义、原始原因和描述参数构造参数校验异常
   *
   * @param cause    原始异常原因
   * @param error    错误定义（提供 code 和 desc 模板）
   * @param descArgs 描述模板参数（MessageFormat 格式化）
   * @return 参数校验异常实例
   */
  public static ParamException of(Throwable cause, Error error, Object... descArgs) {
    return new ParamException(error.code(), cause, error.desc(), descArgs);
  }

  private ParamException(String code, Throwable throwable, String msgPattern, Object... msgArgs) {
    super(code, throwable, msgPattern, msgArgs);
  }

  @Override
  public ExceptionType type() {
    return ExceptionType.PARAM;
  }
}
