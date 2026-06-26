# WafHttpExceptionResolver

> WAF 全局异常处理器，自动捕获并处理 Web 层异常

- **包**: io.soil.waf.exception
- **注解**: `@RestControllerAdvice`, `@Slf4j`

## 方法

### handleWebException (WafException)

`ResponseEntity<WafHttpExceptionResponse> handleWebException(WafException e)`

> 处理 WAF 自定义异常

**参数**:
- `e` (`WafException`) — WAF 异常对象

**返回**: `ResponseEntity<WafHttpExceptionResponse>` — 包含异常信息的 HTTP 响应，状态码由异常对象决定

### handleWebException (Throwable)

`ResponseEntity<WafHttpExceptionResponse> handleWebException(Throwable throwable)`

> 处理所有未捕获的异常

**参数**:
- `throwable` (`Throwable`) — 原始异常对象

**返回**: `ResponseEntity<WafHttpExceptionResponse>` — 包含异常信息的 HTTP 响应，状态码固定为 500

## 示例

```java
// WafHttpExceptionResolver 通过 @RestControllerAdvice 自动注册
// 抛出 WafException 时，自动返回 WafHttpExceptionResponse 格式 JSON
// 状态码由 WafException.getStatus() 决定

// 抛出其他未捕获异常时，自动返回 HTTP 500 + WafHttpExceptionResponse
```
