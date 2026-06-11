# Node

`io.soil.common.tree.Node<ID, TYPE>`

通用树节点定义，支持泛型的节点 ID 和类型。可用于构建任意层级的树形结构。

## 类签名

```java
@Data
public class Node<ID, TYPE>
```

## 类型参数

| 参数 | 说明 |
|------|------|
| ID | 节点标识类型 |
| TYPE | 节点类型类型 |

## 字段

| 字段 | 类型 | 说明 |
|------|------|------|
| id | `ID` | 节点对象标识 |
| name | `String` | 节点对象名称 |
| type | `TYPE` | 节点对象类型 |
| parentId | `ID` | 父节点标识，`null` 表示顶级节点 |
| ordinal | `Number` | 节点序数 |
| addition | `Object` | 树节点的扩展部分 |
| children | `Collection<Node<ID,TYPE>>` | 子节点列表，默认空列表 |

## 构造方法

### Node()

```java
public Node()
```

创建节点，子节点列表初始化为空列表。

```java
Node<Long, String> node = new Node<>();
node.setId(1L);
node.setName("根节点");
node.setType("ROOT");
```

## 实例方法

### addChild(Node<ID,TYPE> node)

```java
public void addChild(Node<ID,TYPE> node)
```

添加子节点，首次添加时自动将子节点列表从空列表初始化为 `ArrayList`。

```java
Node<Long, String> root = new Node<>();
root.setId(1L);
root.setName("根节点");

Node<Long, String> child = new Node<>();
child.setId(2L);
child.setName("子节点");
child.setParentId(1L);

root.addChild(child);
// root.children = [child]
```

## 使用示例

```java
// 构建菜单树
Node<Long, String> menu = new Node<>();
menu.setId(100L);
menu.setName("系统管理");
menu.setType("MENU");

Node<Long, String> subMenu = new Node<>();
subMenu.setId(101L);
subMenu.setName("用户管理");
subMenu.setType("MENU");
subMenu.setParentId(100L);

menu.addChild(subMenu);
```