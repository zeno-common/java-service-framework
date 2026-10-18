# Node<ID,TYPE>

> 通用树节点 DTO，泛型 `ID` 为主键类型、`TYPE` 为节点业务类型。

- **包**: `io.soil.jsf.common.tree`
- **Lombok**: `@Data`（含无参构造，`children` 默认初始化为 `ArrayList`）

## 字段

| 字段 | 类型 | 说明 |
|------|------|------|
| `id` | ID | 主键 ID |
| `name` | String | 节点名称 |
| `type` | TYPE | 节点类型 |
| `parentId` | ID | 父节点 ID |
| `ordinal` | Integer | 同级节点排序序号 |
| `addition` | Object | 附加信息 |
| `children` | List&lt;Node&lt;ID,TYPE&gt;&gt; | 子节点集合 |

> 所有字段均有 getter/setter [Lombok 生成]。

## 方法

### addChild

`void addChild(Node<ID,TYPE> child)`

> 追加子节点。

**参数**: `child` (Node) — 子节点

- **相关**: [TreeUtils](#TreeUtils)
