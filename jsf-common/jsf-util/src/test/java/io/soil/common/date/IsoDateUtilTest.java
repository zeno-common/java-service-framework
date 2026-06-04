package io.soil.common.date;

import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZonedDateTime;

import static org.junit.Assert.*;

/**
 * {@link IsoDateUtil} 单元测试
 */
public class IsoDateUtilTest {

    // ==================== format ====================

    @Test
    public void format_shouldFormatLocalDateAsIso8601() {
        LocalDate date = LocalDate.of(2025, 6, 3);
        String result = IsoDateUtil.format(date);

        assertEquals("2025-06-03", result);
    }

    @Test
    public void format_shouldFormatLocalDateTimeAsIso8601() {
        LocalDateTime dateTime = LocalDateTime.of(2025, 6, 3, 14, 30, 45);
        String result = IsoDateUtil.format(dateTime);

        assertEquals("2025-06-03", result);
    }

    @Test
    public void format_shouldFormatOffsetDateTimeAsIso8601() {
        OffsetDateTime dateTime = OffsetDateTime.parse("2025-06-03T14:30:45+08:00");
        String result = IsoDateUtil.format(dateTime);

        assertEquals("2025-06-03+08:00", result);
    }

    @Test
    public void format_shouldFormatZonedDateTimeAsIso8601() {
        ZonedDateTime dateTime = ZonedDateTime.parse("2025-06-03T14:30:45+08:00");
        String result = IsoDateUtil.format(dateTime);

        assertEquals("2025-06-03+08:00", result);
    }

    // ==================== fromIsoString ====================

    @Test
    public void fromIsoString_shouldParse8601String() {
        LocalDate result = IsoDateUtil.fromIsoString("2025-06-03");

        assertEquals(LocalDate.of(2025, 6, 3), result);
    }

    @Test(expected = java.time.format.DateTimeParseException.class)
    public void fromIsoString_withInvalidFormat_shouldThrowException() {
        IsoDateUtil.fromIsoString("2025/06/03");
    }

    @Test(expected = java.time.format.DateTimeParseException.class)
    public void fromIsoString_withEmptyString_shouldThrowException() {
        IsoDateUtil.fromIsoString("");
    }

    @Test(expected = NullPointerException.class)
    public void fromIsoString_withNull_shouldThrowException() {
        IsoDateUtil.fromIsoString(null);
    }

    // ==================== round-trip ====================

    @Test
    public void isoStringRoundTrip_shouldPreserveValue() {
        LocalDate original = LocalDate.of(2025, 12, 25);
        String isoString = IsoDateUtil.format(original);
        LocalDate parsed = IsoDateUtil.fromIsoString(isoString);

        assertEquals(original, parsed);
    }

    @Test
    public void isoStringRoundTrip_withMinDate_shouldPreserveValue() {
        LocalDate original = LocalDate.of(1, 1, 1);
        String isoString = IsoDateUtil.format(original);
        LocalDate parsed = IsoDateUtil.fromIsoString(isoString);

        assertEquals(original, parsed);
    }

    @Test
    public void isoStringRoundTrip_withLeapDay_shouldPreserveValue() {
        LocalDate original = LocalDate.of(2024, 2, 29);
        String isoString = IsoDateUtil.format(original);
        LocalDate parsed = IsoDateUtil.fromIsoString(isoString);

        assertEquals(original, parsed);
    }
}
