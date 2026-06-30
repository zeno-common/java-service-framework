package io.soil.jsf.wsf.exception;

import lombok.Data;
import org.springframework.http.HttpStatus;

/**
 * WAF 异常响应内容，用于封装异常信息返回给客户端。
 *
 * @author zeno
 */
@Data
public class WsfHttpExceptionResponse {

    /** 异常模块名称 */
    private String module;
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
    public static WsfHttpExceptionResponse createFrom(Throwable ex){

        WsfHttpExceptionResponse responseVO = new WsfHttpExceptionResponse();
        responseVO.setTrace(WsfException.getStackTraceString(ex));
        responseVO.setModule("UNDEFINED");
        responseVO.setErrCode(HttpStatus.INTERNAL_SERVER_ERROR.name());
        responseVO.setErrDesc(ex.getMessage());

        return responseVO;
    }

}
