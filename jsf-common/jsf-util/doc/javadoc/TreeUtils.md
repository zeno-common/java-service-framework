# TreeUtils

`io.soil.common.tree.TreeUtils`

树模型工具类，支持 list、tree、map 等节点在各种数据结构中的转换。

## 类签名

```java
public class TreeUtils
```

## 静态方法

### toTree(List<R>)

```java
public static <ID, TYPE, R extends Node<ID, TYPE>> List<R> toTree(List<R> nodes)
```

将扁平的 list 节点集合转换为 tree 结构。根据 `parentId` 自动组装层级关系，`parentId` 在节点集合中找不到对应节点时视为根节点。

```java
List<Node<Long, String>> flatList = new ArrayList<>();

Node<Long, String> root = new Node<>();
root.setId(1L); root.setName("根"); root.setParentId(null);
flatList.add(root);

Node<Long, String> child = new Node<>();
child.setId(2L); child.setName("子"); child.setParentId(1L);
flatList.add(child);

List<Node<Long, String>> tree = TreeUtils.toTree(flatList);
// tree = [root]，root.children = [child]
```

### toList(Collection<R>)

```java
public static <ID, TYPE, R extends Node<ID, TYPE>> List<R> toList(Collection<R> treeNodes)
```

将 tree 结构展平为 list，包含所有节点（含子节点）。

```java
// tree 是上面 toTree 的结果
List<Node<Long, String>> flatList = TreeUtils.toList(tree);
// flatList = [root, child]
```

### toMap(Collection<R>)

```java
public static <ID, Type, R extends Node<ID, Type>> Map<ID, R> toMap(Collection<R> nodes)
```

将 list 或 tree 节点集合转换为以节点 ID 为键的 Map。自动展平子节点。

```java
List<Node<Long, String>> nodes = List.of(root, child);
Map<Long, Node<Long, String>> nodeMap = TreeUtils.toMap(nodes);
// {1=root, 2=child}
```