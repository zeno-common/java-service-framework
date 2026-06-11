# jsf-components-dependencies

JSF 业务组件依赖管理模块，统一管理 `ruoyi-common-*` 系列业务组件的版本。

## 功能说明

管理所有业务组件模块的依赖版本，覆盖 Web、安全、存储、消息、监控等业务领域。

## 管理的依赖

### 核心与基础

| 依赖 | 说明 |
|------|------|
| ruoyi-common-core | 核心模块，基础工具与通用配置 |
| ruoyi-common-doc | 接口文档模块 |
| ruoyi-common-json | 序列化模块 |
| ruoyi-common-translation | 通用翻译功能 |

### 安全与认证

| 依赖 | 说明 |
|------|------|
| ruoyi-common-security | 安全模块 |
| ruoyi-common-encrypt | 数据加解密模块 |
| ruoyi-common-social | 授权认证（第三方登录） |

### Web 与通信

| 依赖 | 说明 |
|------|------|
| ruoyi-common-web | Web 服务模块 |
| ruoyi-common-websocket | WebSocket 模块 |
| ruoyi-common-sse | SSE 推送模块 |

### 数据与存储

| 依赖 | 说明 |
|------|------|
| ruoyi-common-redis | 缓存服务 |
| ruoyi-common-oss | 对象存储服务 |
| ruoyi-common-elasticsearch | ES 搜索引擎服务 |
| ruoyi-common-excel | Excel 导入导出 |

### 微服务与分布式

| 依赖 | 说明 |
|------|------|
| ruoyi-common-dubbo | RPC 服务 |
| ruoyi-common-seata | 分布式事务 |
| ruoyi-common-loadbalancer | 自定义负载均衡 |
| ruoyi-common-nacos | 配置中心 |
| ruoyi-common-bus | 消息总线模块 |

### 限流与防护

| 依赖 | 说明 |
|------|------|
| ruoyi-common-sentinel | 限流模块 |
| ruoyi-common-ratelimiter | 限流功能 |
| ruoyi-common-idempotent | 幂等功能 |

### 日志与监控

| 依赖 | 说明 |
|------|------|
| ruoyi-common-log | 日志记录 |
| ruoyi-common-logstash | Logstash 日志推送 |
| ruoyi-common-skylog | SkyWalking 日志收集 |
| ruoyi-common-prometheus | Prometheus 监控 |

### 消息与通知

| 依赖 | 说明 |
|------|------|
| ruoyi-common-mail | 邮件模块 |
| ruoyi-common-sms | 短信模块 |

### 其他

| 依赖 | 说明 |
|------|------|
| ruoyi-common-sensitive | 脱敏模块 |
| ruoyi-common-tenant | 租户模块 |
| ruoyi-common-service-impl | 通用 Service 实现模块 |

## 使用方式

```xml
<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>io.soil</groupId>
            <artifactId>jsf-components-dependencies</artifactId>
            <version>0.0.1</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>
```