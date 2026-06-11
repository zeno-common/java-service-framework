# DateTimeConst

`io.soil.common.date.DateTimeConst`

日期时间常量类，提供系统时区偏移量等常量定义。

## 类签名

```java
public final class DateTimeConst
```

## 常量

| 常量 | 类型 | 说明 |
|------|------|------|
| SYSTEM_ZONE_OFFSET | `ZoneOffset` | 系统默认时区偏移量，基于当前时刻计算 |

## 使用示例

```java
// 获取系统时区偏移量
ZoneOffset offset = DateTimeConst.SYSTEM_ZONE_OFFSET;

// 用于 OffsetDateTime 构造
OffsetDateTime now = OffsetDateTime.now(DateTimeConst.SYSTEM_ZONE_OFFSET);
```