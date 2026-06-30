package io.soil.jsf.wsf.exception;

import org.junit.Test;
import org.springframework.http.HttpStatus;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * {@link WsfHttpExceptionResponse} 单元测试
 */
public class WsfExceptionResponseDTOTest {

    @Test
    public void createFrom_withWafException_shouldPopulateFields() {
        WsfException ex = new WsfException("ERR-001", HttpStatus.BAD_REQUEST, "bad request");
        WsfHttpExceptionResponse vo = WsfHttpExceptionResponse.createFrom(ex);

        assertEquals("bad request", vo.getErrDesc());
        // module 默认为 UNDEFINED（createFrom 不区分异常类型）
        assertEquals("UNDEFINED", vo.getModule());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.name(), vo.getErrCode());
    }

    @Test
    public void createFrom_withRuntimeException_shouldPopulateFields() {
        RuntimeException ex = new RuntimeException("something went wrong");
        WsfHttpExceptionResponse vo = WsfHttpExceptionResponse.createFrom(ex);

        assertEquals("something went wrong", vo.getErrDesc());
        assertEquals("UNDEFINED", vo.getModule());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.name(), vo.getErrCode());
    }

    @Test
    public void createFrom_withNullMessage_shouldHandleGracefully() {
        RuntimeException ex = new RuntimeException((String) null);
        WsfHttpExceptionResponse vo = WsfHttpExceptionResponse.createFrom(ex);

        assertNull(vo.getErrDesc());
    }

    @Test
    public void setters_shouldSetFieldsCorrectly() {
        WsfHttpExceptionResponse vo = new WsfHttpExceptionResponse();

        vo.setModule("TEST");
        vo.setErrCode("ERR-001");
        vo.setErrDesc("error");
        vo.setTrace("stack trace");

        assertEquals("TEST", vo.getModule());
        assertEquals("ERR-001", vo.getErrCode());
        assertEquals("error", vo.getErrDesc());
        assertEquals("stack trace", vo.getTrace());
    }
}
