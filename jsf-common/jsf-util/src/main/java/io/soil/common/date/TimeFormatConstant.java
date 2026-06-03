package io.soil.common.date;

/**
 * 时间格式常量类，定义常用的时间日期格式字符串。
 *
 * @author zeno.w
 */
public final class TimeFormatConstant{
//  /** 日期时间格式：yyyy-MM-dd HH:mm:ss */
//  public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
  /** 日期格式：yyyy-MM-dd */
  public static final String DATE_FORMAT = "yyyy-MM-dd";
  /** 时间格式：HH:mm:ss */
  public static final String TIME_FORMAT = "HH:mm:ss";

  /** ISO 8601 日期时间格式：yyyy-MM-dd'T'HH:mm:ss */
  public static final String ISO_DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
  /** ISO 8601 带毫秒的日期时间格式：yyyy-MM-dd'T'HH:mm:ss.SSS */
  public static final String ISO_DATE_TIME_MILLIS_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS";
  /** ISO 8601 带时区的日期时间格式：yyyy-MM-dd'T'HH:mm:ssXXX */
  public static final String ISO_DATE_TIME_OFFSET_FORMAT = "yyyy-MM-dd'T'HH:mm:ssXXX";
  /** ISO 8601 带毫秒和时区的日期时间格式：yyyy-MM-dd'T'HH:mm:ss.SSSXXX */
  public static final String ISO_DATE_TIME_MILLIS_OFFSET_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX";

  private TimeFormatConstant() {}
}
