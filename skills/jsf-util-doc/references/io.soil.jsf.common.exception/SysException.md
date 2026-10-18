# SysException

> 系统异常，`type()` 固定为 `SYS`，`final` 不可继承。

- **包**: `io.soil.jsf.common.exception`
- **父类**: [BaseException](#BaseException)

## 方法

### of

`static SysException of(Error error, Object... descArgs)`
`static SysException of(Throwable cause, Error error, Object... descArgs)`

> 基于 [Error](#Error) 错误码枚举构造系统异常。

**参数**:
- `error` (Error) — 错误码枚举
- `descArgs` (Object...) — 描述模板参数
- `cause` (Throwable) — 原始异常

**返回**: SysException

### type

`ExceptionType type()`

> 固定返回 `SYS`。
