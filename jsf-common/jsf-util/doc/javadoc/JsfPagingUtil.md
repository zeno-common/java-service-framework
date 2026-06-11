# JsfPagingUtil

`io.soil.util.jdbc.JsfPagingUtil`

JSF 分页工具类，基于 REST API 规约的 URL 参数进行 PageHelper 分页处理。支持页码分页（`$pageNo`/`$pageSize`）和偏移量分页（`$offset`/`$limit`）两种模式。

## 类签名

```java
public class JsfPagingUtil
```

## 静态方法

### pageByUrlParam()

```java
public static <T> Page<T> pageByUrlParam()
```

根据 REST API 规约的 URL 参数进行分页处理，默认查询总数。优先使用 offset/limit，若无则使用 pageNo/pageSize。

```java
// 请求: GET /api/users?$pageNo=2&$pageSize=20
Page<User> page = JsfPagingUtil.pageByUrlParam();
List<User> users = userMapper.selectByExample(example);
// page 包含分页信息
```

### pageByUrlParam(String order)

```java
public static <T> Page<T> pageByUrlParam(String order)
```

指定排序字段，默认查询总数。

```java
Page<User> page = JsfPagingUtil.pageByUrlParam("create_time desc");
```

### pageByUrlParam(Boolean count)

```java
public static <T> Page<T> pageByUrlParam(Boolean count)
```

指定是否查询总数。

```java
// 不查询总数，提升性能
Page<User> page = JsfPagingUtil.pageByUrlParam(false);
```

### pageByUrlParam(String order, boolean count)

```java
public static <T> Page<T> pageByUrlParam(String order, boolean count)
```

指定排序和是否查询总数。`order` 为 `null` 时从 URL 参数 `$order` 获取。

```java
Page<User> page = JsfPagingUtil.pageByUrlParam("name asc", false);
```

### pageByUrlParam(Integer pageNo, Integer pageSize, String order)

```java
public static <T> Page<T> pageByUrlParam(Integer pageNo, Integer pageSize, String order)
```

手动指定页码、每页条数和排序，默认查询总数。

```java
Page<User> page = JsfPagingUtil.pageByUrlParam(1, 20, "id desc");
```

### pageByUrlParam(Integer pageNo, Integer pageSize, String order, boolean count)

```java
public static <T> Page<T> pageByUrlParam(Integer pageNo, Integer pageSize, String order, boolean count)
```

手动指定所有分页参数。`order` 为 `null` 时从 URL 参数获取。

```java
Page<User> page = JsfPagingUtil.pageByUrlParam(1, 20, "id desc", true);
```

### offsetByUrlParams(Integer offset, Integer limit, String order)

```java
public static <T> Page<T> offsetByUrlParams(Integer offset, Integer limit, String order)
```

使用偏移量分页，默认查询总数。

```java
Page<User> page = JsfPagingUtil.offsetByUrlParams(100, 20, "id asc");
```

### offsetByUrlParams(Integer offset, Integer limit, String order, boolean count)

```java
public static <T> Page<T> offsetByUrlParams(Integer offset, Integer limit, String order, boolean count)
```

使用偏移量分页，指定是否查询总数。`order` 为 `null` 时从 URL 参数获取。

```java
Page<User> page = JsfPagingUtil.offsetByUrlParams(100, 20, null, false);
```

## URL 参数规约

| 参数名 | 说明 | 示例 |
|--------|------|------|
| `$pageNo` | 页码，默认 1 | `$pageNo=2` |
| `$pageSize` | 每页条数，默认 10 | `$pageSize=20` |
| `$offset` | 偏移量 | `$offset=100` |
| `$limit` | 限制条数 | `$limit=20` |
| `$order` | 排序，逗号分隔多列 | `$order=name asc,create_time desc` |