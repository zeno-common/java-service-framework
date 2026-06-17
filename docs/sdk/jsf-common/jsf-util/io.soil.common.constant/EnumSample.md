# `EnumSample`

`package io.soil.common.constant`

枚举事件，主要用于枚举定义的参考事例代码。

- **Author:** zeno.w

---

## Enum Values

| Name | Description |
|------|-------------|
| `SAMPLE` | 枚举定义事例 |

---

## Methods

### `int status()`

→ **int** — 枚举状态值

获取枚举状态值

---

### `EnumSample of(int status)`

→ **EnumSample** — 枚举对象

根据状态值获取枚举对象

throws `UnknownEnumException` — 当无法匹配到对应的枚举时抛出

| Param | Type | Description |
|-------|------|-------------|
| `status` | `int` | 状态值 |