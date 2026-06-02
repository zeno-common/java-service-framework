package io.soil.common.date;

import java.time.format.DateTimeFormatter;
import java.time.temporal.Temporal;

/**
 * @author zeno
 */
public class DateUtil{

  public static final DateTimeFormatter DEFAULT_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

  public static String defaultFormat(Temporal date){
    return DEFAULT_FORMATTER.format(date);
  }
}
