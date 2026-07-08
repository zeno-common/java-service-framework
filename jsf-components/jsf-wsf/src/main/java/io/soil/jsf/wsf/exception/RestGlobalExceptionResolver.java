package io.soil.jsf.wsf.exception;

import io.soil.jsf.common.exception.BaseException;
import io.soil.jsf.common.exception.ExceptionType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

/**
 * WAF 全局异常处理器，自动捕获并处理 Web 层异常。
 * <p>
 * 使用 {@link RestControllerAdvice} 注解，自动注册为全局异常处理器。
 * 支持处理 {@link WebBizException}、{@link MethodArgumentNotValidException} 和所有未捕获的 {@link Throwable} 异常，
 * 统一返回 {@link RestExceptionResponse} 格式的 JSON 响应。
 * </p>
 *
 * @author wangzezhou
 */
@Slf4j
@RestControllerAdvice
public class RestGlobalExceptionResolver {

  /**
   * 处理 WAF 自定义异常
   *
   * @param e WAF 异常对象
   * @return 包含异常信息的 HTTP 响应，状态码由异常对象决定
   */
  @ExceptionHandler(WebBizException.class)
  public ResponseEntity<RestExceptionResponse> handleWebException(WebBizException e ){
    return new ResponseEntity<>(RestExceptionResponse.createFrom(e),e.status());
  }

  /**
   * 处理 WAF 自定义异常
   *
   * @param e WAF 异常对象
   * @return 包含异常信息的 HTTP 响应，状态码由异常对象决定
   */
  @ExceptionHandler(WebBizException.class)
  public ResponseEntity<RestExceptionResponse> handleWebException(BaseException e ){
    return new ResponseEntity<>(RestExceptionResponse.createFrom(e),HttpStatus.INTERNAL_SERVER_ERROR);
  }

  /**
   * 处理参数校验异常（@Valid / @Validated 校验失败时抛出）
   *
   * @param e 参数校验异常对象
   * @return 包含字段级错误信息的 HTTP 响应，状态码固定为 400
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<RestExceptionResponse> handleValidationException(MethodArgumentNotValidException e){
    String fieldErrors = e.getBindingResult().getFieldErrors().stream()
        .map(FieldError::getDefaultMessage)
        .collect(Collectors.joining("; "));

    RestExceptionResponse response = new RestExceptionResponse();
    response.setErrType(ExceptionType.CLIENT);
    response.setErrCode(HttpStatus.BAD_REQUEST.name());
    response.setErrDesc(fieldErrors);
    response.setTrace(WebBizException.getStackTraceString(e));

    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
  }

  /**
   * 处理所有未捕获的异常
   *
   * @param throwable 原始异常对象
   * @return 包含异常信息的 HTTP 响应，状态码固定为 500
   */
  @ExceptionHandler(Throwable.class)
  public ResponseEntity<RestExceptionResponse> handleWebException(Throwable throwable){
    return new ResponseEntity<>(RestExceptionResponse.createFrom(throwable), HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
