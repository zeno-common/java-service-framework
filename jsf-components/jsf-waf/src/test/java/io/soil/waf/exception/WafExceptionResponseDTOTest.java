package io.soil.waf.exception;

import org.junit.Test;
import org.springframework.http.HttpStatus;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * {@link WafExceptionResponseDTO} 单元测试
 */
public class WafExceptionResponseDTOTest {

    @Test
    public void createFrom_withWafException_shouldPopulateFields() {
        WafException ex = new WafException("ERR-001", HttpStatus.BAD_REQUEST, "bad request");
        WafExceptionResponseDTO vo = WafExceptionResponseDTO.createFrom(ex);

        assertEquals("bad request", vo.getMsg());
        // module 默认为 UNDEFINED（createFrom 不区分异常类型）
        assertEquals("UNDEFINED", vo.getModule());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.name(), vo.getCode());
    }

    @Test
    public void createFrom_withRuntimeException_shouldPopulateFields() {
        RuntimeException ex = new RuntimeException("something went wrong");
        WafExceptionResponseDTO vo = WafExceptionResponseDTO.createFrom(ex);

        assertEquals("something went wrong", vo.getMsg());
        assertEquals("UNDEFINED", vo.getModule());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.name(), vo.getCode());
    }

    @Test
    public void createFrom_withNullMessage_shouldHandleGracefully() {
        RuntimeException ex = new RuntimeException((String) null);
        WafExceptionResponseDTO vo = WafExceptionResponseDTO.createFrom(ex);

        assertNull(vo.getMsg());
    }

    @Test
    public void setters_shouldSetFieldsCorrectly() {
        WafExceptionResponseDTO vo = new WafExceptionResponseDTO();

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
