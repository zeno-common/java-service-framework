package io.soil.waf.exception;

import lombok.Data;
import org.springframework.http.HttpStatus;

/**
 * 异常响应内容
 *
 * @author zeno
 */
@Data
public class WafExceptionResponseVO {

    private String module;
    private String code;
    private String msg;
    private String trace;


    public static WafExceptionResponseVO createFrom(Throwable ex){

        WafExceptionResponseVO responseVO = new WafExceptionResponseVO();
        responseVO.setTrace(WafException.getStackTraceString(ex));
        responseVO.setModule("UNDEFINED");
        responseVO.setCode(HttpStatus.INTERNAL_SERVER_ERROR.name());
        responseVO.setMsg(ex.getMessage());

        return responseVO;
    }

}
