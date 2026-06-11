# JsfUrlParamConst

`io.soil.util.jdbc.JsfUrlParamConst`

JSF URL 参数常量类，定义 REST API 规约中分页、排序、偏移等 URL 参数名称。

## 类签名

```java
public class JsfUrlParamConst
```

## 常量

| 常量 | 值 | 说明 |
|------|----|------|
| OFFSET_ID | `"$offsetId"` | 大于 ID 的查询参数名，用于轮询 |
| OFFSET_TIME | `"$offsetTime"` | 大于时间的查询参数名，用于轮询 |
| ORDER | `"$order"` | 排序参数名，多列排序使用逗号分隔，列名后跟随排序顺序（asc/desc） |
| PAGE_NO | `"$pageNo"` | 分页页码参数名 |
| PAGE_SIZE | `"$pageSize"` | 分页条目数参数名 |
| OFFSET | `"$offset"` | 偏移量参数名 |
| LIMIT | `"$limit"` | 偏移限制量参数名 |

## URL 参数使用示例

```
GET /api/users?$pageNo=2&$pageSize=20&$order=name asc,create_time desc
GET /api/logs?$offset=100&$limit=50&$offsetId=12345&$offsetTime=2025-06-11T00:00:00+08:00
```