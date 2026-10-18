# BaseException

> 业务异常基类（抽象）。

- **包**: `io.soil.jsf.common.exception`
- **修饰**: `abstract`，extends `RuntimeException`
- **构造**: 均为 `protected`（`(String code, String msg)`、`(String code, String msgPattern, Object... msgArgs)`、`(String code, Throwable)`、`(String code, Throwable, String msg)`、`(String code, Throwable, String msgPattern, Object... msgArgs)`）

消息支持 `MessageFormat` 模板（`{0}`、`{1}` 占位符）。子类需实现 `type()` 返回异常分类。

## 方法

### type

`abstract ExceptionType type()`

> 异常类型分类（由子类实现）。

### code

`String code()`

> 异常码。

**返回**: String

### getStackTraceString

`String getStackTraceString()`
`static String getStackTraceString(Throwable e)`

> 将异常栈转为字符串（`PrintWriter` + `StringWriter`）。实例方法委托静态方法。

**参数**: `e` (Throwable) — 异常对象
**返回**: String — 栈信息字符串

- **相关**: [BizException](#BizException)、`SysException`、`ParamException`、[ExceptionType](#ExceptionType)
