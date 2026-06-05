package io.soil.common.date;

import com.fasterxml.jackson.databind.util.StdDateFormat;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;

public final class DateTimeConst {

  public static final ZoneOffset SYSTEM_ZONE_OFFSET = ZoneId.systemDefault().getRules().getOffset(Instant.now());
  private DateTimeConst() {}
}
