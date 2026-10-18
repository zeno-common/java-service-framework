# RemoteAddrUtil

> 远程地址工具类，获取客户端真实 IP（处理代理、负载均衡场景）。

- **包**: `io.soil.jsf.wsf.util`

## 方法

### getRealClientIp

`static String getRealClientIp()`

> 依次检查 `X-Real-IP`、`X-Forwarded-For` 头（多 IP 取第一个），均无效时兜底 `getRemoteAddr()`。依赖当前请求上下文（`RequestContextHolder`），仅在 Web 请求线程内有效。

**返回**: String — 客户端真实 IP

**示例**:
```java
String ip = RemoteAddrUtil.getRealClientIp();
```
