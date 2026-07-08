package io.soil.jsf.common.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * {@link UnknownEnumException} 单元测试
 */
public class UnknownEnumExceptionTest {

    enum TestStatus {
        ACTIVE, INACTIVE
    }

    @Test
    public void constructor_shouldCreateExceptionWithCorrectMessage() {
        UnknownEnumException ex = new UnknownEnumException("INVALID", TestStatus.class);

        assertTrue(ex.getMessage().contains(TestStatus.class.getName()));
        assertTrue(ex.getMessage().contains("INVALID"));
    }

    @Test
    public void type_shouldReturnJSF() {
        UnknownEnumException ex = new UnknownEnumException(999, TestStatus.class);

        assertEquals("JSF", ex.type());
    }

    @Test
    public void constructor_withNumericStatus_shouldWork() {
        UnknownEnumException ex = new UnknownEnumException(999, TestStatus.class);

        assertTrue(ex.getMessage().contains("999"));
    }

    @Test
    public void constructor_withNullStatus_shouldNotThrow() {
        UnknownEnumException ex = new UnknownEnumException(null, TestStatus.class);

        assertNotNull(ex.getMessage());
    }
}
