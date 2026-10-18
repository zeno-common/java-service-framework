# IpUtil

> IP 工具类。

- **包**: `io.soil.jsf.util.ip`

## 方法

### getLocalConnectedIp

`static String getLocalConnectedIp(String ip, Integer port)`

> 获取本机连接指定远端地址时所使用的本地 IP（通过构造 `DatagramSocket` 连接判断，规避多网卡场景选错 IP）。

**参数**:
- `ip` (String) — 目标远端 IP
- `port` (Integer) — 目标远端端口

**返回**: String — 本地连接 IP
