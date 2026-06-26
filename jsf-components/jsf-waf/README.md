# jsf-waf

基于 Spring Boot 封装的 Web Application Framework (WAF) 通用模块，提供全局异常处理、Jackson 序列化配置、健康探针和常用工具类。引入即生效，零配置。

## 功能特性

- **全局异常处理** — `WafHttpExceptionResolver` 自动捕获异常，统一返回 JSON 响应
- **业务异常封装** — `WafException` 支持 HTTP 状态码 + 自定义异常码 + 消息模板
- **Jackson 序列化配置** — Long/BigInteger 转字符串防 JS 精度丢失、枚举字符串化、时间格式统一
- **健康探针** — `GET /v1/probes/activeness` 活跃检测接口
- **客户端 IP 获取** — `RemoteAddrUtil` 处理代理/负载均衡场景下的真实 IP 解析

## 引入依赖

前置条件：通过 [jsf-bom](../../jsf-bom/README.md) 管理依赖版本（二选一）：

```xml
<!-- 方式一：作为依赖版本管理引入 -->
<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>io.soil</groupId>
            <artifactId>jsf-dependencies</artifactId>
            <version>0.0.1</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>

<!-- 方式二：作为父 POM 继承 -->
<parent>
    <groupId>io.soil</groupId>
    <artifactId>jsf-parent</artifactId>
    <version>0.0.1</version>
</parent>
```

引入 jsf-waf：

```xml
<dependency>
    <groupId>io.soil</groupId>
    <artifactId>jsf-waf</artifactId>
</dependency>
```

## 自动配置

引入依赖后，Spring Boot 自动加载 `WafConfig`，包含：

| 配置项 | 说明 |
|--------|------|
| Long/BigInteger → String | 防止 JS Number 精度丢失 |
| LocalDateTime 格式化 | 按 `OffsetDateTimeUtil.OFFSET_DATE_TIME_FORMATTER` 格式 |
| 枚举字符串序列化 | `WRITE_ENUMS_USING_TO_STRING` / `READ_ENUMS_USING_TO_STRING` |
| 忽略未知属性 | `FAIL_ON_UNKNOWN_PROPERTIES = false` |
| 禁用时间戳格式 | `WRITE_DATES_AS_TIMESTAMPS = false` |
| 组件扫描 | 自动扫描 `io.soil.waf.controller` 包 |

## 使用示例

### 抛出业务异常

```java
// 简单消息，HTTP 500
throw new WafException("用户不存在");

// 指定 HTTP 状态码
throw new WafException(HttpStatus.NOT_FOUND, "资源未找到");

// 自定义异常码 + 消息模板
throw new WafException("USER_NOT_FOUND", HttpStatus.NOT_FOUND, "用户 {0} 不存在", userId);

// 包装原始异常
try {
    // ...
} catch (IOException e) {
    throw new WafException("IO_ERROR", e);
}
```

### 异常响应格式

**WafException 响应**（HTTP 状态码由异常决定）：

```json
{
  "module": "JSF-WAF",
  "errCode": "USER_NOT_FOUND",
  "errDesc": "用户 123 不存在",
  "trace": "io.soil.waf.exception.WafException: ..."
}
```

**非 WafException 响应**（HTTP 500）：

```json
{
  "module": "UNDEFINED",
  "errCode": "INTERNAL_SERVER_ERROR",
  "errDesc": "原始异常消息",
  "trace": "java.lang.NullPointerException: ..."
}
```

### 获取客户端 IP

```java
String clientIp = RemoteAddrUtil.getRealClientIp();
```

依次检查 `X-Real-IP`、`X-Forwarded-For` 头，兜底使用 `getRemoteAddr()`。

### 健康探针

```
GET /v1/probes/activeness → "active"
```

## 项目结构

```
io.soil.waf/
├── config/
│   └── WafConfig.java                 # Jackson + 组件扫描配置
├── controller/
│   └── ProbeController.java           # 健康探针
├── exception/
│   ├── WafException.java              # WAF 异常类
│   ├── WafHttpExceptionResolver.java  # 全局异常处理器
│   └── WafHttpExceptionResponse.java  # 异常响应 VO
├── handler/
│   └── BigNumSerializer.java          # 大数字序列化器
└── util/
    └── RemoteAddrUtil.java            # 客户端 IP 工具
```