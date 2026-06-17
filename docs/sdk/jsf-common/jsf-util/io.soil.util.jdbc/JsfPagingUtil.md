# `JsfPagingUtil`

`package io.soil.util.jdbc`

JSF 分页工具类，基于 REST API 规约的 URL 参数进行 PageHelper 分页处理。

支持页码分页（pageNo/pageSize）和偏移量分页（offset/limit）两种模式。

- **Author:** zeno.w

---

## Methods

### `static <T> Page<T> pageByUrlParam()`

→ **Page\<T\>** — PageHelper 的 Page 对象

根据 REST API 规约的 URL 参数进行分页处理（默认查询总数）

| Param | Type | Description |
|-------|------|-------------|
| `<T>` | | 分页数据类型 |

---

### `static <T> Page<T> pageByUrlParam(String order)`

→ **Page\<T\>** — PageHelper 的 Page 对象

根据 REST API 规约的 URL 参数进行分页处理，指定排序字段（默认查询总数）

| Param | Type | Description |
|-------|------|-------------|
| `order` | `String` | 排序字段 |
| `<T>` | | 分页数据类型 |

---

### `static <T> Page<T> pageByUrlParam(Boolean count)`

→ **Page\<T\>** — PageHelper 的 Page 对象

根据 REST API 规约的 URL 参数进行分页处理，指定是否查询总数

| Param | Type | Description |
|-------|------|-------------|
| `count` | `Boolean` | 是否查询总数 |
| `<T>` | | 分页数据类型 |

---

### `static <T> Page<T> pageByUrlParam(String order, boolean count)`

→ **Page\<T\>** — PageHelper 的 Page 对象

根据 REST API 规约的 URL 参数进行分页处理，指定排序和是否查询总数。

优先使用 offset/limit 偏移量分页，若无则使用 pageNo/pageSize 页码分页。

| Param | Type | Description |
|-------|------|-------------|
| `order` | `String` | 排序字段，为 null 时从 URL 参数获取 |
| `count` | `boolean` | 是否查询总数 |
| `<T>` | | 分页数据类型 |

---

### `static <T> Page<T> pageByUrlParam(Integer pageNo, Integer pageSize, String order)`

→ **Page\<T\>** — PageHelper 的 Page 对象

根据 REST API 规约的 URL 参数进行分页处理，指定页码、每页条数和排序（默认查询总数）

| Param | Type | Description |
|-------|------|-------------|
| `pageNo` | `Integer` | 当前页码 |
| `pageSize` | `Integer` | 每页条数 |
| `order` | `String` | 排序字段 |
| `<T>` | | 分页数据类型 |

---

### `static <T> Page<T> pageByUrlParam(Integer pageNo, Integer pageSize, String order, boolean count)`

→ **Page\<T\>** — PageHelper 的 Page 对象

根据 REST API 规约的 URL 参数进行分页处理，指定页码、每页条数、排序和是否查询总数

| Param | Type | Description |
|-------|------|-------------|
| `pageNo` | `Integer` | 当前页码 |
| `pageSize` | `Integer` | 每页条数 |
| `order` | `String` | 排序字段，为 null 时从 URL 参数获取 |
| `count` | `boolean` | 是否查询总数 |
| `<T>` | | 分页数据类型 |

---

### `static <T> Page<T> offsetByUrlParams(Integer offset, Integer limit, String order)`

→ **Page\<T\>** — PageHelper 的 Page 对象

使用偏移量进行分页处理（默认查询总数）

| Param | Type | Description |
|-------|------|-------------|
| `offset` | `Integer` | 偏移量 |
| `limit` | `Integer` | 限制条数 |
| `order` | `String` | 排序字段 |
| `<T>` | | 分页数据类型 |

---

### `static <T> Page<T> offsetByUrlParams(Integer offset, Integer limit, String order, boolean count)`

→ **Page\<T\>** — PageHelper 的 Page 对象

使用偏移量进行分页处理

| Param | Type | Description |
|-------|------|-------------|
| `offset` | `Integer` | 偏移量 |
| `limit` | `Integer` | 限制条数 |
| `order` | `String` | 排序字段，为 null 时从 URL 参数获取 |
| `count` | `boolean` | 是否查询总数 |
| `<T>` | | 分页数据类型 |