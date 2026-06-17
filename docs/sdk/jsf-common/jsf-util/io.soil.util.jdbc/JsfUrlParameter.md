# `JsfUrlParameter`

`package io.soil.util.jdbc`

JSF URL 参数工具类，从 HTTP 请求中提取分页、排序、偏移等 URL 参数。

支持从当前请求上下文中自动获取分页页码、每页条数、偏移量、限制量、排序等参数，并提供默认值和参数校验。

- **Author:** zeno.w

---

## Methods

### `static Integer offset(Integer defaultOffset)`

→ **Integer** — 偏移量

获取请求中的偏移量

| Param | Type | Description |
|-------|------|-------------|
| `defaultOffset` | `Integer` | 默认偏移量，当请求参数不存在时使用 |

---

### `static Integer offset()`

→ **Integer** — 偏移量

获取请求中的偏移量，默认为 0

---

### `static Integer limit(Integer defaultLimit)`

→ **Integer** — 限制条数

获取请求中的限制条数

| Param | Type | Description |
|-------|------|-------------|
| `defaultLimit` | `Integer` | 默认限制条数，当请求参数不存在时使用 |

---

### `static Integer limit()`

→ **Integer** — 限制条数

获取请求中的限制条数，默认为 10

---

### `static Integer pageSize(Integer defaultSize)`

→ **Integer** — 每页条数

获取请求中的每页条数，超过最大值时自动截断为 500

| Param | Type | Description |
|-------|------|-------------|
| `defaultSize` | `Integer` | 默认每页条数，当请求参数不存在时使用 |

---

### `static Long getOffsetId()`

→ **Long** — 偏移 ID，参数不存在时返回 null

获取请求中的偏移 ID，用于轮询查询

---

### `static String getOffsetTime()`

→ **String** — 偏移时间字符串，参数不存在时返回 null

获取请求中的偏移时间，用于轮询查询

---

### `static Integer pageSize()`

→ **Integer** — 每页条数

获取请求中的每页条数，默认为 10

---

### `static Integer pageNo(Integer defaultPageNo)`

→ **Integer** — 当前页码

获取请求中的当前页码

| Param | Type | Description |
|-------|------|-------------|
| `defaultPageNo` | `Integer` | 默认页码，当请求参数不存在时使用 |

---

### `static Integer pageNo()`

→ **Integer** — 当前页码

获取请求中的当前页码，默认为 1

---

### `static String order()`

→ **String** — 排序字符串，参数不存在时返回 null

获取请求中的排序参数

---

### `static Integer getInteger(String paramName, Integer defaultValue)`

→ **Integer** — 参数值的整数形式，参数不存在时返回默认值

获取请求中指定参数名的整数值，带默认值

| Param | Type | Description |
|-------|------|-------------|
| `paramName` | `String` | 参数名 |
| `defaultValue` | `Integer` | 默认值 |

---

### `static String getString(String paramName)`

→ **String** — 参数值字符串

获取请求中指定参数名的字符串值

| Param | Type | Description |
|-------|------|-------------|
| `paramName` | `String` | 参数名 |