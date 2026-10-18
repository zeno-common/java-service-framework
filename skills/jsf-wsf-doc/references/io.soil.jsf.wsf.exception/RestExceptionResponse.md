# RestExceptionResponse

> 全局统一异常响应体，封装异常信息返回给客户端。

- **包**: `io.soil.jsf.wsf.exception`

## 方法

### createFrom

`static RestExceptionResponse createFrom(Throwable ex)`
`static RestExceptionResponse createFrom(WebBizException ex)`
`static RestExceptionResponse createFrom(BaseException ex)`

> 从异常对象构建响应体，`trace` 均无条件填充为栈字符串。差异在 `errType`/`errCode`：
> - `Throwable` 重载：errType=SYS、errCode=INTERNAL_SERVER_ERROR；
> - `WebBizException` 重载：errType=ex.type()（BIZ）、errCode=INTERNAL_SERVER_ERROR；
> - `BaseException` 重载：errType=ex.type()、errCode=ex.code()（取异常自身码）。

**参数**:
- `ex` (Throwable / WebBizException / BaseException) — 异常对象

**返回**: RestExceptionResponse — 异常响应体

**示例**:
```java
RestExceptionResponse resp = RestExceptionResponse.createFrom(ex);
```

## 字段

| 字段 | 类型 | 说明 |
|------|------|------|
| `errType` | ExceptionType | 异常类型（BIZ / SYS / PARAM） |
| `errCode` | String | 异常状态码 |
| `errDesc` | String | 异常消息 |
| `trace` | String | 异常栈信息（始终非空） |

> 所有字段均有 getter/setter [Lombok `@Data` 生成]。

- **相关**: [WebBizException](#WebBizException)、`io.soil.jsf.common.exception.BaseException`
