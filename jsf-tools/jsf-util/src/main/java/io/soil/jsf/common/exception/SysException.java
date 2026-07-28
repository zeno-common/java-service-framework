package io.soil.jsf.common.exception;

/**
 * 系统异常，用于 infrastructure layer 捕获并包装的技术异常（如数据库访问失败、外部服务调用异常）。
 * <p>
 * 继承自 {@link BaseException}，{@link #type()} 固定返回 {@link ExceptionType#SYS}，
 * 由 Infrastructure 层抛出，由全局异常处理器统一捕获转换为标准错误响应，且不向前端暴露底层技术细节。
 * <p>
 * 推荐通过 {@link #of(Error, Object...)} 静态工厂方法创建实例，配合实现 {@link Error} 接口的枚举使用：
 * <pre>{@code
 *   throw SysException.of(DbError.SAVE_FAILED, e);
 * }</pre>
 *
 * @author zeno
 * @see Error
 */
public final class SysException extends BaseException {

  /**
   * 根据错误定义和描述参数构造系统异常（无原始原因）
   *
   * @param error    错误定义（提供 code 和 desc 模板）
   * @param descArgs 描述模板参数（MessageFormat 格式化）
   * @return 系统异常实例
   */
  public static SysException of(Error error, Object... descArgs) {
    return of(null, error, descArgs);
  }

  /**
   * 根据错误定义、原始原因和描述参数构造系统异常
   *
   * @param cause    原始异常原因
   * @param error    错误定义（提供 code 和 desc 模板）
   * @param descArgs 描述模板参数（MessageFormat 格式化）
   * @return 系统异常实例
   */
  public static SysException of(Throwable cause, Error error, Object... descArgs) {
    return new SysException(error.code(), cause, error.desc(), descArgs);
  }

  private SysException(String code, Throwable throwable, String msgPattern, Object... msgArgs) {
    super(code, throwable, msgPattern, msgArgs);
  }

  @Override
  public ExceptionType type() {
    return ExceptionType.SYS;
  }
}
