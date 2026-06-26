# ProbeController

> 健康探针控制器，提供应用活跃状态检测接口

- **包**: io.soil.waf.controller
- **注解**: `@RestController`, `@RequestMapping("/v1/probes")`

## 方法

### activeness

`String activeness()`

> 活跃探针，返回应用活跃状态

**映射**: `GET /v1/probes/activeness`

**返回**: `String` — 固定返回 `"active"`

**示例**:
```bash
curl http://localhost:8080/v1/probes/activeness
# 响应: active
```
