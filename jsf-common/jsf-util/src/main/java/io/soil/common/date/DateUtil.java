package io.soil.common.date;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.Temporal;

/**
 * 日期工具类，提供日期的格式化操作。
 * <p>
 * 支持常规格式（yyyy-MM-dd）和 ISO 8601 格式的日期处理。
 * </p>
 *
 * @author zeno
 */
public class DateUtil{

  /** 默认日期格式化器，格式为 yyyy-MM-dd */
  public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(TimeFormatConstant.DATE_FORMAT);

  /** ISO 8601 日期格式化器，格式为 yyyy-MM-dd */
  public static final DateTimeFormatter ISO_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;

  /**
   * 使用默认格式（yyyy-MM-dd）格式化日期对象
   *
   * @param date 日期对象
   * @return 格式化后的日期字符串
   */
  public static String defaultFormat(Temporal date){
    return DATE_FORMATTER.format(date);
  }

  /**
   * 使用 ISO 8601 格式（yyyy-MM-dd）格式化日期对象
   *
   * @param date 日期对象
   * @return ISO 8601 格式日期字符串
   */
  public static String toIsoString(Temporal date) {
    return ISO_FORMATTER.format(date);
  }

  /**
   * 从 ISO 8601 字符串解析为 LocalDate
   *
   * @param isoText ISO 8601 格式日期字符串
   * @return LocalDate 对象
   */
  public static LocalDate fromIsoString(String isoText) {
    return LocalDate.parse(isoText, ISO_FORMATTER);
  }
}
