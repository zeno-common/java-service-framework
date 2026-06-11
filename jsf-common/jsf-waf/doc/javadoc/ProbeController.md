# ProbeController

`io.soil.waf.controller.ProbeController`

健康探针控制器，提供应用活跃状态检测接口。用于 Kubernetes 等容器编排系统的存活探针检测。

## 类签名

```java
@RestController
@RequestMapping("/v1/probes")
public class ProbeController
```

## 接口

### GET /v1/probes/activeness

活跃探针，返回应用活跃状态。

**请求**：

```
GET /v1/probes/activeness
```

**响应**：

```
HTTP 200
Content-Type: text/plain

active
```

## 使用场景

在 Kubernetes 中配置存活探针：

```yaml
livenessProbe:
  httpGet:
    path: /v1/probes/activeness
    port: 8080
  initialDelaySeconds: 30
  periodSeconds: 10
```