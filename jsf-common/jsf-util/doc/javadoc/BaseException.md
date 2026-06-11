# BaseException

`io.soil.common.exception.BaseException`

异常基类，用于制定通用的异常信息模板，方便快速定义异常类和易于实例化异常类对象。支持消息模板（`MessageFormat` 格式）和异常链。

## 类签名

```java
public abstract class BaseException extends RuntimeException
```

## 构造方法

### BaseException(String msg)

```java
protected BaseException(String msg)
```

使用固定消息构造异常。

```java
throw new MyException("用户不存在");
```

### BaseException(String msgPattern, Object... msgArgs)

```java
protected BaseException(String msgPattern, Object... msgArgs)
```

使用 `MessageFormat` 消息模板构造异常。

```java
throw new MyException("用户 {0} 不存在，状态: {1}", userId, status);
```

### BaseException(Throwable throwable, String msg)

```java
protected BaseException(Throwable throwable, String msg)
```

使用原始异常和固定消息构造异常。

```java
try {
    // ...
} catch (IOException e) {
    throw new MyException(e, "文件读取失败");
}
```

### BaseException(Throwable throwable)

```java
protected BaseException(Throwable throwable)
```

使用原始异常构造，消息取自原始异常的 `getMessage()`。

```java
try {
    // ...
} catch (SQLException e) {
    throw new MyException(e);
}
```

### BaseException(Throwable throwable, String msgPattern, Object... msgArgs)

```java
protected BaseException(Throwable throwable, String msgPattern, Object... msgArgs)
```

使用原始异常和消息模板构造异常。

```java
try {
    // ...
} catch (IOException e) {
    throw new MyException(e, "文件 {0} 读取失败，错误码: {1}", fileName, errorCode);
}
```

## 抽象方法

### module()

```java
protected abstract String module()
```

获取异常模块名称，子类必须实现。

```java
@Override
protected String module() {
    return "ORDER";
}
```

## 实例方法

### getStackTraceString()

```java
public String getStackTraceString()
```

获取当前异常的栈字符串。仅在 `DEBUG` 日志级别时返回内容，否则返回空字符串。

```java
WafException ex = new WafException("test");
String trace = ex.getStackTraceString();
```

## 静态方法

### getStackTraceString(Throwable)

```java
public static String getStackTraceString(Throwable throwable)
```

获取指定异常的栈字符串。仅在 `DEBUG` 日志级别时返回内容，否则返回空字符串。

```java
String trace = BaseException.getStackTraceString(someException);
```

## 继承示例

```java
public class OrderException extends BaseException {

    public OrderException(String msg) {
        super(msg);
    }

    public OrderException(String msgPattern, Object... msgArgs) {
        super(msgPattern, msgArgs);
    }

    public OrderException(Throwable throwable, String msgPattern, Object... msgArgs) {
        super(throwable, msgPattern, msgArgs);
    }

    @Override
    protected String module() {
        return "ORDER";
    }
}
```