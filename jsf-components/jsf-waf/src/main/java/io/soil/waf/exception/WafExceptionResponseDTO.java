package io.soil.waf.exception;

import lombok.Data;
import org.springframework.http.HttpStatus;

/**
 * WAF 异常响应内容 VO，用于封装异常信息返回给客户端。
 *
 * @author zeno
 */
@Data
public class WafExceptionResponseDTO {

    /** 异常模块名称 */
    private String module;
    /** 异常状态码 */
    private String code;
    /** 异常消息 */
    private String msg;
    /** 异常栈信息 */
    private String trace;


    /**
     * 根据异常对象创建异常响应 VO
     *
     * @param ex 异常对象
     * @return 异常响应 VO
     */
    public static WafExceptionResponseDTO createFrom(Throwable ex){

        WafExceptionResponseDTO responseVO = new WafExceptionResponseDTO();
        responseVO.setTrace(WafException.getStackTraceString(ex));
        responseVO.setModule("UNDEFINED");
        responseVO.setCode(HttpStatus.INTERNAL_SERVER_ERROR.name());
        responseVO.setMsg(ex.getMessage());

        return responseVO;
    }

}
