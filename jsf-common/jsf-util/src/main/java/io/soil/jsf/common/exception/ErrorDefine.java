package io.soil.jsf.common.exception;

/**
 * 错误定义接口，用于以枚举方式统一声明异常码与异常消息。
 * <p>
 * 实现此接口的枚举可直接作为 {@link BizException#of(ErrorDefine)}、
 * {@link ParamException#of(ErrorDefine)}、{@link SysException#of(ErrorDefine)} 的参数，
 * 避免在业务代码中硬编码异常码和消息字符串。
 * </p>
 *
 * <pre>{@code
 * enum OrderError implements ErrorDefine {
 *     NOT_FOUND("ORDER-NOT-FOUND", "订单 {0} 不存在"),
 *     EXISTED("ORDER-EXISTED", "订单已存在");
 *
 *     private final String code;
 *     private final String msg;
 *
 *     OrderError(String code, String msg) { this.code = code; this.msg = msg; }
 *     public String code() { return code; }
 *     public String msg() { return msg; }
 * }
 *
 * throw BizException.of(OrderError.NOT_FOUND, orderId);
 * }</pre>
 *
 * @author zeno.w
 * @see BizException#of(ErrorDefine)
 * @see ParamException#of(ErrorDefine)
 * @see SysException#of(ErrorDefine)
 */
public interface ErrorDefine {

  /**
   * 获取异常码
   *
   * @return 异常码，建议格式为 {@code [业务]-[编码]}，例如 {@code ORDER-NOT-FOUND}
   */
  String code();

  /**
   * 获取异常消息，支持 {@link java.text.MessageFormat} 模板语法
   *
   * @return 异常消息模板
   */
  String msg();
}
