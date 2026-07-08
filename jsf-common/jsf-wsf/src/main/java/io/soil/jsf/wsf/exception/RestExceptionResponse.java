package io.soil.jsf.wsf.exception;

import io.soil.jsf.common.exception.BaseException;
import io.soil.jsf.common.exception.ExceptionType;
import lombok.Data;
import org.springframework.http.HttpStatus;

/**
 * WSF 全局统一异常处理，用于封装异常信息返回给客户端。
 *
 * @author zeno
 */
@Data
public class RestExceptionResponse {

    /** 异常类型 */
    private ExceptionType errType;
    /** 异常状态码 */
    private String errCode;
    /** 异常消息 */
    private String errDesc;
    /** 异常栈信息 */
    private String trace;


  /**
   * 根据异常对象创建异常响应 VO
   *
   * @param ex 异常对象
   * @return 异常响应 VO
   */
  public static RestExceptionResponse createFrom(Throwable ex){

    RestExceptionResponse responseVO = new RestExceptionResponse();
    responseVO.setTrace(WebBizException.getStackTraceString(ex));
    responseVO.setErrType(ExceptionType.SYS);
    responseVO.setErrCode(HttpStatus.INTERNAL_SERVER_ERROR.name());
    responseVO.setErrDesc(ex.getMessage());

    return responseVO;
  }

  /**
   * 根据异常对象创建异常响应 VO
   *
   * @param ex 异常对象
   * @return 异常响应 VO
   */
  public static RestExceptionResponse createFrom(WebBizException ex){

    RestExceptionResponse responseVO = new RestExceptionResponse();
    responseVO.setTrace(WebBizException.getStackTraceString(ex));
    responseVO.setErrType(ex.type());
    responseVO.setErrCode(HttpStatus.INTERNAL_SERVER_ERROR.name());
    responseVO.setErrDesc(ex.getMessage());

    return responseVO;
  }




  /**
   * 根据异常对象创建异常响应 VO
   *
   * @param ex 异常对象
   * @return 异常响应 VO
   */
  public static RestExceptionResponse createFrom(BaseException ex){

    RestExceptionResponse responseVO = new RestExceptionResponse();
    responseVO.setTrace(WebBizException.getStackTraceString(ex));
    responseVO.setErrType(ex.type());
    responseVO.setErrCode(ex.code());
    responseVO.setErrDesc(ex.getMessage());

    return responseVO;
  }

}
