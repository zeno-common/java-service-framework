# RemoteAddrUtil

`io.soil.waf.util.RemoteAddrUtil`

远程地址工具类，用于获取客户端真实 IP 地址。处理代理、负载均衡等场景下的 IP 获取，支持 `X-Real-IP` 和 `X-Forwarded-For` 头。

## 类签名

```java
public class RemoteAddrUtil
```

## 静态方法

### getRealClientIp()

```java
public static String getRealClientIp()
```

获取客户端真实 IP 地址。从当前 HTTP 请求上下文中获取，按优先级依次检查：

1. `X-Real-IP` 头（Nginx 等代理常用）
2. `X-Forwarded-For` 头（多代理时取第一个 IP）
3. `request.getRemoteAddr()` 兜底

```java
// 在 Controller 或 Service 中
String clientIp = RemoteAddrUtil.getRealClientIp();

// Nginx 代理场景: X-Real-IP: 192.168.1.50
// 返回 "192.168.1.50"

// 多层代理场景: X-Forwarded-For: 192.168.1.50, 10.0.0.1, 10.0.0.2
// 返回 "192.168.1.50"（取第一个 IP）

// 无代理场景
// 返回 request.getRemoteAddr()
```

## 注意事项

- 本方法依赖 Spring 的 `RequestContextHolder`，必须在 HTTP 请求线程中调用
- `X-Forwarded-For` 可被客户端伪造，在安全敏感场景需结合其他验证手段