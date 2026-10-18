# JsfUrlParamConst

> JSF URL 参数常量类，定义 REST API 规约中分页、排序、偏移等参数名。

- **包**: `io.soil.jsf.util.jdbc`

## 常量

| 常量 | 值 | 说明 |
|------|----|------|
| `OFFSET_ID` | `$offsetId` | 大于 ID 的查询参数名，用于轮询 |
| `OFFSET_TIME` | `$offsetTime` | 大于时间的查询参数名，用于轮询 |
| `ORDER` | `$order` | 排序参数名，多列逗号分隔，列名后跟 asc/desc（如 `name asc,update_time desc`） |
| `PAGE_NO` | `$pageNo` | 分页页码参数名 |
| `PAGE_SIZE` | `$pageSize` | 分页条目数参数名 |
| `OFFSET` | `$offset` | 偏移量参数名 |
| `LIMIT` | `$limit` | 偏移限制量参数名 |

- **相关**: [JsfUrlParams](#JsfUrlParams)、[JsfPagingUtil](#JsfPagingUtil)
