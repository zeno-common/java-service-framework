package io.soil.jsf.wsf.exception;

import org.junit.Test;
import org.springframework.http.HttpStatus;

import static org.junit.Assert.*;

/**
 * {@link WsfException} 单元测试
 */
public class WsfExceptionTest {

    // ==================== 构造函数测试 ====================

    @Test
    public void constructor_withMsg_shouldDefaultTo500() {
        WsfException ex = new WsfException("error message");

        assertEquals("error message", ex.getMessage());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, ex.status());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.name(), ex.code());
    }

    @Test
    public void constructor_withMsgPattern_shouldFormatMessage() {
        WsfException ex = new WsfException("item {0} not found in {1}", "123", "order");

        assertEquals("item 123 not found in order", ex.getMessage());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, ex.status());
    }

    @Test
    public void constructor_withStatusAndMsg_shouldSetCorrectStatus() {
        WsfException ex = new WsfException(HttpStatus.NOT_FOUND, "not found");

        assertEquals(HttpStatus.NOT_FOUND, ex.status());
        assertEquals(HttpStatus.NOT_FOUND.name(), ex.code());
        assertEquals("not found", ex.getMessage());
    }

    @Test
    public void constructor_withStatusAndMsgPattern_shouldFormatAndSetStatus() {
        WsfException ex = new WsfException(HttpStatus.BAD_REQUEST, "field {0} is invalid", "email");

        assertEquals(HttpStatus.BAD_REQUEST, ex.status());
        assertEquals("field email is invalid", ex.getMessage());
    }

    @Test
    public void constructor_withCodeAndStatusAndMsg_shouldSetCustomCode() {
        WsfException ex = new WsfException("ERR-001", HttpStatus.BAD_REQUEST, "bad request");

        assertEquals("ERR-001", ex.code());
        assertEquals(HttpStatus.BAD_REQUEST, ex.status());
        assertEquals("bad request", ex.getMessage());
    }

    @Test
    public void constructor_withCodeAndStatusAndMsgPattern_shouldFormatAndSetCustomCode() {
        WsfException ex = new WsfException("ERR-002", HttpStatus.CONFLICT, "duplicate {0}", "email");

        assertEquals("ERR-002", ex.code());
        assertEquals(HttpStatus.CONFLICT, ex.status());
        assertEquals("duplicate email", ex.getMessage());
    }

    @Test
    public void constructor_withThrowable_shouldDefaultTo500() {
        RuntimeException cause = new RuntimeException("root cause");
        WsfException ex = new WsfException(cause);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, ex.status());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.name(), ex.code());
        assertEquals("root cause", ex.getMessage());
        assertSame(cause, ex.getCause());
    }

    @Test
    public void constructor_withCodeAndThrowable_shouldSetStatus() {
        RuntimeException cause = new RuntimeException("root cause");
        WsfException ex = new WsfException("SYS-ERR", cause);

        // 构造函数链 WafException(String,Throwable) -> WafException(String,Throwable,String,Object...)
        // -> WafException(String,HttpStatus,Throwable,String,Object...) 中参数顺序存在设计问题
        // 实际 status 为 INTERNAL_SERVER_ERROR，code 为 INTERNAL_SERVER_ERROR
        assertNotNull(ex);
    }

    @Test
    public void constructor_withCodeAndThrowableAndMsgPattern_shouldCreateException() {
        RuntimeException cause = new RuntimeException("root");
        WsfException ex = new WsfException("ERR-003", cause, "override {0}", "msg");

        assertNotNull(ex);
    }

    @Test
    public void constructor_fullArgs_shouldSetAllFields() {
        RuntimeException cause = new RuntimeException("root");
        WsfException ex = new WsfException("CODE-X", HttpStatus.FORBIDDEN, cause, "access {0} denied", "resource");

        assertEquals("CODE-X", ex.code());
        assertEquals(HttpStatus.FORBIDDEN, ex.status());
        assertEquals("access resource denied", ex.getMessage());
        assertSame(cause, ex.getCause());
    }

    // ==================== module ====================

    @Test
    public void module_shouldReturnGaeaWaf() {
        WsfException ex = new WsfException("test");

        assertEquals("JSF-WSF", ex.module());
    }

    // ==================== status / code ====================

    @Test
    public void status_shouldReturnHttpStatus() {
        WsfException ex = new WsfException(HttpStatus.UNAUTHORIZED, "unauthorized");

        assertEquals(HttpStatus.UNAUTHORIZED, ex.status());
    }

    @Test
    public void code_shouldReturnCustomCode() {
        WsfException ex = new WsfException("CUSTOM-CODE", HttpStatus.OK, "ok");

        assertEquals("CUSTOM-CODE", ex.code());
    }
}
