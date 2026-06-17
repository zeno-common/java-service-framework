# `JsfUrlParamsPage`

`package io.soil.util.jdbc`

extends `com.mybatisflex.core.paginate.Page<T>`

基于 URL 参数的 MyBatis-Flex 分页对象，继承 Page。

从 HTTP 请求的 URL 参数中自动提取分页信息（页码、每页条数），支持 pageNo/pageSize 和 offset/limit 两种分页模式。排序参数通过 JsfUrlParameter.order() 获取，由调用方应用于 QueryWrapper。

- **Type Parameters:** `<T>` — 分页数据类型
- **Author:** koala

---

## Constructors

### `JsfUrlParamsPage()`

无参构造函数。

---

### `JsfUrlParamsPage(int pageNumber, int pageSize)`

分页构造函数。

| Param | Type | Description |
|-------|------|-------------|
| `pageNumber` | `int` | 当前页 |
| `pageSize` | `int` | 每页显示条数 |

---

### `JsfUrlParamsPage(int pageNumber, int pageSize, long totalRow)`

分页构造函数。

| Param | Type | Description |
|-------|------|-------------|
| `pageNumber` | `int` | 当前页 |
| `pageSize` | `int` | 每页显示条数 |
| `totalRow` | `long` | 总数 |

---

## Methods

### `boolean hasPrevious()`

→ **boolean** — true / false

是否存在上一页

---

### `boolean hasNext()`

→ **boolean** — true / false

是否存在下一页

---

### `String order()`

→ **String** — 排序字符串，参数不存在时返回 null

获取请求中的排序参数

---

## Static Methods

### `static <T> JsfUrlParamsPage<T> of(long pageNumber, long pageSize)`

→ **JsfUrlParamsPage\<T\>** — 分页对象

创建分页对象，指定当前页和每页条数

| Param | Type | Description |
|-------|------|-------------|
| `pageNumber` | `long` | 当前页 |
| `pageSize` | `long` | 每页显示条数 |
| `<T>` | | 分页数据类型 |

---

### `static <T> JsfUrlParamsPage<T> of(long pageNumber, long pageSize, long totalRow)`

→ **JsfUrlParamsPage\<T\>** — 分页对象

创建分页对象，指定当前页、每页条数和总数

| Param | Type | Description |
|-------|------|-------------|
| `pageNumber` | `long` | 当前页 |
| `pageSize` | `long` | 每页显示条数 |
| `totalRow` | `long` | 总数 |
| `<T>` | | 分页数据类型 |

---

### `static <T> JsfUrlParamsPage<T> of()`

→ **JsfUrlParamsPage\<T\>** — 分页对象

从 URL 参数创建分页对象

| Param | Type | Description |
|-------|------|-------------|
| `<T>` | | 分页数据类型 |

---

### `static <T> JsfUrlParamsPage<T> urlPage()`

→ **JsfUrlParamsPage\<T\>** — 分页对象

从 URL 参数创建分页对象（默认查询总数）

| Param | Type | Description |
|-------|------|-------------|
| `<T>` | | 分页数据类型 |

---

### `static Long getOffsetId()`

→ **Long** — 偏移 ID

获取请求中的偏移 ID

---

### `static String getOffsetTime()`

→ **String** — 偏移时间字符串

获取请求中的偏移时间

---

## Inherited from `Page<T>`

以下方法继承自 MyBatis-Flex 的 `com.mybatisflex.core.paginate.Page<T>`，可直接使用：

| Method | Return Type | Description |
|--------|-------------|-------------|
| `getPageNumber()` | `int` | 当前页码 |
| `setPageNumber(int)` | `void` | 设置当前页码 |
| `getPageSize()` | `int` | 每页条数 |
| `setPageSize(int)` | `void` | 设置每页条数 |
| `getTotalRow()` | `long` | 总记录数 |
| `setTotalRow(long)` | `void` | 设置总记录数 |
| `getTotalPage()` | `int` | 总页数 |
| `getList()` | `List<T>` | 查询数据列表 |
| `setList(List<T>)` | `void` | 设置查询数据列表 |