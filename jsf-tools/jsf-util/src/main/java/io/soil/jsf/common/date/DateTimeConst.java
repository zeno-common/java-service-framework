package io.soil.jsf.common.date;

import com.fasterxml.jackson.databind.util.StdDateFormat;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;

/**
 * 日期时间常量类，提供系统默认时区偏移量等常量定义。
 *
 * @author zeno.w
 */
public final class DateTimeConst {

  /** 系统默认时区偏移量 */
  public static final ZoneOffset SYSTEM_ZONE_OFFSET = ZoneId.systemDefault().getRules().getOffset(Instant.now());
  private DateTimeConst() {}
}
