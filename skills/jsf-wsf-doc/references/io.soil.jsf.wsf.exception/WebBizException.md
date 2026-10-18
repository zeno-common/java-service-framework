# WebBizException

> 可指定 HTTP 状态码的业务异常，`final` 不可继承。

- **包**: `io.soil.jsf.wsf.exception`
- **父类**: `io.soil.jsf.common.exception.BizException`（`type()` 固定返回 `BIZ`）

在业务异常基础上额外持有 `HttpStatus`，由 `RestGlobalExceptionResolver` 按自带状态码返回响应；默认 HTTP 状态码 500。推荐用静态工厂方法构造，避免硬编码错误码/描述。

## 构造

| 签名 | 说明 |
|------|------|
| `WebBizException(HttpStatus status, String code, String msg)` | HTTP 状态码 + 异常码 + 消息 |
| `WebBizException(HttpStatus status, String code, String msgPattern, Object... msgArgs)` | 消息模板（MessageFormat） |
| `WebBizException(HttpStatus status, String code, Throwable throwable)` | 包装原始异常 |
| `WebBizException(HttpStatus status, String code, Throwable throwable, String msg)` | HTTP + 码 + 原因 + 消息 |
| `WebBizException(HttpStatus status, String code, Throwable throwable, String msgPattern, Object... msgArgs)` | 全参数 |

## 方法

### of

`static WebBizException of(HttpStatus status, Error error, Object... descArgs)`
`static WebBizException of(HttpStatus status, Throwable cause, Error error, Object... descArgs)`
`static WebBizException of(HttpStatus status, BizException e)`

> 基于 `Error` 错误码枚举构造（`code()`/`desc()` 作为异常码与消息模板，支持 MessageFormat 占位符）；或以 HTTP 状态码重新包装已有 BizException（保留原始码与消息）。

**参数**:
- `status` (HttpStatus) — HTTP 状态码
- `error` (Error) — 提供 code 与 desc 模板的错误枚举
- `e` (BizException) — 待包装的业务异常

**返回**: WebBizException

### status

`HttpStatus status()`

> 获取 HTTP 状态码。

**返回**: HttpStatus

**示例**:
```java
throw WebBizException.of(HttpStatus.NOT_FOUND, OrderError.NOT_FOUND, orderId);
```

- **相关**: [RestExceptionResponse](#RestExceptionResponse)、[RestGlobalExceptionResolver](#RestGlobalExceptionResolver)
