# jsf-bom

Java Service Framework (JSF) 的 BOM（Bill of Materials）模块，负责统一管理整个框架的依赖版本。采用分层依赖管理策略，将不同类别的依赖拆分到独立的子模块中，避免版本冲突。

## 子模块结构

| 子模块 | 说明 |
|--------|------|
| [jsf-dependencies](./jsf-dependencies) | 总依赖聚合入口，整合子 BOM + 直接管理中间件依赖 |
| [jsf-parent](./jsf-parent) | 父 POM，继承 jsf-dependencies，提供构建配置与多环境 Profile |
| [jsf-spring-dependencies](./jsf-spring-dependencies) | Spring Boot & Spring Cloud 依赖管理 |
| [jsf-alibaba-dependencies](./jsf-alibaba-dependencies) | Spring Cloud Alibaba / Nacos / Sentinel / Seata / Dubbo |
| [jsf-common-dependencies](./jsf-common-dependencies) | JSF 通用模块（jsf-pojo / jsf-util / jsf-waf） |
| [jsf-components-dependencies](./jsf-components-dependencies) | JSF 组件模块 |
| [jsf-jdbc-dependencies](./jsf-jdbc-dependencies) | 数据库 & ORM（MyBatis-Flex / PageHelper） |
| [jsf-redis-dependencies](./jsf-redis-dependencies) | Redis（Redisson） |
| [jsf-tools-dependencies](./jsf-tools-dependencies) | 工具库（Jackson / Caffeine / MapStruct-Plus / Commons） |

## 依赖层级关系

```
jsf-parent
└── jsf-dependencies (聚合入口)
    ├── jsf-spring-dependencies       (Spring Boot 3.5.15 / Spring Cloud 2025.0.3)
    ├── jsf-alibaba-dependencies      (Spring Cloud Alibaba / Nacos / Sentinel / Seata / Dubbo)
    ├── jsf-common-dependencies       (jsf-pojo / jsf-util / jsf-waf)
    ├── jsf-components-dependencies   (jsf-waf 等组件)
    ├── jsf-jdbc-dependencies         (MyBatis-Flex / PageHelper)
    ├── jsf-redis-dependencies        (Redisson)
    ├── jsf-tools-dependencies        (Jackson / Caffeine / MapStruct-Plus / Commons)
    └── [直接管理]                    (Sa-Token / AWS SDK / RocketMQ / Lombok / ...)
```

## 使用方式

### 方式一：作为依赖版本管理引入

在项目的 `pom.xml` 中引入 `jsf-dependencies`，即可获得所有依赖的版本管理：

```xml
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
```

### 方式二：作为父 POM 继承

业务项目可继承 `jsf-parent`，自动获得依赖版本管理、全局依赖和构建配置：

```xml
<parent>
    <groupId>io.soil</groupId>
    <artifactId>jsf-parent</artifactId>
    <version>0.0.1</version>
</parent>
```

`jsf-parent` 继承自 `jsf-dependencies`，额外提供：

| 能力 | 说明 |
|------|------|
| 全局依赖 | Lombok、JUnit 自动引入（无需手动添加） |
| 多环境 Profile | `d-local` / `d-backend` / `d-dev` / `d-uat` / `d-prod` |
| Docker 构建 | `pd-docker` Profile，基于 `eclipse-temurin:21-jre-alpine` |

Docker 构建示例：

```bash
mvn package -P d-dev,pd-docker
```

## 核心版本速查

### 基础框架

| 依赖 | 版本 |
|------|------|
| Java | 21 |
| Spring Boot | 3.5.15 |
| Spring Cloud | 2025.0.3 |
| Spring Cloud Alibaba | 2025.0.0.0 |

### 微服务 & 中间件

| 依赖 | 版本 | 来源 |
|------|------|------|
| Nacos Client | 2.5.1 | jsf-alibaba-dependencies |
| Sentinel | *(由 Spring Cloud Alibaba BOM 管理)* | jsf-alibaba-dependencies |
| Seata | *(由 Spring Cloud Alibaba BOM 管理)* | jsf-alibaba-dependencies |
| Dubbo | 3.0.0 | jsf-alibaba-dependencies |
| Redisson | 3.49.0 | jsf-redis-dependencies |
| RocketMQ | 2.3.0 | jsf-dependencies 直接管理 |

### 数据库

| 依赖 | 版本 | 来源 |
|------|------|------|
| MyBatis | 3.5.19 | jsf-jdbc-dependencies |
| MyBatis-Flex | 1.11.7 | jsf-jdbc-dependencies |
| MyBatis-Spring Boot | 3.0.4 | jsf-jdbc-dependencies |
| MySQL Connector | 8.0.33 | jsf-jdbc-dependencies |
| PageHelper | 6.1.1 | jsf-jdbc-dependencies |
| Anyline | 8.7.2 | jsf-dependencies 直接管理 |

### 工具库

| 依赖 | 版本 | 来源 |
|------|------|------|
| Jackson | 2.18.4 | jsf-tools-dependencies |
| Log4j2 | 2.24.3 | jsf-tools-dependencies |
| Lombok | 1.18.46 | jsf-dependencies 直接管理 |
| MapStruct-Plus | 1.4.8 | jsf-tools-dependencies / jsf-dependencies |
| Caffeine | 3.2.0 | jsf-tools-dependencies |
| Commons IO | 2.19.0 | jsf-tools-dependencies |
| Commons Lang3 | 3.17.0 | jsf-tools-dependencies |
| Commons Collections4 | 4.5.0 | jsf-tools-dependencies |
| Disruptor | 4.0.0 | jsf-tools-dependencies |
| Jakarta Validation | 3.1.1 | jsf-tools-dependencies |
| ip2region | 2.7.0 | jsf-tools-dependencies / jsf-dependencies |

### 认证 & 安全

| 依赖 | 版本 | 来源 |
|------|------|------|
| Sa-Token | 1.44.0 | jsf-dependencies 直接管理 |
| BouncyCastle | 1.80 | jsf-dependencies 直接管理 |
| JustAuth | 1.16.7 | jsf-dependencies 直接管理 |

### 存储 & 通知

| 依赖 | 版本 | 来源 |
|------|------|------|
| AWS SDK (S3) | 2.28.22 | jsf-dependencies 直接管理 |
| SMS4J | 3.3.4 | jsf-dependencies 直接管理 |

### 其他

| 依赖 | 版本 | 来源 |
|------|------|------|
| FastJSON | 1.2.83 | jsf-dependencies 直接管理 |
| SkyWalking Toolkit | 9.3.0 | jsf-dependencies 直接管理 |
| Logstash Logback Encoder | 7.2 | jsf-dependencies 直接管理 |
| Warm-Flow | 1.7.3 | jsf-dependencies 直接管理 |