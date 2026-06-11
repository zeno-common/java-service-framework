# EnumSample

`io.soil.common.constant.EnumSample`

枚举事件示例代码，主要用于枚举定义的参考模板。展示了 JSF 框架中枚举类的标准定义模式：通过 `status` 字段关联枚举值，并提供 `of()` 方法根据状态值反查枚举。

## 类签名

```java
public enum EnumSample
```

## 枚举常量

| 常量 | status 值 |
|------|-----------|
| SAMPLE | 0 |

## 字段

| 字段 | 类型 | 说明 |
|------|------|------|
| status | `int` | 枚举状态值 |

## 方法

### status()

```java
public int status()
```

获取枚举状态值。

```java
int val = EnumSample.SAMPLE.status();
// val = 0
```

### of(int status)

```java
public EnumSample of(int status)
```

根据状态值获取枚举对象。无法匹配时抛出 `UnknownEnumException`。

```java
EnumSample e = EnumSample.SAMPLE.of(0);
// e = EnumSample.SAMPLE

EnumSample notFound = EnumSample.SAMPLE.of(999);
// throws UnknownEnumException: 未知的 io.soil.common.constant.EnumSample 枚举状态:999
```

## 参考模式

自定义枚举时应遵循此模式：

```java
public enum OrderStatus {
    PENDING(0),
    CONFIRMED(1),
    SHIPPED(2);

    private final int status;

    OrderStatus(int status) {
        this.status = status;
    }

    public int status() {
        return status;
    }

    public OrderStatus of(int status) {
        for (OrderStatus e : OrderStatus.values()) {
            if (e.status == status) {
                return e;
            }
        }
        throw new UnknownEnumException(status, OrderStatus.class);
    }
}
```