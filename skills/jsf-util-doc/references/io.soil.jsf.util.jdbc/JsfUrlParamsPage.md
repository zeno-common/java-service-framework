# JsfUrlParamsPage<T>

> 基于 URL 参数的 MyBatis-Flex 分页对象，从请求 URL 自动提取页码/每页条数。

- **包**: `io.soil.jsf.util.jdbc`
- **父类**: `com.mybatisflex.core.paginate.Page<T>`
- **支持**: pageNo/pageSize 与 offset/limit 两种模式；排序由 [JsfUrlParams#order()](#JsfUrlParams) 获取，调用方应用于 QueryWrapper

## 构造

| 签名 | 说明 |
|------|------|
| `JsfUrlParamsPage()` | 默认构造 |
| `JsfUrlParamsPage(int pageNumber, int pageSize)` | 当前页 + 每页条数 |
| `JsfUrlParamsPage(int pageNumber, int pageSize, long totalRow)` | 当前页 + 每页条数 + 总数 |

## 方法

### hasPrevious / hasNext

`boolean hasPrevious()` — 是否存在上一页（pageNumber > 1）
`boolean hasNext()` — 是否存在下一页（pageNumber < totalPage）

### order

`String order()`

> 获取 URL 排序参数；不存在返回 null。

**返回**: String

### of

`static <T> JsfUrlParamsPage<T> of(long pageNumber, long pageSize)`
`static <T> JsfUrlParamsPage<T> of(long pageNumber, long pageSize, long totalRow)`

> 静态构造分页对象。

### ofUrlParams

`static <T> JsfUrlParamsPage<T> ofUrlParams()`

> 从当前 URL 参数（`$pageNo` / `$pageSize`）创建分页对象。

**返回**: JsfUrlParamsPage&lt;T&gt;

**示例**:
```java
JsfUrlParamsPage<User> page = JsfUrlParamsPage.ofUrlParams();
```

- **相关**: [JsfUrlParams](#JsfUrlParams)
