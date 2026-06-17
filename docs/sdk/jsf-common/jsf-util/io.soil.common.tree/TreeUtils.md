# `TreeUtils`

`package io.soil.common.tree`

树模型工具类，支持 list、tree、map 等节点在各种数据结构中的转换。

- **Author:** zeno.w

---

## Methods

### `static <ID, TYPE, R extends Node<ID, TYPE>> List<R> toTree(List<R> nodes)`

→ **List\<R\>** — tree 结构模型，根节点列表

list 转换成 tree

| Param | Type | Description |
|-------|------|-------------|
| `nodes` | `List<R>` | list 节点集合 |
| `<ID>` | | 节点标识类型 |
| `<TYPE>` | | 节点类型类型 |
| `<R>` | | 节点子类型 |

---

### `static <ID, TYPE, R extends Node<ID, TYPE>> List<R> toList(Collection<R> treeNodes)`

→ **List\<R\>** — list 结构，包含所有节点（含子节点）

将 tree 转换成 list

| Param | Type | Description |
|-------|------|-------------|
| `treeNodes` | `Collection<R>` | tree 节点集合 |
| `<ID>` | | 节点标识类型 |
| `<TYPE>` | | 节点类型类型 |
| `<R>` | | 节点子类型 |

---

### `static <ID, Type, R extends Node<ID, Type>> Map<ID, R> toMap(Collection<R> nodes)`

→ **Map\<ID, R\>** — 以节点 ID 为键的 Map

将 list 或 tree 转换成 map，以节点 ID 为键

| Param | Type | Description |
|-------|------|-------------|
| `nodes` | `Collection<R>` | list 或 tree 节点集合 |
| `<ID>` | | 节点标识类型 |
| `<Type>` | | 节点类型类型 |
| `<R>` | | 节点子类型 |