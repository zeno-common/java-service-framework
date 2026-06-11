# JsfUrlParameter

`io.soil.util.jdbc.JsfUrlParameter`

JSF URL 参数工具类，从 HTTP 请求中提取分页、排序、偏移等 URL 参数。支持从当前请求上下文中自动获取参数，并提供默认值和参数校验。

## 类签名

```java
public class JsfUrlParameter
```

## 默认值

| 参数 | 默认值 |
|------|--------|
| 每页条数 | 10 |
| 当前页 | 1 |
| 最大每页条数 | 500 |
| 偏移量 | 0 |
| 限制量 | 10 |

## 静态方法

### offset(Integer defaultOffset)

```java
public static Integer offset(Integer defaultOffset)
```

获取请求中的偏移量，参数不存在时使用默认值。

```java
// 请求: GET /api?$offset=100
Integer off = JsfUrlParameter.offset(0);
// 100

// 请求无 $offset 参数
Integer off = JsfUrlParameter.offset(0);
// 0
```

### offset()

```java
public static Integer offset()
```

获取请求中的偏移量，默认为 0。

### limit(Integer defaultLimit)

```java
public static Integer limit(Integer defaultLimit)
```

获取请求中的限制条数，参数不存在时使用默认值。

### limit()

```java
public static Integer limit()
```

获取请求中的限制条数，默认为 10。

### pageSize(Integer defaultSize)

```java
public static Integer pageSize(Integer defaultSize)
```

获取请求中的每页条数，超过最大值（500）时自动截断。

```java
// 请求: GET /api?$pageSize=1000
Integer size = JsfUrlParameter.pageSize(10);
// 500（截断为最大值）
```

### pageSize()

```java
public static Integer pageSize()
```

获取请求中的每页条数，默认为 10。

### pageNo(Integer defaultPageNo)

```java
public static Integer pageNo(Integer defaultPageNo)
```

获取请求中的当前页码，参数不存在时使用默认值。

### pageNo()

```java
public static Integer pageNo()
```

获取请求中的当前页码，默认为 1。

### getOffsetId()

```java
public static Long getOffsetId()
```

获取请求中的偏移 ID，用于轮询查询。参数不存在时返回 `null`。

```java
// 请求: GET /api?$offsetId=12345
Long id = JsfUrlParameter.getOffsetId();
// 12345
```

### getOffsetTime()

```java
public static String getOffsetTime()
```

获取请求中的偏移时间字符串，用于轮询查询。参数不存在时返回 `null`。

### order()

```java
public static String order()
```

获取请求中的排序参数。参数不存在时返回 `null`。

```java
// 请求: GET /api?$order=name asc,create_time desc
String order = JsfUrlParameter.order();
// "name asc,create_time desc"
```

### getInteger(String paramName, Integer defaultValue)

```java
public static Integer getInteger(String paramName, Integer defaultValue)
```

获取请求中指定参数名的整数值，带默认值。

### getString(String paramName)

```java
public static String getString(String paramName)
```

获取请求中指定参数名的字符串值。