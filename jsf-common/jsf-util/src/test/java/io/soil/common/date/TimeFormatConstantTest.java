package io.soil.common.date;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * {@link TimeFormatConstant} 单元测试
 */
public class TimeFormatConstantTest {
    @Test
    public void dateFormat_shouldMatchPattern() {
        assertEquals("yyyy-MM-dd", TimeFormatConstant.DATE_FORMAT);
    }

    @Test
    public void timeFormat_shouldMatchPattern() {
        assertEquals("HH:mm:ss", TimeFormatConstant.TIME_FORMAT);
    }

    @Test
    public void isoDateTimeFormat_shouldMatchPattern() {
        assertEquals("yyyy-MM-dd'T'HH:mm:ss", TimeFormatConstant.ISO_DATE_TIME_FORMAT);
    }

    @Test
    public void isoDateTimeMillisFormat_shouldMatchPattern() {
        assertEquals("yyyy-MM-dd'T'HH:mm:ss.SSS", TimeFormatConstant.ISO_DATE_TIME_MILLIS_FORMAT);
    }

    @Test
    public void isoDateTimeOffsetFormat_shouldMatchPattern() {
        assertEquals("yyyy-MM-dd'T'HH:mm:ssXXX", TimeFormatConstant.ISO_DATE_TIME_OFFSET_FORMAT);
    }

    @Test
    public void isoDateTimeMillisOffsetFormat_shouldMatchPattern() {
        assertEquals("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", TimeFormatConstant.ISO_DATE_TIME_MILLIS_OFFSET_FORMAT);
    }

    @Test
    public void isoDateTimeFormat_shouldBeParseable() {
        // 验证格式字符串可以被 DateTimeFormatter 正确解析
        assertNotNull(java.time.format.DateTimeFormatter.ofPattern(TimeFormatConstant.ISO_DATE_TIME_FORMAT));
    }

    @Test
    public void isoDateTimeMillisFormat_shouldBeParseable() {
        assertNotNull(java.time.format.DateTimeFormatter.ofPattern(TimeFormatConstant.ISO_DATE_TIME_MILLIS_FORMAT));
    }

    @Test
    public void isoDateTimeOffsetFormat_shouldBeParseable() {
        assertNotNull(java.time.format.DateTimeFormatter.ofPattern(TimeFormatConstant.ISO_DATE_TIME_OFFSET_FORMAT));
    }

    @Test
    public void isoDateTimeMillisOffsetFormat_shouldBeParseable() {
        assertNotNull(java.time.format.DateTimeFormatter.ofPattern(TimeFormatConstant.ISO_DATE_TIME_MILLIS_OFFSET_FORMAT));
    }
}
