package io.soil.jsf.common.exception;

/**
 * 错误定义接口，用于以类型安全的方式定义错误码与错误描述。
 * <p>
 * 实现此接口的枚举类可作为 {@link BizException#of(Error, Object...)} 等静态工厂方法的参数，
 * 避免在业务代码中硬编码错误码和描述字符串。
 * <p>
 * 典型用法：
 * <pre>{@code
 *   public enum OrderError implements Error {
 *     NOT_FOUND("ORDER-NOT-FOUND", "订单 {0} 不存在"),
 *     EXISTED("ORDER-EXISTED", "订单已存在");
 *
 *     private final String code;
 *     private final String desc;
 *     OrderError(String code, String desc) { this.code = code; this.desc = desc; }
 *     @Override public String code() { return code; }
 *     @Override public String desc() { return desc; }
 *   }
 *
 *   throw BizException.of(OrderError.NOT_FOUND, orderId);
 * }</pre>
 *
 * @author zeno
 * @see BizException#of(Error, Object...)
 * @see SysException#of(Error, Object...)
 * @see ParamException#of(Error, Object...)
 */
public interface Error {
  /** 错误码 */
  String code();

  /** 错误描述（支持 MessageFormat 模板语法） */
  String desc();
}