package io.soil.common.date;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.Temporal;

/**
 * 日期工具类，提供日期的格式化操作。
 * <p>
 * 支持常规格式（yyyy-MM-dd）和 ISO 8601 日期时间格式的日期处理。
 * </p>
 *
 * @author zeno
 */
public class IsoDateUtil {

  /**
   * 使用 ISO 8601 格式（yyyy-MM-dd）格式化日期对象
   *
   * @param date 日期对象
   * @return ISO 8601 格式日期字符串
   */
  public static String format(Temporal date) {
    return DateTimeFormatter.ISO_DATE.format(date);
  }

  /**
   * 从 ISO 8601 字符串解析为 LocalDate
   *
   * @param date ISO 8601 格式日期字符串
   * @return LocalDate 对象
   */
  public static LocalDate fromIsoString(String date) {
    return LocalDate.parse(date, DateTimeFormatter.ISO_DATE);
  }
}
