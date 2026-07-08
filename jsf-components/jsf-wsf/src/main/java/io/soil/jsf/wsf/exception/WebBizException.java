package io.soil.jsf.wsf.exception;

import io.soil.jsf.common.exception.BaseException;
import io.soil.jsf.common.exception.ExceptionType;
import org.springframework.http.HttpStatus;

import java.util.Collections;

/**
 * Web 业务服务异常基类， Web 服务的业逻辑异常定义从此类派生定义并抛出异常，给全局异常类进行处理 （{@link RestGlobalExceptionResolver}），
 * Web 业务异常类，继承自 {@link BaseException}，用于 Web 服务业务异常抛出。
 * <p>
 * 支持 HTTP 状态码、自定义异常状态码、消息模板等多种构造方式，
 * 适用于 REST API 的异常响应场景。
 * </p>
 *
 * @author zeno
 */
public class WebBizException extends BaseException {

  /** http 状态码 */
  private HttpStatus status;

  /**
   * 使用消息构造 WAF 异常，默认 HTTP 状态码 500
   *
   * @param msg 异常消息
   */
  public WebBizException(String msg){
    this(HttpStatus.INTERNAL_SERVER_ERROR, msg);
  }

  /**
   * 使用消息模板构造 WAF 异常，默认 HTTP 状态码 500
   *
   * @param msgPattern java.text.MessageFormat 消息模板
   * @param msgArgs    消息模板参数
   */
  public WebBizException(String msgPattern, Object... msgArgs){
    this(HttpStatus.INTERNAL_SERVER_ERROR, msgPattern, msgArgs);
  }

  /**
   * 使用 HTTP 状态码和消息构造 WAF 异常
   *
   * @param status HTTP 状态码
   * @param msg    异常消息
   */
  public WebBizException(HttpStatus status, String msg){
    this(status.name(), status,  msg);
  }

  /**
   * 使用 HTTP 状态码和消息模板构造 WAF 异常
   *
   * @param status     HTTP 状态码
   * @param msgPattern java.text.MessageFormat 消息模板
   * @param msgArgs    消息模板参数
   */
  public WebBizException(HttpStatus status, String msgPattern, Object... msgArgs){
    this(status.name(), status, msgPattern, msgArgs);
  }

  /**
   * 使用自定义异常状态码和 HTTP 状态码构造 WAF 异常
   *
   * @param code   自定义异常状态码
   * @param status HTTP 状态码
   * @param msg    异常消息
   */
  public WebBizException(String code, HttpStatus status, String msg){
    this(code, status, msg, Collections.emptyList());
  }

  /**
   * 使用自定义异常状态码、HTTP 状态码和消息模板构造 WAF 异常
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
   * 使用异常栈构造 WAF 异常，默认 HTTP 状态码 500
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
   * 使用自定义异常状态码和异常栈构造 WAF 异常
   *
   * @param code      自定义异常状态码
   * @param throwable 原始异常
   */
  public WebBizException(String code, Throwable throwable){
    this(code, throwable, throwable.getMessage(), Collections.emptyList());
  }

  /**
   * 使用自定义异常状态码、异常栈和消息模板构造 WAF 异常
   *
   * @param code       自定义异常状态码
   * @param throwable  原始异常
   * @param msgPattern java.text.MessageFormat 消息模板
   * @param msgArgs    消息模板参数
   */
  public WebBizException(String code, Throwable throwable, String msgPattern, Object... msgArgs){
    this(HttpStatus.INTERNAL_SERVER_ERROR, code, throwable, msgPattern, msgArgs);
  }

  @Override
  public ExceptionType type() {
    return ExceptionType.BIZ;
  }

  /**
   * 全参数构造 WAF 异常
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
