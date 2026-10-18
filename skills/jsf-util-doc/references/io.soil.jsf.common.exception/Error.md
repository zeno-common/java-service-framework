# Error

> 异常编码和异常信息模板定义接口，业务错误枚举实现此接口以配合 BizException / SysException / ParamException 使用。

- **包**: `io.soil.jsf.common.exception`
- **类型**: `interface`

## 方法

### code

`String code()`

> 异常码。

**返回**: String

### desc

`String desc()`

> 异常描述模板，支持 `MessageFormat` 占位符（如 `"订单 {0} 不存在"`）。

**返回**: String

**示例**:
```java
public enum OrderError implements Error {
    NOT_FOUND("ORDER_404", "订单 {0} 不存在");
    // code(), desc() 由字段实现
}
```
