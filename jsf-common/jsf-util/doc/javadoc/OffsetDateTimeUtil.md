# OffsetDateTimeUtil

`io.soil.common.date.OffsetDateTimeUtil`

日期时间工具类，提供日期时间的格式化和转换操作。支持 ISO 8601 格式的时间处理。

## 类签名

```java
public class OffsetDateTimeUtil
```

## 常量

| 常量 | 类型 | 说明 |
|------|------|------|
| OFFSET_DATE_TIME_FORMATTER | `DateTimeFormatter` | ISO 8601 日期时间格式化器（`yyyy-MM-dd'T'HH:mm:ssXXX`） |

## 静态方法

### toDayStartString(Temporal)

```java
public static String toDayStartString(Temporal time)
```

将指定日期时间格式化为当天开始时间字符串（00:00:00），ISO 8601 格式。

```java
OffsetDateTime time = OffsetDateTime.of(2025, 6, 11, 14, 30, 0, 0, ZoneOffset.ofHours(8));
String start = OffsetDateTimeUtil.toDayStartString(time);
// "2025-06-11T00:00:00+08:00"
```

### toDayEndString(Temporal)

```java
public static String toDayEndString(Temporal time)
```

将指定日期时间格式化为当天结束时间字符串（23:59:59.999999999），ISO 8601 格式。

```java
OffsetDateTime time = OffsetDateTime.of(2025, 6, 11, 14, 30, 0, 0, ZoneOffset.ofHours(8));
String end = OffsetDateTimeUtil.toDayEndString(time);
// "2025-06-11T23:59:59.999999999+08:00"
```

### toMillis(Temporal)

```java
public static Long toMillis(Temporal time)
```

将指定日期时间转换为毫秒时间戳。

```java
OffsetDateTime time = OffsetDateTime.of(2025, 6, 11, 14, 30, 0, 0, ZoneOffset.ofHours(8));
Long millis = OffsetDateTimeUtil.toMillis(time);
// 1749621000000
```

### from(Temporal)

```java
public static OffsetDateTime from(Temporal time)
```

将任意 `Temporal` 类型统一转换为 `OffsetDateTime`。支持以下类型：

| 输入类型 | 转换规则 |
|----------|----------|
| OffsetDateTime | 直接返回 |
| ZonedDateTime | 转为 OffsetDateTime |
| Instant | 以 UTC 时区构建 |
| LocalDateTime | 以系统默认时区构建 |
| LocalDate | 以当天 00:00 + 系统默认时区构建 |
| LocalTime | 以当天日期 + 系统默认时区构建 |
| 其他 | 抛出 UnsupportedOperationException |

```java
// LocalDateTime -> OffsetDateTime
LocalDateTime ldt = LocalDateTime.of(2025, 6, 11, 14, 30);
OffsetDateTime odt = OffsetDateTimeUtil.from(ldt);

// Instant -> OffsetDateTime (UTC)
Instant instant = Instant.now();
OffsetDateTime utcTime = OffsetDateTimeUtil.from(instant);

// LocalDate -> OffsetDateTime
LocalDate date = LocalDate.of(2025, 6, 11);
OffsetDateTime dayStart = OffsetDateTimeUtil.from(date);
```

### toString(Temporal)

```java
public static String toString(Temporal time)
```

将任意 `Temporal` 类型格式化为 ISO 8601 字符串（`yyyy-MM-dd'T'HH:mm:ssXXX`）。

```java
LocalDateTime ldt = LocalDateTime.of(2025, 6, 11, 14, 30);
String isoStr = OffsetDateTimeUtil.toString(ldt);
// "2025-06-11T14:30:00+08:00"
```