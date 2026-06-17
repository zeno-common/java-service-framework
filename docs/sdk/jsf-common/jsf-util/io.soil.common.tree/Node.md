# `Node`

`package io.soil.common.tree`

`@Data`

通用树节点定义，支持泛型的节点ID和类型。

可用于构建任意层级的树形结构，每个节点包含ID、名称、类型、父节点引用及子节点列表。

- **Type Parameters:** `<ID>` — 节点标识类型, `<TYPE>` — 节点类型类型
- **Author:** zeno.w

---

## Methods

### `void addChild(Node<ID, TYPE> node)`

→ **void**

添加子节点，首次添加时自动初始化子节点列表

| Param | Type | Description |
|-------|------|-------------|
| `node` | `Node<ID, TYPE>` | 要添加的子节点 |