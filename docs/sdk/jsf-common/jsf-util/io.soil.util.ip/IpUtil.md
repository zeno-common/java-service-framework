# `IpUtil`

`package io.soil.util.ip`

本地连接IP工具类，用于获取与指定远程IP端口连接的本地IP地址

---

## Methods

### `static String getLocalConnectedIp(String remoteIp, Integer remotePort)`

→ **String** — 本地连接IP地址（字符串形式），连接失败时返回null

获取本地与指定远程IP端口连接时使用的IP地址

throws `IllegalArgumentException` — 当远程IP格式无效或端口超出范围时抛出

| Param | Type | Description |
|-------|------|-------------|
| `remoteIp` | `String` | 远程IP地址（如："192.168.1.100"） |
| `remotePort` | `Integer` | 远程端口（如：8080） |