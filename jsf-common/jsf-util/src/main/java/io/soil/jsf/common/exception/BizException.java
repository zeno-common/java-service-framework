package io.soil.jsf.common.exception;

/**
 * 业务异常，用于 domain/app layer 层违反业务规则（如前置条件不满足、状态机流转非法）。
 * <p>
 * 继承自 {@link BaseException}，{@link #type()} 固定返回 {@link ExceptionType#BIZ}，
 * 由 Domain / App 层抛出，由全局异常处理器统一捕获转换为标准错误响应。
 * <p>
 * 推荐通过 {@link #of(Error, Object...)} 静态工厂方法创建实例，配合实现 {@link Error} 接口的枚举使用：
 * <pre>{@code
 *   throw BizException.of(OrderError.NOT_FOUND, orderId);
 * }</pre>
 *
 * @author zeno
 * @see Error
 */
public class BizException extends BaseException {

  /**
   * 根据错误定义和描述参数构造业务异常（无原始原因）
   *
   * @param error    错误定义（提供 code 和 desc 模板）
   * @param descArgs 描述模板参数（MessageFormat 格式化）
   * @return 业务异常实例
   */
  public static BizException of(Error error, Object... descArgs) {
    return of(null, error, descArgs);
  }

  /**
   * 根据错误定义、原始原因和描述参数构造业务异常
   *
   * @param cause    原始异常原因
   * @param error    错误定义（提供 code 和 desc 模板）
   * @param descArgs 描述模板参数（MessageFormat 格式化）
   * @return 业务异常实例
   */
  public static BizException of(Throwable cause, Error error, Object... descArgs) {
    return new BizException(error.code(), cause, error.desc(), descArgs);
  }

  protected BizException(String code, Throwable throwable, String msgPattern, Object... msgArgs) {
    super(code, throwable, msgPattern, msgArgs);
  }

  @Override
  public ExceptionType type() {
    return ExceptionType.BIZ;
  }
}
