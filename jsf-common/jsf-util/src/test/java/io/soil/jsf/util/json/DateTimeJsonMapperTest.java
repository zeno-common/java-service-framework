package io.soil.jsf.util.json;

import org.junit.jupiter.api.Test;

import java.time.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * {@link JsonMapper} 单元测试
 */
public class DateTimeJsonMapperTest {


  // ==================== LocalDate ====================

  @Test
  public void toString_withLocalDate_shouldFormatAsIsoLocalDate() {
    TimeDto dto = new TimeDto();
    dto.setLocalDate(LocalDate.of(2025, 6, 3));

    String json = JsonMapper.toString(dto);

    assertTrue(json.contains("\"localDate\":\"2025-06-03\""));
  }

  @Test
  public void toObject_withLocalDate_shouldDeserializeCorrectly() {
    String json = "{\"localDate\":\"2025-06-03\"}";

    TimeDto result = JsonMapper.toObject(json, TimeDto.class);

    assertEquals(LocalDate.of(2025, 6, 3), result.getLocalDate());
  }

  @Test
  public void localDateRoundTrip_shouldPreserveValue() {
    TimeDto dto = new TimeDto();
    dto.setLocalDate(LocalDate.of(2025, 12, 31));

    String json = JsonMapper.toString(dto);
    TimeDto result = JsonMapper.toObject(json, TimeDto.class);

    assertEquals(dto.getLocalDate(), result.getLocalDate());
  }

  @Test
  public void localDate_withLeapDay_shouldHandleCorrectly() {
    TimeDto dto = new TimeDto();
    dto.setLocalDate(LocalDate.of(2024, 2, 29));

    String json = JsonMapper.toString(dto);
    TimeDto result = JsonMapper.toObject(json, TimeDto.class);

    assertEquals(LocalDate.of(2024, 2, 29), result.getLocalDate());
  }

  // ==================== LocalTime ====================

  @Test
  public void toString_withLocalTime_shouldFormatAsIsoLocalTime() {
    TimeDto dto = new TimeDto();
    dto.setLocalTime(LocalTime.of(14, 30, 45));

    String json = JsonMapper.toString(dto);

    assertTrue(json.contains("\"localTime\":\"14:30:45\""));
  }

  @Test
  public void toObject_withLocalTime_shouldDeserializeCorrectly() {
    String json = "{\"localTime\":\"14:30:45\"}";

    TimeDto result = JsonMapper.toObject(json, TimeDto.class);

    assertEquals(LocalTime.of(14, 30, 45), result.getLocalTime());
  }

  @Test
  public void localTimeRoundTrip_shouldPreserveValue() {
    TimeDto dto = new TimeDto();
    dto.setLocalTime(LocalTime.of(23, 59, 59));

    String json = JsonMapper.toString(dto);
    TimeDto result = JsonMapper.toObject(json, TimeDto.class);

    assertEquals(dto.getLocalTime(), result.getLocalTime());
  }

  @Test
  public void localTime_withMidnight_shouldHandleCorrectly() {
    TimeDto dto = new TimeDto();
    dto.setLocalTime(LocalTime.MIDNIGHT);

    String json = JsonMapper.toString(dto);
    TimeDto result = JsonMapper.toObject(json, TimeDto.class);

    assertEquals(LocalTime.of(0, 0, 0), result.getLocalTime());
  }

  @Test
  public void localTime_withNanos_shouldHandleCorrectly() {
    TimeDto dto = new TimeDto();
    dto.setLocalTime(LocalTime.of(14, 30, 45, 123456789));

    String json = JsonMapper.toString(dto);
    TimeDto result = JsonMapper.toObject(json, TimeDto.class);

    assertEquals(dto.getLocalTime(), result.getLocalTime());
  }

  // ==================== OffsetDateTime 时区变体 ====================

  @Test
  public void offsetDateTime_withUtcZ_shouldSerializeCorrectly() {
    TimeDto dto = new TimeDto();
    dto.setOffsetDateTime(OffsetDateTime.of(2025, 6, 3, 14, 30, 45, 0, ZoneOffset.ofHours(8)));

    String json = JsonMapper.toString(dto);

    assertTrue(json.contains("2025-06-03T14:30:45+08:00"));
  }

  @Test
  public void offsetDateTime_withUtcZ_shouldDeserializeCorrectly() {
    String json = "{\"offsetDateTime\":\"2025-06-03T14:30:45Z\"}";

    TimeDto result = JsonMapper.toObject(json, TimeDto.class);

    assertEquals(OffsetDateTime.of(2025, 6, 3, 14, 30, 45, 0, ZoneOffset.UTC), result.getOffsetDateTime());
  }

  @Test
  public void offsetDateTime_withPositiveOffset_shouldSerializeCorrectly() {
    TimeDto dto = new TimeDto();
    dto.setOffsetDateTime(OffsetDateTime.of(2025, 6, 3, 14, 30, 45, 0, ZoneOffset.ofHours(8)));

    String json = JsonMapper.toString(dto);

    assertTrue(json.contains("2025-06-03T14:30:45+08:00"));
  }

  @Test
  public void offsetDateTime_withNegativeOffset_shouldSerializeCorrectly() {
    TimeDto dto = new TimeDto();
    dto.setOffsetDateTime(OffsetDateTime.of(2025, 6, 3, 14, 30, 45, 0, ZoneOffset.ofHours(-5)));

    String json = JsonMapper.toString(dto);

    assertTrue(json.contains("2025-06-03T14:30:45-05:00"));
  }

  @Test
  public void offsetDateTime_withHalfHourOffset_shouldSerializeCorrectly() {
    TimeDto dto = new TimeDto();
    dto.setOffsetDateTime(OffsetDateTime.of(2025, 6, 3, 14, 30, 45, 0, ZoneOffset.ofHoursMinutes(5, 30)));

    String json = JsonMapper.toString(dto);

    assertTrue(json.contains("2025-06-03T14:30:45+05:30"));
  }

  @Test
  public void offsetDateTime_withNegativeHalfHourOffset_shouldDeserializeToUtc() {
    String json = "{\"offsetDateTime\":\"2025-06-03T14:30:45-09:30\"}";

    TimeDto result = JsonMapper.toObject(json, TimeDto.class);

    assertEquals(OffsetDateTime.of(2025, 6, 4, 0, 0, 45, 0, ZoneOffset.UTC), result.getOffsetDateTime());
  }

  @Test
  public void offsetDateTime_withNanos_shouldSerializeAndDeserialize() {
    TimeDto dto = new TimeDto();
    dto.setOffsetDateTime(OffsetDateTime.of(2025, 6, 3, 14, 30, 45, 123456789, ZoneOffset.ofHours(8)));

    String json = JsonMapper.toString(dto);
    TimeDto result = JsonMapper.toObject(json, TimeDto.class);

    assertEquals(dto.getOffsetDateTime().toInstant(), result.getOffsetDateTime().toInstant());
  }

  @Test
  public void offsetDateTime_roundTrip_shouldPreserveInstant() {
    TimeDto dto = new TimeDto();
    dto.setOffsetDateTime(OffsetDateTime.of(2025, 6, 3, 14, 30, 45, 0, ZoneOffset.ofHours(-7)));

    String json = JsonMapper.toString(dto);
    TimeDto result = JsonMapper.toObject(json, TimeDto.class);

    assertEquals(dto.getOffsetDateTime().toInstant(), result.getOffsetDateTime().toInstant());
    assertEquals(ZoneOffset.UTC, result.getOffsetDateTime().getOffset());
  }

  // ==================== ZonedDateTime ====================

  @Test
  public void toString_withZonedDateTime_shouldFormatAsIsoOffsetDateTime() {
    TimeDto dto = new TimeDto();
    dto.setZonedDateTime(ZonedDateTime.of(2025, 6, 3, 14, 30, 45, 0, ZoneId.of("Asia/Shanghai")));

    String json = JsonMapper.toString(dto);

    assertTrue(json.contains("2025-06-03T14:30:45+08:00"));
  }

  @Test
  public void toObject_withZonedDateTime_shouldDeserializeToUtc() {
    String json = "{\"zonedDateTime\":\"2025-06-03T14:30:45+08:00\"}";

    TimeDto result = JsonMapper.toObject(json, TimeDto.class);

    assertNotNull(result.getZonedDateTime());
    assertEquals(2025, result.getZonedDateTime().getYear());
    assertEquals(6, result.getZonedDateTime().getMonthValue());
    assertEquals(3, result.getZonedDateTime().getDayOfMonth());
    assertEquals(6, result.getZonedDateTime().getHour());
    assertEquals(30, result.getZonedDateTime().getMinute());
  }

  @Test
  public void zonedDateTimeRoundTrip_shouldPreserveInstant() {
    TimeDto dto = new TimeDto();
    dto.setZonedDateTime(ZonedDateTime.of(2025, 6, 3, 14, 30, 45, 0, ZoneId.of("Asia/Shanghai")));

    String json = JsonMapper.toString(dto);
    TimeDto result = JsonMapper.toObject(json, TimeDto.class);

    assertEquals(dto.getZonedDateTime().toInstant(), result.getZonedDateTime().toInstant());
  }

  @Test
  public void zonedDateTime_withUtc_shouldSerializeCorrectly() {
    TimeDto dto = new TimeDto();
    dto.setZonedDateTime(ZonedDateTime.of(2025, 6, 3, 14, 30, 45, 0, ZoneId.of("UTC")));

    String json = JsonMapper.toString(dto);

    assertTrue(json.contains("2025-06-03T14:30:45Z"));
  }

  // ==================== Instant ====================

  @Test
  public void toString_withInstant_shouldFormatAsIsoInstant() {
    TimeDto dto = new TimeDto();
    dto.setInstant(Instant.parse("2025-06-03T06:30:45Z"));

    String json = JsonMapper.toString(dto);

    assertTrue(json.contains("2025-06-03T06:30:45Z"));
  }

  @Test
  public void toObject_withInstant_shouldDeserializeCorrectly() {
    String json = "{\"instant\":\"2025-06-03T06:30:45Z\"}";

    TimeDto result = JsonMapper.toObject(json, TimeDto.class);

    assertEquals(Instant.parse("2025-06-03T06:30:45Z"), result.getInstant());
  }

  @Test
  public void instantRoundTrip_shouldPreserveValue() {
    TimeDto dto = new TimeDto();
    dto.setInstant(Instant.parse("2025-06-03T06:30:45.123456789Z"));

    String json = JsonMapper.toString(dto);
    TimeDto result = JsonMapper.toObject(json, TimeDto.class);

    assertEquals(dto.getInstant(), result.getInstant());
  }

  @Test
  public void instant_withEpochMillis_shouldDeserializeCorrectly() {
    String json = "{\"instant\":\"1970-01-01T00:00:00Z\"}";

    TimeDto result = JsonMapper.toObject(json, TimeDto.class);

    assertEquals(Instant.EPOCH, result.getInstant());
  }

  // ==================== OffsetTime ====================

  @Test
  public void toString_withOffsetTime_shouldFormatAsIsoOffsetTime() {
    TimeDto dto = new TimeDto();
    dto.setOffsetTime(OffsetTime.of(14, 30, 45, 0, ZoneOffset.ofHours(8)));

    String json = JsonMapper.toString(dto);

    assertTrue(json.contains("14:30:45+08:00"));
  }

  @Test
  public void toObject_withOffsetTime_shouldDeserializeCorrectly() {
    String json = "{\"offsetTime\":\"14:30:45+08:00\"}";

    TimeDto result = JsonMapper.toObject(json, TimeDto.class);

    assertEquals(OffsetTime.of(14, 30, 45, 0, ZoneOffset.ofHours(8)), result.getOffsetTime());
  }

  @Test
  public void offsetTimeRoundTrip_shouldPreserveValue() {
    TimeDto dto = new TimeDto();
    dto.setOffsetTime(OffsetTime.of(14, 30, 45, 0, ZoneOffset.ofHours(8)));

    String json = JsonMapper.toString(dto);
    TimeDto result = JsonMapper.toObject(json, TimeDto.class);

    assertEquals(dto.getOffsetTime(), result.getOffsetTime());
  }

  @Test
  public void offsetTime_withUtc_shouldSerializeCorrectly() {
    TimeDto dto = new TimeDto();
    dto.setOffsetTime(OffsetTime.of(14, 30, 45, 0, ZoneOffset.UTC));

    String json = JsonMapper.toString(dto);

    assertTrue(json.contains("14:30:45Z"));
  }

  // ==================== Year ====================

  @Test
  public void toString_withYear_shouldFormatAsYearString() {
    TimeDto dto = new TimeDto();
    dto.setYear(Year.of(2025));

    String json = JsonMapper.toString(dto);

    assertTrue(json.contains("\"year\":\"2025\""));
  }

  @Test
  public void toObject_withYear_shouldDeserializeCorrectly() {
    String json = "{\"year\":\"2025\"}";

    TimeDto result = JsonMapper.toObject(json, TimeDto.class);

    assertEquals(Year.of(2025), result.getYear());
  }

  @Test
  public void yearRoundTrip_shouldPreserveValue() {
    TimeDto dto = new TimeDto();
    dto.setYear(Year.of(1999));

    String json = JsonMapper.toString(dto);
    TimeDto result = JsonMapper.toObject(json, TimeDto.class);

    assertEquals(dto.getYear(), result.getYear());
  }

  // ==================== YearMonth ====================

  @Test
  public void toString_withYearMonth_shouldFormatAsYearMonth() {
    TimeDto dto = new TimeDto();
    dto.setYearMonth(YearMonth.of(2025, 6));

    String json = JsonMapper.toString(dto);

    assertTrue(json.contains("\"yearMonth\":\"2025-06\""));
  }

  @Test
  public void toObject_withYearMonth_shouldDeserializeCorrectly() {
    String json = "{\"yearMonth\":\"2025-06\"}";

    TimeDto result = JsonMapper.toObject(json, TimeDto.class);

    assertEquals(YearMonth.of(2025, 6), result.getYearMonth());
  }

  @Test
  public void yearMonthRoundTrip_shouldPreserveValue() {
    TimeDto dto = new TimeDto();
    dto.setYearMonth(YearMonth.of(2025, 12));

    String json = JsonMapper.toString(dto);
    TimeDto result = JsonMapper.toObject(json, TimeDto.class);

    assertEquals(dto.getYearMonth(), result.getYearMonth());
  }

  // ==================== 混合时间类型 ====================

  @Test
  public void multipleTimeTypes_shouldAllSerializeAndDeserializeCorrectly() {
    TimeDto dto = new TimeDto();
    dto.setLocalDate(LocalDate.of(2025, 6, 3));
    dto.setLocalTime(LocalTime.of(14, 30, 45));
    dto.setLocalDateTime(LocalDateTime.of(2025, 6, 3, 14, 30, 45));
    dto.setOffsetDateTime(OffsetDateTime.of(2025, 6, 3, 14, 30, 45, 0, ZoneOffset.ofHours(8)));
    dto.setZonedDateTime(ZonedDateTime.of(2025, 6, 3, 14, 30, 45, 0, ZoneId.of("Asia/Shanghai")));
    dto.setInstant(Instant.parse("2025-06-03T06:30:45Z"));
    dto.setOffsetTime(OffsetTime.of(14, 30, 45, 0, ZoneOffset.ofHours(8)));
    dto.setYear(Year.of(2025));
    dto.setYearMonth(YearMonth.of(2025, 6));

    String json = JsonMapper.toString(dto);
    TimeDto result = JsonMapper.toObject(json, TimeDto.class);

    assertEquals(dto.getLocalDate(), result.getLocalDate());
    assertEquals(dto.getLocalTime(), result.getLocalTime());
    assertEquals(dto.getLocalDateTime(), result.getLocalDateTime());
    assertEquals(dto.getOffsetDateTime().toInstant(), result.getOffsetDateTime().toInstant());
    assertEquals(dto.getZonedDateTime().toInstant(), result.getZonedDateTime().toInstant());
    assertEquals(dto.getInstant(), result.getInstant());
    assertEquals(dto.getOffsetTime(), result.getOffsetTime());
    assertEquals(dto.getYear(), result.getYear());
    assertEquals(dto.getYearMonth(), result.getYearMonth());
  }

  @Test
  public void nullTimeFields_shouldSerializeAsNull() {
    TimeDto dto = new TimeDto();

    String json = JsonMapper.toString(dto);

    assertTrue(json.contains("\"localDate\":null"));
    assertTrue(json.contains("\"localTime\":null"));
    assertTrue(json.contains("\"offsetDateTime\":null"));
    assertTrue(json.contains("\"instant\":null"));
  }

  // ==================== 边界值测试 ====================

  @Test
  public void offsetDateTime_withMaxOffset_shouldSerializeCorrectly() {
    TimeDto dto = new TimeDto();
    dto.setOffsetDateTime(OffsetDateTime.of(2025, 6, 3, 14, 30, 45, 0, ZoneOffset.ofHoursMinutes(12, 0)));

    String json = JsonMapper.toString(dto);

    assertTrue(json.contains("+12:00"));
  }

  @Test
  public void offsetDateTime_withMinOffset_shouldSerializeCorrectly() {
    TimeDto dto = new TimeDto();
    dto.setOffsetDateTime(OffsetDateTime.of(2025, 6, 3, 14, 30, 45, 0, ZoneOffset.ofHoursMinutes(-12, 0)));

    String json = JsonMapper.toString(dto);

    assertTrue(json.contains("-12:00"));
  }

  public static class TimeDto {
    private LocalDate localDate;
    private LocalTime localTime;
    private LocalDateTime localDateTime;
    private OffsetDateTime offsetDateTime;
    private ZonedDateTime zonedDateTime;
    private Instant instant;
    private OffsetTime offsetTime;
    private Year year;
    private YearMonth yearMonth;

    public LocalDate getLocalDate() {
      return localDate;
    }

    public void setLocalDate(LocalDate localDate) {
      this.localDate = localDate;
    }

    public LocalTime getLocalTime() {
      return localTime;
    }

    public void setLocalTime(LocalTime localTime) {
      this.localTime = localTime;
    }

    public LocalDateTime getLocalDateTime() {
      return localDateTime;
    }

    public void setLocalDateTime(LocalDateTime localDateTime) {
      this.localDateTime = localDateTime;
    }

    public OffsetDateTime getOffsetDateTime() {
      return offsetDateTime;
    }

    public void setOffsetDateTime(OffsetDateTime offsetDateTime) {
      this.offsetDateTime = offsetDateTime;
    }

    public ZonedDateTime getZonedDateTime() {
      return zonedDateTime;
    }

    public void setZonedDateTime(ZonedDateTime zonedDateTime) {
      this.zonedDateTime = zonedDateTime;
    }

    public Instant getInstant() {
      return instant;
    }

    public void setInstant(Instant instant) {
      this.instant = instant;
    }

    public OffsetTime getOffsetTime() {
      return offsetTime;
    }

    public void setOffsetTime(OffsetTime offsetTime) {
      this.offsetTime = offsetTime;
    }

    public Year getYear() {
      return year;
    }

    public void setYear(Year year) {
      this.year = year;
    }

    public YearMonth getYearMonth() {
      return yearMonth;
    }

    public void setYearMonth(YearMonth yearMonth) {
      this.yearMonth = yearMonth;
    }
  }

  public enum TestStatus {
    ACTIVE, INACTIVE
  }
}
