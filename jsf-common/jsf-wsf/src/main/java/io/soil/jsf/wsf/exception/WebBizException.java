package io.soil.jsf.wsf.exception;

import io.soil.jsf.common.exception.BaseException;
import io.soil.jsf.common.exception.BizException;
import io.soil.jsf.common.exception.ExceptionType;
import org.springframework.http.HttpStatus;

import java.util.Collections;

/**
 * Web 业务异常，可指定 HTTP 状态码的业务异常,用于支持 REST API 异常响应场景。
 * <p>
 * 继承自通用业务异常 {@link BizException}（{@link #type()} 固定返回 {@link ExceptionType#BIZ}），
 * 在业务异常基础上额外持有 {@link HttpStatus} 状态码，由全局异常处理器（{@link RestGlobalExceptionResolver}）
 * 按异常对象自带的状态码返回对应的 HTTP 响应。
 * </p>
 * <p>
 * 支持 HTTP 状态码、自定义异常码、消息模板（MessageFormat）等多种构造方式，
 * 适用于 REST API 的异常响应场景。默认 HTTP 状态码为 500（INTERNAL_SERVER_ERROR）。
 * </p>
 *
 * @author zeno
 */
public class WebBizException extends BizException {

  /** http 状态码 */
  private final HttpStatus status;

    /**
     * 将业务异常重新包装为 Web 业务异常并抛出，保留原始异常码和消息，同时指定 HTTP 状态码。
     *
     * @param status HTTP 状态码
     * @param e      原始业务异常
     */
    public static void rethrow(HttpStatus status,BizException e){
      throw new WebBizException(e.code(),status,e,e.getMessage());
    }

  /**
   * 使用 HTTP 状态码和消息构造 Web 业务 异常
   *
   * @param status HTTP 状态码
   * @param msg    异常消息
   */
  public WebBizException(HttpStatus status, String msg){
    this(status.name(), status,  msg);
  }

  /**
   * 使用 HTTP 状态码和消息模板构造 Web 业务 异常
   *
   * @param status     HTTP 状态码
   * @param msgPattern java.text.MessageFormat 消息模板
   * @param msgArgs    消息模板参数
   */
  public WebBizException(HttpStatus status, String msgPattern, Object... msgArgs){
    this(status.name(), status, msgPattern, msgArgs);
  }

  /**
   * 使用自定义异常状态码和 HTTP 状态码构造 Web 业务 异常
   *
   * @param code   自定义异常状态码
   * @param status HTTP 状态码
   * @param msg    异常消息
   */
  public WebBizException(String code, HttpStatus status, String msg){
    this(code, status, msg, Collections.emptyList());
  }

  /**
   * 使用自定义异常状态码、HTTP 状态码和消息模板构造 Web 业务 异常
   *
   * @param code       自定义异常状态码
   * @param status     HTTP 状态码
   * @param msgPattern java.text.MessageFormat 消息模板
   * @param msgArgs    消息模板参数
   */
  public WebBizException(String code, HttpStatus status, String msgPattern, Object... msgArgs){
    this(code, status,  null, msgPattern, msgArgs);
  }

  /**
   * 使用异常栈构造 Web 业务 异常，默认 HTTP 状态码 500
   *
   * @param throwable 原始异常
   */
  public WebBizException(Throwable throwable){
    this(
      HttpStatus.INTERNAL_SERVER_ERROR.name(),
      HttpStatus.INTERNAL_SERVER_ERROR,
         throwable,
         throwable.getMessage(),
         Collections.emptyList());
  }

  /**
   * 使用自定义异常状态码和异常栈构造 Web 业务 异常
   *
   * @param code      自定义异常状态码
   * @param throwable 原始异常
   */
  public WebBizException(String code, Throwable throwable){
    this(code, throwable, throwable.getMessage(), Collections.emptyList());
  }

  /**
   * 使用自定义异常状态码、异常栈和消息模板构造 Web 业务 异常
   *
   * @param code       自定义异常状态码
   * @param throwable  原始异常
   * @param msgPattern java.text.MessageFormat 消息模板
   * @param msgArgs    消息模板参数
   */
  public WebBizException(String code, Throwable throwable, String msgPattern, Object... msgArgs){
    this(HttpStatus.INTERNAL_SERVER_ERROR, code, throwable, msgPattern, msgArgs);
  }

  /**
   * 全参数构造 Web 业务 异常
   *
   * @param code       自定义异常状态码
   * @param status     HTTP 状态码
   * @param throwable  原始异常
   * @param msgPattern java.text.MessageFormat 消息模板
   * @param msgArgs    消息模板参数
   */
  public WebBizException(String code, HttpStatus status, Throwable throwable, String msgPattern, Object... msgArgs){
    super(code, throwable, msgPattern, msgArgs);
    this.status = status;
  }


  /**
   * 获取 HTTP 状态码
   *
   * @return HTTP 状态码
   */
  public HttpStatus status(){
    return status;
  }
}
