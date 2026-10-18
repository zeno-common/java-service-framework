# OffsetDateTimeUtil

> 日期时间格式化和转换工具类。

- **包**: `io.soil.jsf.common.date`

## 常量

| 常量 | 类型 | 说明 |
|------|------|------|
| `OFFSET_DATE_TIME_FORMATTER` | DateTimeFormatter | `yyyy-MM-dd HH:mm:ss.SSS Z`，毫秒 + 时区偏移 |

## 方法

### toDayStartString

`static String toDayStartString(LocalDate localDate)`

> 获取当天起始时刻字符串（`00:00:00`，系统默认时区）。

**参数**: `localDate` (LocalDate) — 本地日期
**返回**: String — 当天起始字符串

### toDayEndString

`static String toDayEndString(LocalDate localDate)`

> 获取当天结束时刻字符串（`23:59:59.999`，系统默认时区）。

**参数**: `localDate` (LocalDate) — 本地日期
**返回**: String — 当天结束字符串

### toMillis

`static Long toMillis(OffsetDateTime offsetDateTime)`

> OffsetDateTime 转 UTC 毫秒数（epoch）。

**参数**: `offsetDateTime` (OffsetDateTime) — 带偏移的日期时间
**返回**: Long — UTC 毫秒数

### from

`static OffsetDateTime from(Long utcMillis)`

> UTC 毫秒数转 OffsetDateTime（系统默认时区）。

**参数**: `utcMillis` (Long) — UTC 毫秒时间戳
**返回**: OffsetDateTime

### toString

`static String toString(OffsetDateTime offsetDateTime)`

> 格式化为 `OFFSET_DATE_TIME_FORMATTER` 字符串。

**参数**: `offsetDateTime` (OffsetDateTime) — 带偏移的日期时间
**返回**: String — 格式化字符串
