package io.soil.jsf.wsf.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class WsfExceptionResponseDTOTest {

    @Test
    void createFrom_withWsfException_shouldPopulateFields() {
        WsfException ex = new WsfException("ERR-001", HttpStatus.BAD_REQUEST, "bad request");
        WsfHttpExceptionResponse vo = WsfHttpExceptionResponse.createFrom(ex);

        assertEquals("bad request", vo.getErrDesc());
        assertEquals("UNDEFINED", vo.getModule());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.name(), vo.getErrCode());
    }

    @Test
    void createFrom_withRuntimeException_shouldPopulateFields() {
        RuntimeException ex = new RuntimeException("something went wrong");
        WsfHttpExceptionResponse vo = WsfHttpExceptionResponse.createFrom(ex);

        assertEquals("something went wrong", vo.getErrDesc());
        assertEquals("UNDEFINED", vo.getModule());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.name(), vo.getErrCode());
    }

    @Test
    void createFrom_withNullMessage_shouldHandleGracefully() {
        RuntimeException ex = new RuntimeException((String) null);
        WsfHttpExceptionResponse vo = WsfHttpExceptionResponse.createFrom(ex);

        assertNull(vo.getErrDesc());
    }

    @Test
    void setters_shouldSetFieldsCorrectly() {
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
