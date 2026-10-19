---
name: "jsf-wsf-doc"
description: "Use when coding with jsf-wsf SDK, need API reference for global exception handling, Jackson serialization config, health probe and client IP utilities."
version: "jsf.0.2.1"
---

# jsf-wsf SDK 参考

基于 Spring Boot 封装的 Web Service Framework，提供全局异常处理、Jackson 序列化配置、健康探针与客户端 IP 工具，引入即生效。

## BOM 引入示例

```xml
<!-- 1. 导入 jsf-dependencies 聚合 BOM，统一管理版本 -->
<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>io.soil.jsf</groupId>
            <artifactId>jsf-dependencies</artifactId>
            <version>jsf.0.2.1</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>

<!-- 2. 声明依赖（版本由 BOM 管理，省略 <version>） -->
<dependencies>
    <dependency>
        <groupId>io.soil.jsf</groupId>
        <artifactId>jsf-wsf</artifactId>
    </dependency>
</dependencies>
```

> 启动项目也可直接继承 `io.soil.jsf:jsf-parent:jsf.0.2.1` 作为 `<parent>`，自动获得全部依赖版本管理。

## 类索引

| 类 | 包 | 说明 | 文档 |
|----|-----|------|------|
| `WsfConfig` | `io.soil.jsf.wsf.config` | Jackson 序列化配置 + 组件扫描 | [WsfConfig](references/io.soil.jsf.wsf.config/WsfConfig.md) |
| `ProbeController` | `io.soil.jsf.wsf.controller` | 健康探针接口 | [ProbeController](references/io.soil.jsf.wsf.controller/ProbeController.md) |
| `RestExceptionResponse` | `io.soil.jsf.wsf.exception` | 异常响应体 | [RestExceptionResponse](references/io.soil.jsf.wsf.exception/RestExceptionResponse.md) |
| `RestGlobalExceptionResolver` | `io.soil.jsf.wsf.exception` | 全局异常处理器 | [RestGlobalExceptionResolver](references/io.soil.jsf.wsf.exception/RestGlobalExceptionResolver.md) |
| `WebBizException` | `io.soil.jsf.wsf.exception` | 带 HTTP 状态码的业务异常 | [WebBizException](references/io.soil.jsf.wsf.exception/WebBizException.md) |
| `RemoteAddrUtil` | `io.soil.jsf.wsf.util` | 客户端真实 IP 工具 | [RemoteAddrUtil](references/io.soil.jsf.wsf.util/RemoteAddrUtil.md) |
