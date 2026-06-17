# `FunctionWith2Param`

`package io.soil.common.collection`

`@FunctionalInterface`

带两个额外参数的函数式接口，扩展了标准 Function 以支持两个额外参数的传递。

- **Type Parameters:** `<S>` — 源类型, `<R>` — 返回类型, `<P1>` — 第一个额外参数类型, `<P2>` — 第二个额外参数类型

---

## Methods

### `R apply(S s, P1 p1, P2 p2)`

→ **R** — 函数执行结果

应用函数

| Param | Type | Description |
|-------|------|-------------|
| `s` | `S` | 源对象 |
| `p1` | `P1` | 第一个额外参数 |
| `p2` | `P2` | 第二个额外参数 |