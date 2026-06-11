# FunctionWithParam

`io.soil.common.collection.FunctionWithParam<S, R, P>`

带一个额外参数的函数式接口，扩展了标准 `Function` 以支持额外的参数传递。

## 接口签名

```java
@FunctionalInterface
public interface FunctionWithParam<S, R, P>
```

## 类型参数

| 参数 | 说明 |
|------|------|
| S | 源类型 |
| R | 返回类型 |
| P | 额外参数类型 |

## 方法

### apply(S s, P p)

```java
R apply(S s, P p)
```

应用函数，接收源对象和一个额外参数，返回结果。

```java
FunctionWithParam<String, Integer, Integer> lengthWithOffset =
    (str, offset) -> str.length() + offset;

int result = lengthWithOffset.apply("hello", 10);
// result = 15
```

## 配合 CollectionUtil 使用

```java
List<String> names = List.of("Alice", "Bob");
List<String> prefixed = CollectionUtil.mapList(names,
    (name, prefix) -> prefix + name,
    "User: "
);
// ["User: Alice", "User: Bob"]
```