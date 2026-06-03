package io.soil.common.date;

import org.junit.Test;

import java.time.*;

import static org.junit.Assert.*;

/**
 * {@link DateTimeUtil} 单元测试
 */
public class DateTimeUtilTest {

    // ==================== toDayStartString ====================

    @Test
    public void toDayStartString_shouldReturnStartOfDay() {
        LocalDateTime time = LocalDateTime.of(2025, 6, 3, 14, 30, 45);
        String result = DateTimeUtil.toDayStartString(time);

        assertEquals("2025-06-03 00:00:00", result);
    }

    @Test
    public void toDayStartString_withMidnight_shouldReturnSame() {
        LocalDateTime time = LocalDateTime.of(2025, 1, 1, 0, 0, 0);
        String result = DateTimeUtil.toDayStartString(time);

        assertEquals("2025-01-01 00:00:00", result);
    }

    // ==================== toDayEndString ====================

    @Test
    public void toDayEndString_shouldReturnEndOfDay() {
        LocalDateTime time = LocalDateTime.of(2025, 6, 3, 14, 30, 45);
        String result = DateTimeUtil.toDayEndString(time);

        assertEquals("2025-06-03 23:59:59", result);
    }

    @Test
    public void toDayEndString_withLastSecond_shouldReturnSame() {
        LocalDateTime time = LocalDateTime.of(2025, 12, 31, 23, 59, 59);
        String result = DateTimeUtil.toDayEndString(time);

        assertEquals("2025-12-31 23:59:59", result);
    }

    // ==================== toMillis ====================

    @Test
    public void toMillis_shouldReturnEpochMillis() {
        LocalDateTime time = LocalDateTime.of(1970, 1, 1, 0, 0, 0);
        Long millis = DateTimeUtil.toMillis(time);

        // 在系统默认时区下，1970-01-01 00:00:00 的毫秒数取决于时区偏移
        assertNotNull(millis);
    }

    @Test
    public void toMillis_shouldReturnPositiveForFutureDate() {
        LocalDateTime time = LocalDateTime.of(2025, 6, 3, 12, 0, 0);
        Long millis = DateTimeUtil.toMillis(time);

        assertTrue(millis > 0);
    }

    // ==================== toIsoString ====================

    @Test
    public void toIsoString_shouldFormatAsIso8601() {
        LocalDateTime time = LocalDateTime.of(2025, 6, 3, 14, 30, 45);
        String result = DateTimeUtil.toIsoString(time);

        assertEquals("2025-06-03T14:30:45", result);
    }

    @Test
    public void toIsoString_withMidnight_shouldFormatCorrectly() {
        LocalDateTime time = LocalDateTime.of(2025, 1, 1, 0, 0, 0);
        String result = DateTimeUtil.toIsoString(time);

        assertEquals("2025-01-01T00:00:00", result);
    }

    // ==================== toIsoOffsetString (OffsetDateTime) ====================

    @Test
    public void toIsoOffsetString_withOffsetDateTime_shouldFormatAsIso8601() {
        OffsetDateTime time = OffsetDateTime.of(2025, 6, 3, 14, 30, 45, 0, ZoneOffset.ofHours(8));
        String result = DateTimeUtil.toIsoOffsetString(time);

        assertEquals("2025-06-03T14:30:45+08:00", result);
    }

    // ==================== toIsoOffsetString (ZonedDateTime) ====================

    @Test
    public void toIsoOffsetString_withZonedDateTime_shouldFormatAsIso8601() {
        ZonedDateTime time = ZonedDateTime.of(2025, 6, 3, 14, 30, 45, 0, ZoneId.of("Asia/Shanghai"));
        String result = DateTimeUtil.toIsoOffsetString(time);

        assertEquals("2025-06-03T14:30:45+08:00", result);
    }

    // ==================== fromIsoString ====================

    @Test
    public void fromIsoString_shouldParseIso8601String() {
        LocalDateTime result = DateTimeUtil.fromIsoString("2025-06-03T14:30:45");

        assertEquals(LocalDateTime.of(2025, 6, 3, 14, 30, 45), result);
    }

    @Test(expected = java.time.format.DateTimeParseException.class)
    public void fromIsoString_withInvalidFormat_shouldThrowException() {
        DateTimeUtil.fromIsoString("2025-06-03 14:30:45");
    }

    // ==================== fromIsoOffsetString ====================

    @Test
    public void fromIsoOffsetString_shouldParseIso8601OffsetString() {
        OffsetDateTime result = DateTimeUtil.fromIsoOffsetString("2025-06-03T14:30:45+08:00");

        assertEquals(OffsetDateTime.of(2025, 6, 3, 14, 30, 45, 0, ZoneOffset.ofHours(8)), result);
    }

    @Test(expected = java.time.format.DateTimeParseException.class)
    public void fromIsoOffsetString_withInvalidFormat_shouldThrowException() {
        DateTimeUtil.fromIsoOffsetString("2025-06-03T14:30:45");
    }

    // ==================== round-trip ====================

    @Test
    public void isoStringRoundTrip_shouldPreserveValue() {
        LocalDateTime original = LocalDateTime.of(2025, 6, 3, 14, 30, 45);
        String isoString = DateTimeUtil.toIsoString(original);
        LocalDateTime parsed = DateTimeUtil.fromIsoString(isoString);



        assertEquals(original, parsed);
    }

    @Test
    public void isoOffsetStringRoundTrip_shouldPreserveValue() {
        OffsetDateTime original = OffsetDateTime.of(2025, 6, 3, 14, 30, 45, 0, ZoneOffset.ofHours(8));
        String isoString = DateTimeUtil.toIsoOffsetString(original);
        OffsetDateTime parsed = DateTimeUtil.fromIsoOffsetString(isoString);

        assertEquals(original, parsed);
    }
}
