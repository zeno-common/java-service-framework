package io.soil.common.date;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.Temporal;

/**
 * @author zeno
 */
public class DateTimeUtil{

  public static final DateTimeFormatter DEFAULT_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

  /**
   * @return 当天的 00:00:00
   */
  public static String toDayStartString(Temporal time) {
    return DEFAULT_FORMATTER.format(time.with(LocalTime.MIN));
  }

  /**
   * @return 获取当天的 23:59:59
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
