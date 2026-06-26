# RemoteAddrUtil

> 远程地址工具类，用于获取客户端真实 IP 地址

- **包**: io.soil.waf.util

## 方法

### getRealClientIp

`static String getRealClientIp()`

> 获取客户端真实 IP 地址，处理代理、负载均衡等场景

**返回**: `String` — 客户端真实 IP

**解析顺序**:

| 优先级 | Header | 说明 |
|--------|--------|------|
| 1 | `X-Real-IP` | Nginx 等代理常用 |
| 2 | `X-Forwarded-For` | 多代理时取第一个 IP（逗号分隔） |
| 3 | `request.getRemoteAddr()` | 兜底 |

## 示例

```java
String clientIp = RemoteAddrUtil.getRealClientIp();
// 从当前请求上下文自动获取客户端 IP
// 优先读取 X-Real-IP / X-Forwarded-For 头
// 无法获取时回退到 request.getRemoteAddr()
```
