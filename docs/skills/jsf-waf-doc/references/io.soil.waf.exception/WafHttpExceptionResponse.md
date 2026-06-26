# WafHttpExceptionResponse

> WAF 异常响应内容，用于封装异常信息返回给客户端

- **包**: io.soil.waf.exception
- **Lombok**: `@Data` — 所有字段均有 getter/setter [Lombok 生成]

## 字段

| 字段 | 类型 | 说明 |
|------|------|------|
| `module` | `String` | 异常模块名称 |
| `errCode` | `String` | 异常状态码 |
| `errDesc` | `String` | 异常消息 |
| `trace` | `String` | 异常栈信息 |

## 方法

### createFrom

`static WafHttpExceptionResponse createFrom(Throwable ex)`

> 根据异常对象创建异常响应 VO

**参数**:
- `ex` (`Throwable`) — 异常对象

**返回**: `WafHttpExceptionResponse` — 异常响应 VO

**默认值**: `module` = `"UNDEFINED"`, `errCode` = `HttpStatus.INTERNAL_SERVER_ERROR.name()`, `errDesc` = `ex.getMessage()`

## 示例

```java
WafHttpExceptionResponse response = WafHttpExceptionResponse.createFrom(exception);
// response.getModule()   -> "UNDEFINED"
// response.getErrCode()  -> "INTERNAL_SERVER_ERROR"
// response.getErrDesc()  -> exception.getMessage()
// response.getTrace()    -> 堆栈字符串
```
