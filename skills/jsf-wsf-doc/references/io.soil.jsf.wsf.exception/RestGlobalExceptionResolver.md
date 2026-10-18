# RestGlobalExceptionResolver

> 全局异常处理器，捕获 Web 层异常并统一返回 `RestExceptionResponse` JSON。

- **包**: `io.soil.jsf.wsf.exception`

由 `@RestControllerAdvice` 自动注册为全局异常处理器，无需业务侧配置。

## 方法

### handleValidationException (ParamException)

`ResponseEntity<RestExceptionResponse> handleValidationException(ParamException e)`

> 处理 Adapter 层手动抛出的参数校验异常。

**参数**:
- `e` (ParamException) — 参数校验异常

**返回**: HTTP 400 + RestExceptionResponse（errType=PARAM, errCode=e.code()）

### handleValidationException (MethodArgumentNotValidException)

`ResponseEntity<RestExceptionResponse> handleValidationException(MethodArgumentNotValidException e)`

> 处理 `@Valid` / `@Validated` 校验失败，聚合字段级错误消息（`;` 分隔）。

**参数**:
- `e` (MethodArgumentNotValidException) — Bean 校验异常

**返回**: HTTP 400 + RestExceptionResponse（errType=PARAM, errCode=BAD_REQUEST）

### handleBaseException (WebBizException)

`ResponseEntity<RestExceptionResponse> handleBaseException(WebBizException e)`

> 处理 Web 业务异常，HTTP 状态码由异常自带 `status()` 决定。

**参数**:
- `e` (WebBizException) — Web 业务异常

**返回**: 状态码=e.status() + RestExceptionResponse

### handleBaseException (BaseException)

`ResponseEntity<RestExceptionResponse> handleBaseException(BaseException e)`

> 处理通用业务异常，HTTP 500。

**参数**:
- `e` (BaseException) — 通用业务异常

**返回**: HTTP 500 + RestExceptionResponse

### handleBaseException (Throwable)

`ResponseEntity<RestExceptionResponse> handleBaseException(Throwable throwable)`

> 兜底处理所有未捕获异常，HTTP 500。

**参数**:
- `throwable` (Throwable) — 原始异常

**返回**: HTTP 500 + RestExceptionResponse
