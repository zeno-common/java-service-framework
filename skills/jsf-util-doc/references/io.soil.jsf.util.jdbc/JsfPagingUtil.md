# JsfPagingUtil

> JSF 分页工具类，基于 REST API 规约的 URL 参数进行 PageHelper 分页。支持页码分页（pageNo/pageSize）与偏移量分页（offset/limit）两种模式。

- **包**: `io.soil.jsf.util.jdbc`
- **依赖**: `com.github.pagehelper.PageHelper`

优先使用 `offset/limit`，若无则回退 `pageNo/pageSize`。排序字段为 `null` 时自动从 URL 参数 `$order` 读取。

## 方法

### pageByUrlParam

`static <T> Page<T> pageByUrlParam()`
`static <T> Page<T> pageByUrlParam(String order)`
`static <T> Page<T> pageByUrlParam(Boolean count)`
`static <T> Page<T> pageByUrlParam(String order, boolean count)`
`static <T> Page<T> pageByUrlParam(Integer pageNo, Integer pageSize, String order)`
`static <T> Page<T> pageByUrlParam(Integer pageNo, Integer pageSize, String order, boolean count)`

> 按 URL 参数启动 PageHelper 分页（默认查询总数）。`count` 控制是否查总数，`order` 为排序字段。

**参数**:
- `pageNo` (Integer) — 当前页码
- `pageSize` (Integer) — 每页条数
- `order` (String) — 排序字段，null 时取 URL `$order`
- `count` (boolean) — 是否查询总数

**返回**: `com.github.pagehelper.Page<T>` — PageHelper 分页对象

### offsetByUrlParams

`static <T> Page<T> offsetByUrlParams(Integer offset, Integer limit, String order)`
`static <T> Page<T> offsetByUrlParams(Integer offset, Integer limit, String order, boolean count)`

> 使用偏移量启动 PageHelper 分页（`PageHelper.offsetPage`）。

**参数**:
- `offset` (Integer) — 偏移量
- `limit` (Integer) — 限制条数
- `order` (String) — 排序字段，null 时取 URL `$order`
- `count` (boolean) — 是否查询总数

**返回**: `com.github.pagehelper.Page<T>`

**示例**:
```java
Page<User> page = JsfPagingUtil.pageByUrlParam();
List<User> users = userMapper.selectAll(); // PageHelper 拦截自动分页
```

- **相关**: [JsfUrlParams](#JsfUrlParams)、[JsfUrlParamConst](#JsfUrlParamConst)
