# TreeUtils

> 树工具类，list / tree / map 三种结构互转。

- **包**: `io.soil.jsf.common.tree`

## 方法

### toTree

`static List<Node> toTree(List<Node> list)`

> 扁平列表转树结构（按 `parentId` 组装 children）。

**参数**: `list` (List&lt;Node&gt;) — 节点列表
**返回**: List&lt;Node&gt; — 树节点列表

### toList

`static List<Node> toList(Collection<Node> tree)`

> 树结构转扁平列表（深度遍历）。

**参数**: `tree` (Collection&lt;Node&gt;) — 树节点集合
**返回**: List&lt;Node&gt;

### toMap

`static Map<Object,Node> toMap(Collection<Node> tree)`

> 树结构转 Map，key 为节点 `id`。

**参数**: `tree` (Collection&lt;Node&gt;) — 树节点集合
**返回**: Map&lt;Object,Node&gt;

- **相关**: [Node](#Node)
