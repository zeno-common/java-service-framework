# UnknownEnumException

`io.soil.common.exception.UnknownEnumException`

未知的枚举类型异常，当根据状态值无法匹配到对应的枚举时抛出。继承自 `BaseException`。

## 类签名

```java
public class UnknownEnumException extends BaseException
```

## 构造方法

### UnknownEnumException(Object status, Class<? extends Enum> enumClazz)

```java
public UnknownEnumException(Object status, Class<? extends Enum> enumClazz)
```

构造未知枚举异常，消息模板为 `"未知的 {0} 枚举状态:{1}"`。

| 参数 | 说明 |
|------|------|
| status | 无法匹配的状态值 |
| enumClazz | 枚举类对象 |

```java
throw new UnknownEnumException(999, OrderStatus.class);
// 异常消息: "未知的 com.example.OrderStatus 枚举状态:999"
```

## 方法

### module()

```java
protected String module()
```

返回 `"JSF"`。

## 典型用法

在枚举的 `of()` 工厂方法中使用：

```java
public enum OrderStatus {
    PENDING(0), CONFIRMED(1);

    private final int status;

    OrderStatus(int status) { this.status = status; }

    public int status() { return status; }

    public static OrderStatus of(int status) {
        for (OrderStatus e : values()) {
            if (e.status == status) return e;
        }
        throw new UnknownEnumException(status, OrderStatus.class);
    }
}
```