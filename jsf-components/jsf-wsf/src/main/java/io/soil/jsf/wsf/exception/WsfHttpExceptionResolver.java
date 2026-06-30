package io.soil.jsf.wsf.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * WAF 全局异常处理器，自动捕获并处理 Web 层异常。
 * <p>
 * 使用 {@link RestControllerAdvice} 注解，自动注册为全局异常处理器。
 * 支持处理 {@link WsfException} 和所有未捕获的 {@link Throwable} 异常，
 * 统一返回 {@link WsfHttpExceptionResponse} 格式的 JSON 响应。
 * </p>
 *
 * @author wangzezhou
 */
@Slf4j
@RestControllerAdvice
public class WsfHttpExceptionResolver {

  /**
   * 处理 WAF 自定义异常
   *
   * @param e WAF 异常对象
   * @return 包含异常信息的 HTTP 响应，状态码由异常对象决定
   */
  @ExceptionHandler(WsfException.class)
  public ResponseEntity<WsfHttpExceptionResponse> handleWebException(WsfException e ){
    return new ResponseEntity<>(WsfHttpExceptionResponse.createFrom(e),e.status());
  }

  /**
   * 处理所有未捕获的异常
   *
   * @param throwable 原始异常对象
   * @return 包含异常信息的 HTTP 响应，状态码固定为 500
   */
  @ExceptionHandler(Throwable.class)
  public ResponseEntity<WsfHttpExceptionResponse> handleWebException(Throwable throwable){
    return new ResponseEntity<>(WsfHttpExceptionResponse.createFrom(throwable), HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
