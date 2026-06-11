# PagedResult

`com.skylink.pojo.dto.PagedResult<T>`

分页结果响应体，用于封装分页查询的返回结果。包含查询总数和当前页的数据列表，支持泛型以适配不同类型的分页数据。

## 类签名

```java
@Data
public class PagedResult<T>
```

## 字段

| 字段 | 类型 | 说明 |
|------|------|------|
| total | `int` | 查询总数 |
| items | `Collection<T>` | 返回项列表 |

## 静态工厂方法

### empty()

```java
public static <T> PagedResult<T> empty()
```

返回空结果，总数为 0，列表为空。

```java
PagedResult<User> result = PagedResult.empty();
// result.total = 0, result.items = []
```

### empty(long total)

```java
public static <T> PagedResult<T> empty(long total)
```

返回空列表但指定总数的分页结果。

```java
PagedResult<User> result = PagedResult.empty(100);
// result.total = 100, result.items = []
```

### of(long total, Collection<T> items)

```java
public static <T> PagedResult<T> of(long total, Collection<T> items)
```

根据总数和集合列表构建分页结果，`long` 版本。

```java
List<User> users = List.of(user1, user2);
PagedResult<User> result = PagedResult.of(100L, users);
```

### of(int total, Collection<T> items)

```java
public static <T> PagedResult<T> of(int total, Collection<T> items)
```

根据总数和集合列表构建分页结果。`items` 为 `null` 时自动替换为空列表。

```java
List<User> users = List.of(user1, user2);
PagedResult<User> result = PagedResult.of(100, users);

// items 为 null 时安全处理
PagedResult<User> safeResult = PagedResult.of(0, null);
// safeResult.items = []（空列表，非 null）
```