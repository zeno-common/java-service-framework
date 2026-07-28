package io.soil.jsf.wsf.exception;

import io.soil.jsf.common.exception.BizException;
import io.soil.jsf.common.exception.Error;
import io.soil.jsf.common.exception.ExceptionType;
import org.springframework.http.HttpStatus;

import java.util.Collections;

/**
 * Web 业务异常，可指定 HTTP 状态码的业务异常，用于 application layer 层的异常响应场景。本类为 final，不可被继承。
 * <p>
 * 继承自通用业务异常 {@link BizException}（{@link #type()} 固定返回 {@link ExceptionType#BIZ}），
 * 在业务异常基础上额外持有 {@link HttpStatus} 状态码，由全局异常处理器（{@link RestGlobalExceptionResolver}）
 * 按异常对象自带的状态码返回对应的 HTTP 响应。默认 HTTP 状态码为 500（INTERNAL_SERVER_ERROR）。
 * </p>
 * <p>
 * 推荐通过静态工厂方法构造，避免硬编码错误码与描述：
 * <ul>
 *   <li>{@link #of(HttpStatus, Error, Object...)} — 基于 {@link Error} 错误码枚举构造，并指定 HTTP 状态码</li>
 *   <li>{@link #of(HttpStatus, BizException)} — 将已有 {@link BizException} 重新包装为 WebBizException，保留原始异常码与消息，仅补充 HTTP 状态码</li>
 * </ul>
 * 其中 {@link Error} 是以类型安全方式定义错误码与描述的接口（如各领域的 {@code XxxError} 枚举），
 * 其 {@code code()} / {@code desc()} 分别作为异常码与消息模板（支持 MessageFormat 占位符）。
 * </p>
 *
 * @author zeno
 */
public final class WebBizException extends BizException {

  /**
   * http 状态码
   */
  private final HttpStatus status;
  public static WebBizException of(HttpStatus status,Error error, Object... descArgs) { return of(status, null, error, descArgs); }
  public static WebBizException of(HttpStatus status,Throwable cause, Error error, Object... descArgs) { return new WebBizException(status, error.code(),  cause, error.desc(),descArgs); }

  /**
   * 将 BizException 重新包装为 WebBizException 对象，保留原始异常码和消息，同时指定 HTTP 状态码。
   *
   * @param status HTTP 状态码
   * @param e      原始业务异常
   */
  public static WebBizException of(HttpStatus status, BizException e) {
    return new WebBizException(status, e.code(),  e, e.getMessage());
  }


  /**
   * 指定 HTTP 状态码和自定义异常状态码、异常栈和消息内容构造 Web 业务 异常
   *
   * @param status     HTTP 状态码
   * @param code       自定义异常状态码
   * @param msg        消息内容
   */
  public WebBizException(HttpStatus status, String code, String msg) {
    this(status, code, msg, Collections.emptyList());
  }

  /**
   * 指定 HTTP 状态码和自定义异常状态码、异常栈和消息内容构造 Web 业务 异常
   *
   * @param status     HTTP 状态码
   * @param code       自定义异常状态码
   * @param msgPattern java.text.MessageFormat 消息模板
   * @param msgArgs    消息模板参数
   */
  public WebBizException(HttpStatus status, String code, String msgPattern, Object... msgArgs) {
    this(status, code, null, msgPattern, msgArgs);
  }

  /**
   * 指定 HTTP 状态码和自定义异常状态码、异常栈和消息内容构造 Web 业务 异常
   *
   * @param status     HTTP 状态码
   * @param code       自定义异常状态码
   * @param throwable  原始异常
   */
  public WebBizException(HttpStatus status, String code, Throwable throwable) {
    this(status, code, throwable, throwable.getMessage(), Collections.emptyList());
  }

  /**
   * 指定 HTTP 状态码和自定义异常状态码、异常栈和消息内容构造 Web 业务 异常
   *
   * @param status     HTTP 状态码
   * @param code       自定义异常状态码
   * @param throwable  原始异常
   * @param msg        消息内容
   */
  public WebBizException(HttpStatus status, String code, Throwable throwable, String msg) {
    this(status, code, throwable, msg, Collections.emptyList());
  }

  /**
   * 全参数构造 Web 业务 异常
   *
   * @param status     HTTP 状态码
   * @param code       自定义异常状态码
   * @param throwable  原始异常
   * @param msgPattern java.text.MessageFormat 消息模板
   * @param msgArgs    消息模板参数
   */
  public WebBizException(HttpStatus status, String code, Throwable throwable, String msgPattern, Object... msgArgs) {
    super(code, throwable, msgPattern, msgArgs);
    this.status = status;
  }


  /**
   * 获取 HTTP 状态码
   *
   * @return HTTP 状态码
   */
  public HttpStatus status() {
    return status;
  }
}
