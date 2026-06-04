package io.soil.common.date;

import org.junit.Test;

import java.time.*;

import static org.junit.Assert.*;

/**
 * {@link OffsetDateTimeUtil} 单元测试
 */
public class OffsetDateTimeUtilTest {
    // ==================== toDayStartString ====================

    @Test
    public void toDayStartString_shouldReturnStartOfDay() {
        OffsetDateTime time = OffsetDateTime.of(2025, 6, 3, 14, 30, 45, 0, DateTimeConst.SYSTEM_OFFSET);
        String result = OffsetDateTimeUtil.toDayStartString(time);

        assertTrue(result.startsWith("2025-06-03T00:00:00"));
    }

    @Test
    public void toDayStartString_withMidnight_shouldReturnSame() {
        OffsetDateTime time = OffsetDateTime.of(2025, 1, 1, 0, 0, 0, 0, DateTimeConst.SYSTEM_OFFSET);
        String result = OffsetDateTimeUtil.toDayStartString(time);

        assertTrue(result.startsWith("2025-01-01T00:00:00"));
    }

    // ==================== toDayEndString ====================

    @Test
    public void toDayEndString_shouldReturnEndOfDay() {
        OffsetDateTime time = OffsetDateTime.of(2025, 6, 3, 14, 30, 45, 0, DateTimeConst.SYSTEM_OFFSET);
        String result = OffsetDateTimeUtil.toDayEndString(time);

        assertTrue(result.startsWith("2025-06-03T23:59:59"));
    }

    @Test
    public void toDayEndString_withLastSecond_shouldReturnSame() {
        OffsetDateTime time = OffsetDateTime.of(2025, 12, 31, 23, 59, 59, 0, DateTimeConst.SYSTEM_OFFSET);
        String result = OffsetDateTimeUtil.toDayEndString(time);

        assertTrue(result.startsWith("2025-12-31T23:59:59"));
    }

    // ==================== toMillis ====================

    @Test
    public void toMillis_shouldReturnEpochMillisForUtcEpoch() {
        OffsetDateTime epoch = OffsetDateTime.of(1970, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC);
        Long millis = OffsetDateTimeUtil.toMillis(epoch);

        assertEquals(Long.valueOf(0L), millis);
    }

    @Test
    public void toMillis_shouldReturnPositiveForFutureDate() {
        OffsetDateTime time = OffsetDateTime.of(2025, 6, 3, 12, 0, 0, 0, DateTimeConst.SYSTEM_OFFSET);
        Long millis = OffsetDateTimeUtil.toMillis(time);

        assertTrue(millis > 0);
    }

    @Test
    public void toMillis_shouldReturnNegativeForPreEpochDate() {
        OffsetDateTime time = OffsetDateTime.of(1960, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC);
        Long millis = OffsetDateTimeUtil.toMillis(time);

        assertTrue(millis < 0);
    }
    // ==================== toString ====================

    @Test
    public void toOffsetDateTimeString_with_shouldFormatCorrectly() {
        OffsetDateTime time = OffsetDateTime.of(2025, 6, 3, 14, 30, 45, 0, ZoneOffset.ofHours(8));
        String result = OffsetDateTimeUtil.toString(time);

        assertEquals("2025-06-03T14:30:45+08:00", result);
    }

    @Test
    public void to_shouldConvertAndFormat() {
        ZonedDateTime time = ZonedDateTime.of(2025, 6, 3, 14, 30, 45, 0, ZoneId.of("Asia/Shanghai"));
        String result = OffsetDateTimeUtil.toString(time);

        assertEquals("2025-06-03T14:30:45+08:00", result);
    }

    @Test
    public void toOffsetDateTimeString_withInstant_shouldConvertToUtc() {
        Instant instant = OffsetDateTime.of(2025, 6, 3, 14, 30, 45, 0, ZoneOffset.ofHours(8)).toInstant();
        String result = OffsetDateTimeUtil.toString(instant);

        assertEquals("2025-06-03T06:30:45Z", result);
    }

    @Test
    public void to_shouldUseSystemDefaultZone() {
        LocalDateTime time = LocalDateTime.of(2025, 6, 3, 14, 30, 45);
        String result = OffsetDateTimeUtil.toString(time);

        // 结果应包含时间部分，时区取决于系统默认时区
        assertTrue(result.startsWith("2025-06-03T14:30:45"));
    }

    @Test
    public void toOffsetDateTimeString_withLocalDate_shouldUseStartOfDay() {
        LocalDate date = LocalDate.of(2025, 6, 3);
        String result = OffsetDateTimeUtil.toString(date);

        assertTrue(result.startsWith("2025-06-03T00:00:00"));
    }

    @Test
    public void toOffsetDateTimeString_withLocalTime_shouldUseTodayDate() {
        LocalTime time = LocalTime.of(14, 30, 45);
        String result = OffsetDateTimeUtil.toString(time);

        assertTrue(result.contains("T14:30:45"));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void toString_withUnsupportedTemporal_shouldThrowException() {
        // 使用一个不支持的 Temporal 类型
        java.time.YearMonth ym = java.time.YearMonth.of(2025, 6);
        OffsetDateTimeUtil.toString(ym);
    }
}
