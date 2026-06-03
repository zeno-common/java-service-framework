package io.soil.common.date;

import org.junit.Test;

import java.time.LocalDate;

import static org.junit.Assert.*;

/**
 * {@link DateUtil} 单元测试
 */
public class DateUtilTest {

    // ==================== defaultFormat ====================

    @Test
    public void defaultFormat_shouldFormatAsDefaultPattern() {
        LocalDate date = LocalDate.of(2025, 6, 3);
        String result = DateUtil.defaultFormat(date);

        assertEquals("2025-06-03", result);
    }

    @Test
    public void defaultFormat_withFirstDayOfYear_shouldFormatCorrectly() {
        LocalDate date = LocalDate.of(2025, 1, 1);
        String result = DateUtil.defaultFormat(date);

        assertEquals("2025-01-01", result);
    }

    // ==================== toIsoString ====================

    @Test
    public void toIsoString_shouldFormatAsIso8601() {
        LocalDate date = LocalDate.of(2025, 6, 3);
        String result = DateUtil.toIsoString(date);

        assertEquals("2025-06-03", result);
    }

    // ==================== fromIsoString ====================

    @Test
    public void fromIsoString_shouldParseIso8601String() {
        LocalDate result = DateUtil.fromIsoString("2025-06-03");

        assertEquals(LocalDate.of(2025, 6, 3), result);
    }

    @Test(expected = java.time.format.DateTimeParseException.class)
    public void fromIsoString_withInvalidFormat_shouldThrowException() {
        DateUtil.fromIsoString("2025/06/03");
    }

    // ==================== round-trip ====================

    @Test
    public void isoStringRoundTrip_shouldPreserveValue() {
        LocalDate original = LocalDate.of(2025, 12, 25);
        String isoString = DateUtil.toIsoString(original);
        LocalDate parsed = DateUtil.fromIsoString(isoString);

        assertEquals(original, parsed);
    }
}
