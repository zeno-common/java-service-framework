package io.soil.common.date;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.Temporal;

/**
 * 日期时间工具类，提供日期时间的格式化和转换操作。
 *
 * @author zeno
 */
public class DateTimeUtil{

  /** 默认日期时间格式化器，格式为 yyyy-MM-dd HH:mm:ss */
  public static final DateTimeFormatter DEFAULT_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

  /**
   * 将指定日期时间格式化为当天开始时间字符串（00:00:00）
   *
   * @param time 指定的日期时间对象
   * @return 当天的 00:00:00 格式化字符串
   */
  public static String toDayStartString(Temporal time) {
    return DEFAULT_FORMATTER.format(time.with(LocalTime.MIN));
  }

  /**
   * 将指定日期时间格式化为当天结束时间字符串（23:59:59）
   *
   * @param time 指定的日期时间对象
   * @return 当天的 23:59:59 格式化字符串
   */
  public static String toDayEndString(Temporal time){
    return DEFAULT_FORMATTER.format(time.with(LocalTime.MAX));
  }

  /**
   * @param time 指定日期
   * @return 指定日期的毫秒
   */
  public static Long toMillis(LocalDateTime time) {
    return time.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
  }
}
