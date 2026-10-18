# BizException

> 业务异常，`type()` 固定为 `BIZ`。

- **包**: `io.soil.jsf.common.exception`
- **父类**: [BaseException](#BaseException)

## 方法

### of

`static BizException of(Error error, Object... descArgs)`
`static BizException of(Throwable cause, Error error, Object... descArgs)`

> 基于 [Error](#Error) 错误码枚举构造业务异常，`desc()` 作为消息模板以 `descArgs` 填充。

**参数**:
- `error` (Error) — 提供 code 与 desc 模板的错误枚举
- `descArgs` (Object...) — 描述模板参数
- `cause` (Throwable) — 原始异常

**返回**: BizException

**示例**:
```java
throw BizException.of(OrderError.NOT_FOUND, orderId);
```

### type

`ExceptionType type()`

> 固定返回 `BIZ`。
