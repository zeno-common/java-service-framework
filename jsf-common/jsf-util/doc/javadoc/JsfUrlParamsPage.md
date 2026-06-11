# JsfUrlParamsPage

`io.soil.util.jdbc.JsfUrlParamsPage<T>`

基于 URL 参数的 MyBatis-Plus 分页对象，实现 `IPage<T>` 接口。从 HTTP 请求的 URL 参数中自动提取分页信息，支持 pageNo/pageSize 和 offset/limit 两种分页模式。

## 类签名

```java
public class JsfUrlParamsPage<T> implements IPage<T>
```

## 字段

| 字段 | 类型 | 默认值 | 说明 |
|------|------|--------|------|
| records | `List<T>` | `[]` | 查询数据列表 |
| total | `long` | 0 | 总数 |
| size | `long` | 10 | 每页显示条数 |
| current | `long` | 0 | 当前页 |
| orders | `List<OrderItem>` | `[]` | 排序字段信息 |
| optimizeCountSql | `boolean` | true | 自动优化 COUNT SQL |
| searchCount | `boolean` | true | 是否进行 count 查询 |
| optimizeJoinOfCountSql | `boolean` | true | 优化 count 的 join |
| maxLimit | `Long` | 500 | 单页分页条数限制 |
| countId | `String` | null | count 查询 ID |

## 构造方法

### JsfUrlParamsPage()

```java
public JsfUrlParamsPage()
```

### JsfUrlParamsPage(long current, long size)

```java
public JsfUrlParamsPage(long current, long size)
```

### JsfUrlParamsPage(long current, long size, long total)

```java
public JsfUrlParamsPage(long current, long size, long total)
```

### JsfUrlParamsPage(long current, long size, boolean searchCount)

```java
public JsfUrlParamsPage(long current, long size, boolean searchCount)
```

### JsfUrlParamsPage(long current, long size, long total, boolean searchCount)

```java
public JsfUrlParamsPage(long current, long size, long total, boolean searchCount)
```

## 静态工厂方法

### of(long current, long size)

```java
public static <T> JsfUrlParamsPage<T> of(long current, long size)
```

```java
JsfUrlParamsPage<User> page = JsfUrlParamsPage.of(1, 20);
```

### of(long current, long size, long total)

```java
public static <T> JsfUrlParamsPage<T> of(long current, long size, long total)
```

### of(long current, long size, boolean searchCount)

```java
public static <T> JsfUrlParamsPage<T> of(long current, long size, boolean searchCount)
```

### of(long current, long size, long total, boolean searchCount)

```java
public static <T> JsfUrlParamsPage<T> of(long current, long size, long total, boolean searchCount)
```

### of(boolean searchCount)

```java
public static <T> JsfUrlParamsPage<T> of(boolean searchCount)
```

从 URL 参数创建分页对象，自动提取 `$pageNo` 和 `$pageSize`。

```java
// 请求: GET /api/users?$pageNo=2&$pageSize=20
JsfUrlParamsPage<User> page = JsfUrlParamsPage.of(true);
```

### urlPage()

```java
public static <T> JsfUrlParamsPage<T> urlPage()
```

从 URL 参数创建分页对象，默认查询总数。

```java
JsfUrlParamsPage<User> page = JsfUrlParamsPage.urlPage();
```

## 实例方法

### addOrder(OrderItem... items)

```java
public JsfUrlParamsPage<T> addOrder(OrderItem... items)
```

添加排序条件，支持链式调用。

```java
JsfUrlParamsPage<User> page = JsfUrlParamsPage.of(1, 20)
    .addOrder(OrderItem.desc("create_time"))
    .addOrder(OrderItem.asc("name"));
```

### addOrder(List<OrderItem> items)

```java
public JsfUrlParamsPage<T> addOrder(List<OrderItem> items)
```

批量添加排序条件。

### hasPrevious() / hasNext()

```java
public boolean hasPrevious()
public boolean hasNext()
```

判断是否存在上一页/下一页。

### getOffsetId() / getOffsetTime()

```java
public static Long getOffsetId()
public static String getOffsetTime()
```

从 URL 参数获取偏移 ID 和偏移时间，用于轮询查询。

## MyBatis-Plus 使用示例

```java
// Controller
@GetMapping("/users")
public IPage<User> listUsers() {
    JsfUrlParamsPage<User> page = JsfUrlParamsPage.urlPage();
    return userMapper.selectPage(page, queryWrapper);
}
```