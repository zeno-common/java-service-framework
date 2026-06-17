# `CollectionUtil`

`package io.soil.common.collection`

集合工具类，提供集合转换、映射等常用操作。

- **Author:** zeno

---

## Methods

### `static <KEY, VALUE> Map<KEY, VALUE> toMap(Collection<VALUE> values, Function<VALUE, KEY> keyMapper)`

→ **Map\<KEY, VALUE\>** — 以 keyMapper 提取的键为键、原元素为值的 Map

将集合转换为 Map，使用指定的 key 映射函数提取键，值保持不变

| Param | Type | Description |
|-------|------|-------------|
| `values` | `Collection<VALUE>` | 源集合 |
| `keyMapper` | `Function<VALUE, KEY>` | 键映射函数 |
| `<KEY>` | | Map 键类型 |
| `<VALUE>` | | Map 值类型 |

---

### `static <T, R> List<R> mapList(Collection<T> source, Function<T, R> mapper)`

→ **List\<R\>** — 映射后的新列表，源集合为空时返回空列表

将源集合中的元素映射为新的列表

| Param | Type | Description |
|-------|------|-------------|
| `source` | `Collection<T>` | 源集合 |
| `mapper` | `Function<T, R>` | 映射函数 |
| `<T>` | | 源元素类型 |
| `<R>` | | 目标元素类型 |

---

### `static <S, R, P> List<R> mapList(Collection<S> source, FunctionWithParam<S, R, P> mapper, P param1)`

→ **List\<R\>** — 映射后的新列表，源集合为空时返回空列表

将源集合中的元素使用带一个额外参数的映射函数映射为新的列表

| Param | Type | Description |
|-------|------|-------------|
| `source` | `Collection<S>` | 源集合 |
| `mapper` | `FunctionWithParam<S, R, P>` | 带一个额外参数的映射函数 |
| `param1` | `P` | 额外参数 |
| `<S>` | | 源元素类型 |
| `<R>` | | 目标元素类型 |
| `<P>` | | 额外参数类型 |

---

### `static <S, R, P1, P2> List<R> mapList(Collection<S> source, FunctionWith2Param<S, R, P1, P2> mapper, P1 p1, P2 p2)`

→ **List\<R\>** — 映射后的新列表，源集合为空时返回空列表

将源集合中的元素使用带两个额外参数的映射函数映射为新的列表

| Param | Type | Description |
|-------|------|-------------|
| `source` | `Collection<S>` | 源集合 |
| `mapper` | `FunctionWith2Param<S, R, P1, P2>` | 带两个额外参数的映射函数 |
| `p1` | `P1` | 第一个额外参数 |
| `p2` | `P2` | 第二个额外参数 |
| `<S>` | | 源元素类型 |
| `<R>` | | 目标元素类型 |
| `<P1>` | | 第一个额外参数类型 |
| `<P2>` | | 第二个额外参数类型 |

---

### `static <S, R, P1, P2, P3> List<R> mapList(Collection<S> source, FunctionWith3Param<S, R, P1, P2, P3> mapper, P1 p1, P2 p2, P3 p3)`

→ **List\<R\>** — 映射后的新列表，源集合为空时返回空列表

将源集合中的元素使用带三个额外参数的映射函数映射为新的列表

| Param | Type | Description |
|-------|------|-------------|
| `source` | `Collection<S>` | 源集合 |
| `mapper` | `FunctionWith3Param<S, R, P1, P2, P3>` | 带三个额外参数的映射函数 |
| `p1` | `P1` | 第一个额外参数 |
| `p2` | `P2` | 第二个额外参数 |
| `p3` | `P3` | 第三个额外参数 |
| `<S>` | | 源元素类型 |
| `<R>` | | 目标元素类型 |
| `<P1>` | | 第一个额外参数类型 |
| `<P2>` | | 第二个额外参数类型 |
| `<P3>` | | 第三个额外参数类型 |