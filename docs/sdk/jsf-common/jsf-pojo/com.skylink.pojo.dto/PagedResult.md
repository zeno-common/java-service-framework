# `PagedResult`

`package com.skylink.pojo.dto`

`@Data`

分页结果响应体，用于封装分页查询的返回结果。

包含查询总数和当前页的数据列表，支持泛型以适配不同类型的分页数据。

- **Type Parameters:** `<T>` — 分页数据的元素类型
- **Author:** zeno.w

---

## Methods

### `static <T> PagedResult<T> empty()`

→ **PagedResult\<T\>** — 空的分页结果对象

直接返回空结果，总数为0，列表为空

| Param | Type | Description |
|-------|------|-------------|
| `<T>` | | 分页数据的元素类型 |

---

### `static <T> PagedResult<T> empty(long total)`

→ **PagedResult\<T\>** — 空列表但指定总数的分页结果对象

直接返回空结果，列表为空但指定总数

| Param | Type | Description |
|-------|------|-------------|
| `total` | `long` | 查询总数 |
| `<T>` | | 分页数据的元素类型 |

---

### `static <T> PagedResult<T> of(long total, Collection<T> items)`

→ **PagedResult\<T\>** — 分页结果对象

根据总数和集合列表构建分页结果

| Param | Type | Description |
|-------|------|-------------|
| `total` | `long` | 总数 |
| `items` | `Collection<T>` | 分页列表 |
| `<T>` | | 分页数据的元素类型 |

---

### `static <T> PagedResult<T> of(int total, Collection<T> items)`

→ **PagedResult\<T\>** — 分页结果对象

根据集合列表返回分页结果

| Param | Type | Description |
|-------|------|-------------|
| `total` | `int` | 总数 |
| `items` | `Collection<T>` | 分页列表，为null时自动替换为空列表 |
| `<T>` | | 分页数据的元素类型 |

---

## Constants

| Name | Type | Value | Description |
|------|------|-------|-------------|
| *(No public constants)* | | | |