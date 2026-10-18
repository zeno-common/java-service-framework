# ProbeController

> 健康探针控制器，提供应用活跃状态检测接口。

- **包**: `io.soil.jsf.wsf.controller`

## 方法

### activeness

`String activeness()`

> 活跃探针，映射 `GET /v1/probes/activeness`，供容器/负载均衡做存活检测。

**返回**: String — 固定 `"active"`

**示例**:
```java
// GET /v1/probes/activeness  ->  "active"
```
