# FunctionWith2Param

`io.soil.common.collection.FunctionWith2Param<S, R, P1, P2>`

带两个额外参数的函数式接口，扩展了标准 `Function` 以支持两个额外参数的传递。

## 接口签名

```java
@FunctionalInterface
public interface FunctionWith2Param<S, R, P1, P2>
```

## 类型参数

| 参数 | 说明 |
|------|------|
| S | 源类型 |
| R | 返回类型 |
| P1 | 第一个额外参数类型 |
| P2 | 第二个额外参数类型 |

## 方法

### apply(S s, P1 p1, P2 p2)

```java
R apply(S s, P1 p1, P2 p2)
```

应用函数，接收源对象和两个额外参数，返回结果。

```java
FunctionWith2Param<String, String, String, Integer> formatWithPrefixAndRepeat =
    (str, prefix, times) -> prefix.repeat(times) + str;

String result = formatWithPrefixAndRepeat.apply("hello", "->", 2);
// result = "->->hello"
```