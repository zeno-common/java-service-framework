# FunctionWith3Param

`io.soil.common.collection.FunctionWith3Param<S, R, P1, P2, P3>`

带三个额外参数的函数式接口，扩展了标准 `Function` 以支持三个额外参数的传递。

## 接口签名

```java
@FunctionalInterface
public interface FunctionWith3Param<S, R, P1, P2, P3>
```

## 类型参数

| 参数 | 说明 |
|------|------|
| S | 源类型 |
| R | 返回类型 |
| P1 | 第一个额外参数类型 |
| P2 | 第二个额外参数类型 |
| P3 | 第三个额外参数类型 |

## 方法

### apply(S s, P1 p1, P2 p2, P3 p3)

```java
R apply(S s, P1 p1, P2 p2, P3 p3)
```

应用函数，接收源对象和三个额外参数，返回结果。

```java
FunctionWith3Param<String, String, String, String, Integer> customFormat =
    (str, prefix, suffix, repeat) -> prefix + str.repeat(repeat) + suffix;

String result = customFormat.apply("ha", "(", ")", 3);
// result = "(hahaha)"
```