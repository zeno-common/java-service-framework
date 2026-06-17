# `OffsetDateTimeUtil`

`package io.soil.common.date`

日期时间工具类，提供日期时间的格式化和转换操作。

支持常规格式（yyyy-MM-dd HH:mm:ss）和 ISO 8601 格式的时间处理。

- **Author:** zeno

---

## Constants

| Name | Type | Value | Description |
|------|------|-------|-------------|
| `OFFSET_DATE_TIME_FORMATTER` | `DateTimeFormatter` | `DateTimeFormatter.ISO_OFFSET_DATE_TIME` | ISO 8601 日期时间格式化器，格式为 yyyy-MM-dd'T'HH:mm:ssXXX |

---

## Methods

### `static String toDayStartString(Temporal time)`

→ **String** — 当天的 00:00:00 格式化字符串

将指定日期时间格式化为当天开始时间字符串（00:00:00）

| Param | Type | Description |
|-------|------|-------------|
| `time` | `Temporal` | 指定的日期时间对象 |

---

### `static String toDayEndString(Temporal time)`

→ **String** — 当天的 23:59:59 格式化字符串

将指定日期时间格式化为当天结束时间字符串（23:59:59）

| Param | Type | Description |
|-------|------|-------------|
| `time` | `Temporal` | 指定的日期时间对象 |

---

### `static Long toMillis(Temporal time)`

→ **Long** — 指定日期的毫秒时间戳

将指定日期时间转换为毫秒时间戳

| Param | Type | Description |
|-------|------|-------------|
| `time` | `Temporal` | 指定日期 |

---

### `static OffsetDateTime from(Temporal time)`

→ **OffsetDateTime** — OffsetDateTime 对象

将 ISO 8601 字符串解析为 OffsetDateTime

| Param | Type | Description |
|-------|------|-------------|
| `time` | `Temporal` | ISO 8601 格式日期时间字符串 |

---

### `static String toString(Temporal time)`

→ **String** — ISO 8601 带时区格式字符串

将带时区偏移的日期时间格式化为 ISO 8601 字符串（yyyy-MM-dd'T'HH:mm:ssXXX）

| Param | Type | Description |
|-------|------|-------------|
| `time` | `Temporal` | 带时区偏移的日期时间对象 |