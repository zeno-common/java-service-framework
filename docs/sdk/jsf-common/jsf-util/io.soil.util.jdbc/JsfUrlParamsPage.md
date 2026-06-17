# `JsfUrlParamsPage`

`package io.soil.util.jdbc`

implements `IPage<T>`

基于 URL 参数的 MyBatis-Plus 分页对象，实现 IPage 接口。

从 HTTP 请求的 URL 参数中自动提取分页信息（页码、每页条数、排序等），支持 pageNo/pageSize 和 offset/limit 两种分页模式。

- **Type Parameters:** `<T>` — 分页数据类型
- **Author:** koala

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

### `List<T> getRecords()`

→ **List\<T\>** — 查询数据列表

*(No description provided)*

---

### `IPage<T> setRecords(List<T> records)`

→ **IPage\<T\>** — 分页对象

*(No description provided)*

| Param | Type | Description |
|-------|------|-------------|
| `records` | `List<T>` | 查询数据列表 |

---

### `long getTotal()`

→ **long** — 总数

*(No description provided)*

---

### `IPage<T> setTotal(long total)`

→ **IPage\<T\>** — 分页对象

*(No description provided)*

| Param | Type | Description |
|-------|------|-------------|
| `total` | `long` | 总数 |

---

### `long getSize()`

→ **long** — 每页显示条数

*(No description provided)*

---

### `IPage<T> setSize(long size)`

→ **IPage\<T\>** — 分页对象

*(No description provided)*

| Param | Type | Description |
|-------|------|-------------|
| `size` | `long` | 每页显示条数 |

---

### `long getCurrent()`

→ **long** — 当前页

*(No description provided)*

---

### `IPage<T> setCurrent(long current)`

→ **IPage\<T\>** — 分页对象

*(No description provided)*

| Param | Type | Description |
|-------|------|-------------|
| `current` | `long` | 当前页 |

---

### `String countId()`

→ **String** — countId

*(No description provided)*

---

### `Long maxLimit()`

→ **Long** — 单页分页条数限制

*(No description provided)*

---

### `JsfUrlParamsPage<T> addOrder(OrderItem... items)`

→ **JsfUrlParamsPage\<T\>** — 返回分页参数本身

添加新的排序条件，构造条件可以使用工厂：OrderItem#descs(String...)、OrderItem#ascs(String...)

| Param | Type | Description |
|-------|------|-------------|
| `items` | `OrderItem...` | 条件 |

---

### `JsfUrlParamsPage<T> addOrder(List<OrderItem> items)`

→ **JsfUrlParamsPage\<T\>** — 返回分页参数本身

添加新的排序条件，构造条件可以使用工厂：OrderItem#descs(String...)、OrderItem#ascs(String...)

| Param | Type | Description |
|-------|------|-------------|
| `items` | `List<OrderItem>` | 条件 |

---

### `List<OrderItem> orders()`

→ **List\<OrderItem\>** — 排序字段信息

*(No description provided)*

---

### `boolean optimizeCountSql()`

→ **boolean** — 是否自动优化 COUNT SQL

*(No description provided)*

---

### `static <T> JsfUrlParamsPage<T> of(long current, long size, long total, boolean searchCount)`

→ **JsfUrlParamsPage\<T\>** — 分页对象

创建分页对象，指定当前页、每页条数、总数和是否查询总数

| Param | Type | Description |
|-------|------|-------------|
| `current` | `long` | 当前页 |
| `size` | `long` | 每页条数 |
| `total` | `long` | 总数 |
| `searchCount` | `boolean` | 是否查询总数 |
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

### `boolean optimizeJoinOfCountSql()`

→ **boolean** — *(No description provided)*

*(No description provided)*

---

### `JsfUrlParamsPage<T> setSearchCount(boolean searchCount)`

→ **JsfUrlParamsPage\<T\>** — 分页对象

*(No description provided)*

| Param | Type | Description |
|-------|------|-------------|
| `searchCount` | `boolean` | 是否进行 count 查询 |

---

### `JsfUrlParamsPage<T> setOptimizeCountSql(boolean optimizeCountSql)`

→ **JsfUrlParamsPage\<T\>** — 分页对象

*(No description provided)*

| Param | Type | Description |
|-------|------|-------------|
| `optimizeCountSql` | `boolean` | 是否自动优化 COUNT SQL |

---

### `long getPages()`

→ **long** — 总页数

*(No description provided)*

---

### `static <T> JsfUrlParamsPage<T> of(long current, long size)`

→ **JsfUrlParamsPage\<T\>** — 分页对象

创建分页对象，指定当前页和每页条数

| Param | Type | Description |
|-------|------|-------------|
| `current` | `long` | 当前页 |
| `size` | `long` | 每页条数 |
| `<T>` | | 分页数据类型 |

---

### `static <T> JsfUrlParamsPage<T> of(long current, long size, long total)`

→ **JsfUrlParamsPage\<T\>** — 分页对象

创建分页对象，指定当前页、每页条数和总数

| Param | Type | Description |
|-------|------|-------------|
| `current` | `long` | 当前页 |
| `size` | `long` | 每页条数 |
| `total` | `long` | 总数 |
| `<T>` | | 分页数据类型 |

---

### `static <T> JsfUrlParamsPage<T> of(long current, long size, boolean searchCount)`

→ **JsfUrlParamsPage\<T\>** — 分页对象

创建分页对象，指定当前页、每页条数和是否查询总数

| Param | Type | Description |
|-------|------|-------------|
| `current` | `long` | 当前页 |
| `size` | `long` | 每页条数 |
| `searchCount` | `boolean` | 是否查询总数 |
| `<T>` | | 分页数据类型 |

---

### `static <T> JsfUrlParamsPage<T> of(boolean searchCount)`

→ **JsfUrlParamsPage\<T\>** — 分页对象

从 URL 参数创建分页对象，指定是否查询总数

| Param | Type | Description |
|-------|------|-------------|
| `searchCount` | `boolean` | 是否查询总数 |
| `<T>` | | 分页数据类型 |

---

### `static <T> JsfUrlParamsPage<T> urlPage()`

→ **JsfUrlParamsPage\<T\>** — 分页对象

从 URL 参数创建分页对象（默认查询总数）

| Param | Type | Description |
|-------|------|-------------|
| `<T>` | | 分页数据类型 |

---

### `boolean searchCount()`

→ **boolean** — 是否进行 count 查询

*(No description provided)*