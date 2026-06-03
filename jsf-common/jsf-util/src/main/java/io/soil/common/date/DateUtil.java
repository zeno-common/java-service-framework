package io.soil.common.date;

import java.time.format.DateTimeFormatter;
import java.time.temporal.Temporal;

/**
 * 日期工具类，提供日期的格式化操作。
 *
 * @author zeno
 */
public class DateUtil{

  /** 默认日期格式化器，格式为 yyyy-MM-dd */
  public static final DateTimeFormatter DEFAULT_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

  /**
   * 使用默认格式（yyyy-MM-dd）格式化日期对象
   *
   * @param date 日期对象
   * @return 格式化后的日期字符串
   */
  public static String defaultFormat(Temporal date){
    return DEFAULT_FORMATTER.format(date);
  }
}
