---
name: "jsf-waf-doc"
description: "Use when coding with jsf-waf SDK, need API reference for WAF framework, exception handling, Jackson config, health probe, or client IP resolution."
---

# jsf-waf SDK 参考

WAF（Web Application Framework）— 基于 Spring Boot 的 Web 应用框架，提供全局异常处理、Jackson 序列化配置、健康探针和工具类。

## 依赖引入

Java Maven 工程的依赖引入参考：'[jsf-bom/README.md](../../agent-rules/jsf-bom/README.md)'

| ArtifactId | 说明 |
|-----------|------|
| `jsf-waf` | Web 应用框架，含自动配置 |

## 类索引

| 类 | 包 | 说明 | 文档 |
|----|-----|------|------|
| `WafConfig` | io.soil.waf.config | Jackson 序列化/反序列化配置与组件扫描 | [WafConfig](references/io.soil.waf.config/WafConfig.md) |
| `ProbeController` | io.soil.waf.controller | 健康探针接口 | [ProbeController](references/io.soil.waf.controller/ProbeController.md) |
| `WafException` | io.soil.waf.exception | WAF 异常，支持 HTTP 状态码和自定义错误码 | [WafException](references/io.soil.waf.exception/WafException.md) |
| `WafHttpExceptionResolver` | io.soil.waf.exception | 全局异常处理器 | [WafHttpExceptionResolver](references/io.soil.waf.exception/WafHttpExceptionResolver.md) |
| `WafHttpExceptionResponse` | io.soil.waf.exception | 异常响应 VO | [WafHttpExceptionResponse](references/io.soil.waf.exception/WafHttpExceptionResponse.md) |
| `RemoteAddrUtil` | io.soil.waf.util | 从代理头获取客户端真实 IP | [RemoteAddrUtil](references/io.soil.waf.util/RemoteAddrUtil.md) |
