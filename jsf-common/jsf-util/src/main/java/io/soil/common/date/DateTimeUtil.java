package io.soil.common.date;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
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
public class DateTimeUtil{

  /** 默认日期时间格式化器，格式为 yyyy-MM-dd HH:mm:ss */
  public static final DateTimeFormatter DEFAULT_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

  /** ISO 8601 日期时间格式化器，格式为 yyyy-MM-dd'T'HH:mm:ss */
  public static final DateTimeFormatter ISO_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

  /** ISO 8601 带偏移时区的日期时间格式化器，格式为 yyyy-MM-dd'T'HH:mm:ssXXX */
  public static final DateTimeFormatter ISO_OFFSET_FORMATTER = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

  /**
   * 将指定日期时间格式化为当天开始时间字符串（00:00:00）
   *
   * @param time 指定的日期时间对象
   * @return 当天的 00:00:00 格式化字符串
   */
  public static String toDayStartString(Temporal time) {
    return ISO_FORMATTER.format(time.with(LocalTime.MIN));
  }

  /**
   * 将指定日期时间格式化为当天结束时间字符串（23:59:59）
   *
   * @param time 指定的日期时间对象
   * @return 当天的 23:59:59 格式化字符串
   */
  public static String toDayEndString(Temporal time){
    return ISO_FORMATTER.format(time.with(LocalTime.MAX));
  }

  /**
   * 将指定日期时间转换为毫秒时间戳
   *
   * @param time 指定日期
   * @return 指定日期的毫秒时间戳
   */
  public static Long toMillis(LocalDateTime time) {
    return time.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
  }

  /**
   * 将日期时间格式化为 ISO 8601 字符串（yyyy-MM-dd'T'HH:mm:ss）
   *
   * @param time 日期时间对象
   * @return ISO 8601 格式字符串
   */
  public static String toIsoString(LocalDateTime time) {
    return ISO_FORMATTER.format(time);
  }

  /**
   * 将带时区偏移的日期时间格式化为 ISO 8601 字符串（yyyy-MM-dd'T'HH:mm:ssXXX）
   *
   * @param time 带时区偏移的日期时间对象
   * @return ISO 8601 带时区格式字符串
   */
  public static String toIsoOffsetString(OffsetDateTime time) {
    return ISO_OFFSET_FORMATTER.format(time);
  }

  /**
   * 将带时区的日期时间格式化为 ISO 8601 字符串（yyyy-MM-dd'T'HH:mm:ssXXX）
   *
   * @param time 带时区的日期时间对象
   * @return ISO 8601 带时区格式字符串
   */
  public static String toIsoOffsetString(ZonedDateTime time) {
    return ISO_OFFSET_FORMATTER.format(time);
  }

  /**
   * 从 ISO 8601 字符串解析为 LocalDateTime
   *
   * @param isoText ISO 8601 格式字符串
   * @return LocalDateTime 对象
   */
  public static LocalDateTime fromIsoString(String isoText) {
    return LocalDateTime.parse(isoText, ISO_FORMATTER);
  }

  /**
   * 从 ISO 8601 带时区字符串解析为 OffsetDateTime
   *
   * @param isoText ISO 8601 带时区格式字符串
   * @return OffsetDateTime 对象
   */
  public static OffsetDateTime fromIsoOffsetString(String isoText) {
    return OffsetDateTime.parse(isoText, ISO_OFFSET_FORMATTER);
  }
}
