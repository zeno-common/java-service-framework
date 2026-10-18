# JsfUrlParams

> JSF URL 参数工具类，从当前 HTTP 请求上下文提取分页、排序、偏移等 URL 参数，带默认值与数字校验。

- **包**: `io.soil.jsf.util.jdbc`
- **默认值**: pageSize=10、pageNo=1、pageSize 上限=500、offset=0、limit=10

参数名取自 [JsfUrlParamConst](#JsfUrlParamConst)。非数字参数值视为不存在返回默认值。

## 方法

### offset

`static Integer offset()` — 偏移量，默认 0
`static Integer offset(Integer defaultOffset)` — 指定默认值

### limit

`static Integer limit()` — 限制条数，默认 10
`static Integer limit(Integer defaultLimit)` — 指定默认值

### pageSize

`static Integer pageSize()` — 每页条数，默认 10
`static Integer pageSize(Integer defaultSize)` — 指定默认值，超过 500 自动截断为 500

### pageNo

`static Integer pageNo()` — 当前页码，默认 1
`static Integer pageNo(Integer defaultPageNo)` — 指定默认值

### getOffsetId

`static Long getOffsetId()`

> 获取偏移 ID（`$offsetId`），用于轮询查询；不存在返回 null。

### getOffsetTime

`static String getOffsetTime()`

> 获取偏移时间（`$offsetTime`），用于轮询查询；不存在返回 null。

### order

`static String order()`

> 获取排序参数（`$order`）；不存在返回 null。

### getInteger

`static Integer getInteger(String paramName, Integer defaultValue)`

> 取指定参数名的整数值，不存在或非数字时返回默认值。

### getString

`static String getString(String paramName)`

> 取指定参数名的原始字符串值（从 `RequestContextHolder` 当前请求读取）。

**返回**: String

- **相关**: [JsfPagingUtil](#JsfPagingUtil)、[JsfUrlParamConst](#JsfUrlParamConst)
