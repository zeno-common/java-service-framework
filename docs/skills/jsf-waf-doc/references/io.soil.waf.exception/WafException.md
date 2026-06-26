# WafException

> WAF 异常类，继承 BaseException，支持 HTTP 状态码和自定义错误码

- **包**: io.soil.waf.exception
- **父类**: `BaseException`

## 字段

| 字段 | 类型 | 说明 |
|------|------|------|
| `status` | `HttpStatus` | HTTP 状态码 |
| `code` | `String` | 自定义异常状态码 |

## 构造

| 签名 | 说明 |
|------|------|
| `WafException(String msg)` | 使用消息构造，默认 HTTP 500 |
| `WafException(String msgPattern, Object... msgArgs)` | 使用消息模板构造，默认 HTTP 500 |
| `WafException(HttpStatus status, String msg)` | 使用 HTTP 状态码和消息构造 |
| `WafException(HttpStatus status, String msgPattern, Object... msgArgs)` | 使用 HTTP 状态码和消息模板构造 |
| `WafException(String code, HttpStatus status, String msg)` | 使用自定义错误码、HTTP 状态码和消息构造 |
| `WafException(String code, HttpStatus status, String msgPattern, Object... msgArgs)` | 使用自定义错误码、HTTP 状态码和消息模板构造 |
| `WafException(Throwable throwable)` | 使用异常栈构造，默认 HTTP 500 |
| `WafException(String code, Throwable throwable)` | 使用自定义错误码和异常栈构造 |
| `WafException(String code, Throwable throwable, String msgPattern, Object... msgArgs)` | 使用自定义错误码、异常栈和消息模板构造 |
| `WafException(String code, HttpStatus status, Throwable throwable, String msgPattern, Object... msgArgs)` | 全参数构造 |

## 方法

### module

`String module()`

> 获取异常所属模块名称

**返回**: `String` — 固定返回 `"JSF-WAF"`

### status

`HttpStatus status()`

> 获取 HTTP 状态码

**返回**: `HttpStatus` — HTTP 状态码

### code

`String code()`

> 获取自定义异常状态码

**返回**: `String` — 异常状态码

## 示例

```java
// 简单消息异常（HTTP 500）
throw new WafException("操作失败");

// 指定 HTTP 状态码
throw new WafException(HttpStatus.BAD_REQUEST, "参数错误");

// 自定义错误码 + HTTP 状态码
throw new WafException("USER_NOT_FOUND", HttpStatus.NOT_FOUND, "用户不存在");

// 消息模板
throw new WafException(HttpStatus.BAD_REQUEST, "参数 {0} 不能为空", "name");

// 包装原始异常
throw new WafException("DB_ERROR", throwable);
```
