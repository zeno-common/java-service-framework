package io.soil.common.date;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.Temporal;

/**
 * 日期时间工具类，提供日期时间的格式化和转换操作。
 * <p>
 * 支持常规格式（yyyy-MM-dd HH:mm:ss）和 ISO 8601 格式的时间处理。
 * </p>
 *
 * @author zeno
 */
public class OffsetDateTimeUtil {

  /** ISO 8601 日期时间格式化器，格式为 yyyy-MM-dd'T'HH:mm:ssXXX */
  public static final DateTimeFormatter OFFSET_DATE_TIME_FORMATTER = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

  /**
   * 将指定日期时间格式化为当天开始时间字符串（00:00:00）
   *
   * @param time 指定的日期时间对象
   * @return 当天的 00:00:00 格式化字符串
   */
  public static String toDayStartString(Temporal time) {
    return OFFSET_DATE_TIME_FORMATTER.format(time.with(LocalTime.MIN));
  }

  /**
   * 将指定日期时间格式化为当天结束时间字符串（23:59:59）
   *
   * @param time 指定的日期时间对象
   * @return 当天的 23:59:59 格式化字符串
   */
  public static String toDayEndString(Temporal time){
    return OFFSET_DATE_TIME_FORMATTER.format(time.with(LocalTime.MAX));
  }

  /**
   * 将指定日期时间转换为毫秒时间戳
   *
   * @param time 指定日期
   * @return 指定日期的毫秒时间戳
   */
  public static Long toMillis(Temporal time) {
    return Instant.from(time).toEpochMilli();
  }

  /**
   * 将 ISO 8601 字符串解析为 OffsetDateTime
   *
   * @param time ISO 8601 格式日期时间字符串
   * @return OffsetDateTime 对象
   */
  public static OffsetDateTime from(Temporal time) {

    OffsetDateTime odt;
    if (time instanceof OffsetDateTime existing) {
      odt = existing;
    } else if (time instanceof ZonedDateTime zdt) {
      odt = zdt.toOffsetDateTime();
    } else if (time instanceof Instant instant) {
      odt = instant.atOffset(ZoneOffset.UTC);
    } else if (time instanceof LocalDateTime ldt) {
      odt = ldt.atZone(ZoneId.systemDefault()).toOffsetDateTime();
    } else if (time instanceof LocalDate ld) {
      odt = ld.atStartOfDay(ZoneId.systemDefault()).toOffsetDateTime();
    } else if (time instanceof LocalTime lt) {
      odt = LocalDate.now().atTime(lt).atZone(ZoneId.systemDefault()).toOffsetDateTime();
    } else {
      throw new UnsupportedOperationException(
        "不支持的 Temporal 类型: " + time.getClass().getName());
    }

    return odt;
  }

  /**
   * 将带时区偏移的日期时间格式化为 ISO 8601 字符串（yyyy-MM-dd'T'HH:mm:ssXXX）
   *
   * @param time 带时区偏移的日期时间对象
   * @return ISO 8601 带时区格式字符串
   */
  public static String toString(Temporal time) {
    return from(time).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
  }
}
