# WafExceptionResponseVO

`io.soil.waf.exception.WafExceptionResponseDTO`

WAF 异常响应内容 VO，用于封装异常信息返回给客户端。

## 类签名

```java
@Data
public class WafExceptionResponseVO
```

## 字段

| 字段 | 类型 | 说明 |
|------|------|------|
| module | `String` | 异常模块名称 |
| code | `String` | 异常状态码 |
| msg | `String` | 异常消息 |
| trace | `String` | 异常栈信息 |

## 静态工厂方法

### createFrom(Throwable ex)

```java
public static WafExceptionResponseVO createFrom(Throwable ex)
```

根据异常对象创建异常响应 VO。

- `module` 默认为 `"UNDEFINED"`
- `code` 默认为 `HttpStatus.INTERNAL_SERVER_ERROR.name()`（`"INTERNAL_SERVER_ERROR"`）
- `msg` 取自异常的 `getMessage()`
- `trace` 取自 `WafException.getStackTraceString(ex)`（仅在 DEBUG 级别时有内容）

```java
try {
    // 业务逻辑
} catch (Exception e) {
    WafExceptionResponseVO response = WafExceptionResponseVO.createFrom(e);
    // response.module = "UNDEFINED"
    // response.code = "INTERNAL_SERVER_ERROR"
    // response.msg = e.getMessage()
    // response.trace = "" (非 DEBUG 模式)
}
```

## 典型用法

配合全局异常处理器使用：

```java
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(WafException.class)
    public ResponseEntity<WafExceptionResponseVO> handleWafException(WafException ex) {
        WafExceptionResponseVO vo = new WafExceptionResponseVO();
        vo.setModule(ex.module());
        vo.setCode(ex.code());
        vo.setMsg(ex.getMessage());
        vo.setTrace(ex.getStackTraceString());
        return ResponseEntity.status(ex.status()).body(vo);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<WafExceptionResponseVO> handleException(Exception ex) {
        WafExceptionResponseVO vo = WafExceptionResponseVO.createFrom(ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(vo);
    }
}
```