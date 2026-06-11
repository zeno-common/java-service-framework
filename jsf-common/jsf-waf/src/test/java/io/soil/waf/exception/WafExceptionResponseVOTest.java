package io.soil.waf.exception;

import org.junit.Test;
import org.springframework.http.HttpStatus;

import static org.junit.Assert.*;

/**
 * {@link WafExceptionResponseVO} 单元测试
 */
public class WafExceptionResponseVOTest {

    @Test
    public void createFrom_withWafException_shouldPopulateFields() {
        WafException ex = new WafException("ERR-001", HttpStatus.BAD_REQUEST, "bad request");
        WafExceptionResponseVO vo = WafExceptionResponseVO.createFrom(ex);

        assertEquals("bad request", vo.getMsg());
        // module 默认为 UNDEFINED（createFrom 不区分异常类型）
        assertEquals("UNDEFINED", vo.getModule());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.name(), vo.getCode());
    }

    @Test
    public void createFrom_withRuntimeException_shouldPopulateFields() {
        RuntimeException ex = new RuntimeException("something went wrong");
        WafExceptionResponseVO vo = WafExceptionResponseVO.createFrom(ex);

        assertEquals("something went wrong", vo.getMsg());
        assertEquals("UNDEFINED", vo.getModule());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.name(), vo.getCode());
    }

    @Test
    public void createFrom_withNullMessage_shouldHandleGracefully() {
        RuntimeException ex = new RuntimeException((String) null);
        WafExceptionResponseVO vo = WafExceptionResponseVO.createFrom(ex);

        assertNull(vo.getMsg());
    }

    @Test
    public void setters_shouldSetFieldsCorrectly() {
        WafExceptionResponseVO vo = new WafExceptionResponseVO();

        vo.setModule("TEST");
        vo.setCode("ERR-001");
        vo.setMsg("error");
        vo.setTrace("stack trace");

        assertEquals("TEST", vo.getModule());
        assertEquals("ERR-001", vo.getCode());
        assertEquals("error", vo.getMsg());
        assertEquals("stack trace", vo.getTrace());
    }
}
