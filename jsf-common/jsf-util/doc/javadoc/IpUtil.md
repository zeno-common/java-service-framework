# IpUtil

`io.soil.util.ip.IpUtil`

本地连接 IP 工具类，用于获取与指定远程 IP 端口连接时使用的本地 IP 地址。

## 类签名

```java
public class IpUtil
```

## 静态方法

### getLocalConnectedIp(String remoteIp, Integer remotePort)

```java
public static String getLocalConnectedIp(String remoteIp, Integer remotePort)
```

获取本地与指定远程 IP 端口连接时使用的本地 IP 地址。通过创建一个到远程地址的临时 Socket 连接来获取本地绑定的 IP。

| 参数 | 说明 |
|------|------|
| remoteIp | 远程 IP 地址（如 `"192.168.1.100"`） |
| remotePort | 远程端口（如 `8080`） |
| 返回值 | 本地连接 IP 地址字符串，连接失败时返回 `"localhost"` |

**异常**：当远程 IP 格式无效或端口超出范围时抛出 `IllegalArgumentException`。

**超时**：连接超时为 3 秒。

```java
// 获取本机访问目标服务时使用的 IP
String localIp = IpUtil.getLocalConnectedIp("192.168.1.100", 8080);
// "192.168.1.50"

// 无效 IP
IpUtil.getLocalConnectedIp("invalid-ip", 8080);
// throws IllegalArgumentException: 无效的远程IP地址: invalid-ip

// 端口超出范围
IpUtil.getLocalConnectedIp("192.168.1.100", 99999);
// throws IllegalArgumentException: 端口号超出范围（0-65535）: 99999
```