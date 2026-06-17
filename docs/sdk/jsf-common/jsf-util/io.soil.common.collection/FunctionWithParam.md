# `FunctionWithParam`

`package io.soil.common.collection`

`@FunctionalInterface`

带一个额外参数的函数式接口，扩展了标准 Function 以支持额外的参数传递。

- **Type Parameters:** `<S>` — 源类型, `<R>` — 返回类型, `<P>` — 额外参数类型

---

## Methods

### `R apply(S s, P p)`

→ **R** — 函数执行结果

应用函数

| Param | Type | Description |
|-------|------|-------------|
| `s` | `S` | 源对象 |
| `p` | `P` | 额外参数 |