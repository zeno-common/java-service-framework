# WafException

`io.soil.waf.exception.WafException`

WAF 异常类，继承自 `BaseException`，用于 Web 应用框架层的异常处理。支持 HTTP 状态码、自定义异常状态码、消息模板等多种构造方式，适用于 REST API 的异常响应场景。

## 类签名

```java
public class WafException extends BaseException
```

## 字段

| 字段 | 类型 | 说明 |
|------|------|------|
| status | `HttpStatus` | HTTP 状态码 |
| code | `String` | 自定义异常状态码 |

## 构造方法

### WafException(String msg)

```java
public WafException(String msg)
```

使用消息构造，默认 HTTP 500。

```java
throw new WafException("用户不存在");
```

### WafException(String msgPattern, Object... msgArgs)

```java
public WafException(String msgPattern, Object... msgArgs)
```

使用消息模板构造，默认 HTTP 500。

```java
throw new WafException("用户 {0} 不存在，状态: {1}", userId, status);
```

### WafException(HttpStatus status, String msg)

```java
public WafException(HttpStatus status, String msg)
```

使用 HTTP 状态码和消息构造，`code` 取状态码名称。

```java
throw new WafException(HttpStatus.NOT_FOUND, "资源不存在");
// code = "NOT_FOUND", status = 404
```

### WafException(HttpStatus status, String msgPattern, Object... msgArgs)

```java
public WafException(HttpStatus status, String msgPattern, Object... msgArgs)
```

使用 HTTP 状态码和消息模板构造。

```java
throw new WafException(HttpStatus.BAD_REQUEST, "参数 {0} 格式错误", paramName);
```

### WafException(String code, HttpStatus status, String msg)

```java
public WafException(String code, HttpStatus status, String msg)
```

使用自定义异常状态码和 HTTP 状态码构造。

```java
throw new WafException("USER_NOT_FOUND", HttpStatus.NOT_FOUND, "用户不存在");
```

### WafException(String code, HttpStatus status, String msgPattern, Object... msgArgs)

```java
public WafException(String code, HttpStatus status, String msgPattern, Object... msgArgs)
```

使用自定义异常状态码、HTTP 状态码和消息模板构造。

```java
throw new WafException("INVALID_PARAM", HttpStatus.BAD_REQUEST, "参数 {0} 不合法", paramName);
```

### WafException(Throwable throwable)

```java
public WafException(Throwable throwable)
```

使用原始异常构造，默认 HTTP 500，`code` 取 `"INTERNAL_SERVER_ERROR"`。

```java
try {
    // ...
} catch (IOException e) {
    throw new WafException(e);
}
```

### WafException(String code, Throwable throwable)

```java
public WafException(String code, Throwable throwable)
```

使用自定义异常状态码和原始异常构造，默认 HTTP 500。

```java
throw new WafException("FILE_ERROR", e);
```

### WafException(String code, Throwable throwable, String msgPattern, Object... msgArgs)

```java
public WafException(String code, Throwable throwable, String msgPattern, Object... msgArgs)
```

使用自定义异常状态码、原始异常和消息模板构造。

```java
throw new WafException("DB_ERROR", e, "数据库操作失败: {0}", operation);
```

### WafException(String code, HttpStatus status, Throwable throwable, String msgPattern, Object... msgArgs)

```java
public WafException(String code, HttpStatus status, Throwable throwable, String msgPattern, Object... msgArgs)
```

全参数构造。

```java
throw new WafException("ORDER_TIMEOUT", HttpStatus.REQUEST_TIMEOUT, e,
    "订单 {0} 处理超时", orderId);
```

## 方法

### module()

```java
public String module()
```

返回 `"GAEA-WAF"`。

### status()

```java
public HttpStatus status()
```

获取 HTTP 状态码。

### code()

```java
public String code()
```

获取自定义异常状态码。

## 典型用法

```java
// REST API 异常处理
@GetMapping("/users/{id}")
public User getUser(@PathVariable Long id) {
    User user = userService.findById(id);
    if (user == null) {
        throw new WafException("USER_NOT_FOUND", HttpStatus.NOT_FOUND,
            "用户 {0} 不存在", id);
    }
    return user;
}
```