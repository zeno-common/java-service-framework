package io.soil.waf.exception;

import java.util.Collections;

import io.soil.common.exception.BaseException;
import org.springframework.http.HttpStatus;

/**
 *
 */
public class WafException extends BaseException {

  /** http 状态码 */
  private HttpStatus status;

  /** 异常状态码 */
  private String code;


  public WafException(String msg){
    this(HttpStatus.INTERNAL_SERVER_ERROR, msg);
  }

  public WafException(String msgPattern, Object... msgArgs){
    this(HttpStatus.INTERNAL_SERVER_ERROR, msgPattern, msgArgs);
  }

  /***
   *
   * @param status      http 状态码
   * @param msg         异常消息
   */
  public WafException(HttpStatus status, String msg){
    this(status.name(), status,  msg);
  }

  /***
   *
   * @param status      http 状态码
   * @param msgPattern  java.text.MessageFormat 消息模板
   * @param msgArgs     java.text.MessageFormat 消息模板参数
   */
  public WafException(HttpStatus status, String msgPattern, Object... msgArgs){
    this(status.name(), status, msgPattern, msgArgs);
  }

  /***
   *
   * @param code        异常状态码
   * @param status      http 状态码
   */
  public WafException(String code, HttpStatus status, String msg){
    this(code, status, msg, Collections.emptyList());
  }

  /***
   *
   * @param code        异常状态码
   * @param status      http 状态码
   * @param msgPattern  java.text.MessageFormat 消息模板
   * @param msgArgs     java.text.MessageFormat 消息模板参数
   */
  public WafException(String code, HttpStatus status, String msgPattern, Object... msgArgs){
    this(code, status,  null, msgPattern, msgArgs);
  }

  /***
   *
   * @param throwable   异常栈
   */
  public WafException(Throwable throwable){
    this(
      HttpStatus.INTERNAL_SERVER_ERROR.name(),
      HttpStatus.INTERNAL_SERVER_ERROR,
         throwable,
         throwable.getMessage(),
         Collections.emptyList());
  }

  /***
   *
   * @param code        异常状态码
   * @param throwable   异常栈
   */
  public WafException(String code, Throwable throwable){
    this(code, throwable, throwable.getMessage(), Collections.emptyList());
  }

  /***
   *
   * @param code        异常状态码
   * @param throwable   异常栈
   * @param msgPattern  java.text.MessageFormat 消息模板
   * @param msgArgs     java.text.MessageFormat 消息模板参数
   */
  public WafException(String code, Throwable throwable, String msgPattern, Object... msgArgs){
    this(HttpStatus.INTERNAL_SERVER_ERROR, code, throwable, msgPattern, msgArgs);
  }

  /***
   *
   * @param status      http 状态码
   * @param code        异常状态码
   * @param throwable   异常栈
   * @param msgPattern  java.text.MessageFormat 消息模板
   * @param msgArgs     java.text.MessageFormat 消息模板参数
   */
  public WafException(String code, HttpStatus status,  Throwable throwable, String msgPattern, Object... msgArgs){
    super(throwable, msgPattern, msgArgs);
    this.status = status;
    this.code = code;
  }

  @Override
  public String module(){
    return "GAEA-WAF";
  }

  public HttpStatus status(){
    return status;
  }

  public String code(){
    return code;
  }
}
