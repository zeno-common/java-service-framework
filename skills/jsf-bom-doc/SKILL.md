---
name: "jsf-bom-doc"
description: "Use when setting up Maven dependencies for an io.soil.jsf project, need the jsf-bom import methods, the managed artifact list, version catalog, or build profiles."
version: "jsf.0.1.2"
---

# jsf-bom 使用参考

JSF 框架的 Maven BOM（Bill of Materials），统一管理所有依赖版本、构建插件与多环境 Profile。引入 BOM 后声明依赖**一律省略 `<version>`**。

## 引入方式

三选一，聚合入口为 `io.soil.jsf:jsf-dependencies`（版本统一 `jsf.0.1.2`）。

### 方式一：继承 jsf-parent（推荐，用于启动项目）

```xml
<parent>
    <groupId>io.soil.jsf</groupId>
    <artifactId>jsf-parent</artifactId>
    <version>jsf.0.1.2</version>
</parent>
```

自动获得：`jsf-dependencies` 全部依赖版本管理、Java 21 编译配置、多环境 Profile、Docker 镜像构建能力。

### 方式二：继承 jsf-dependencies（用于非启动项目）

```xml
<parent>
    <groupId>io.soil.jsf</groupId>
    <artifactId>jsf-dependencies</artifactId>
    <version>jsf.0.1.2</version>
</parent>
```

### 方式三：导入 jsf-dependencies BOM（不改变 parent）

```xml
<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>io.soil.jsf</groupId>
            <artifactId>jsf-dependencies</artifactId>
            <version>jsf.0.1.2</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>
```

## 声明依赖规则

引入 BOM/父 POM 后，使用其中托管的依赖时**必须省略 `<version>`**，由 BOM 统一供版。

```xml
<!-- ✅ 正确 -->
<dependency>
    <groupId>io.soil.jsf</groupId>
    <artifactId>jsf-util</artifactId>
</dependency>

<!-- ❌ 错误：不要写 version -->
<dependency>
    <groupId>io.soil.jsf</groupId>
    <artifactId>jsf-util</artifactId>
    <version>jsf.0.1.2</version>
</dependency>
```

## BOM 模块结构

| ArtifactId | 类型 | 说明 |
|-----------|------|------|
| `jsf-dependencies` | 聚合入口 BOM | 导入以下所有子 BOM，并直接管理部分通用依赖 |
| `jsf-parent` | 父 POM | 继承 jsf-dependencies，附加多环境 Profile 与 Docker 构建 |
| `jsf-spring-dependencies` | 子 BOM | Spring Boot 3.5.16 / Spring Cloud 2025.0.3 |
| `jsf-alibaba-dependencies` | 子 BOM | Spring Cloud Alibaba 2025.0.0.0 / Nacos 3.2.4 / Sentinel / Seata / Dubbo 3.0.0 |
| `jsf-tools-dependencies` | 子 BOM | jsf-pojo / jsf-util / jsf-wsf / jsf-leaf-id / jsf-unit-test |
| `jsf-common-dependencies` | 子 BOM | jsf-mongodb / jsf-mq-core / jsf-mq-mongodb |
| `jsf-jdbc-dependencies` | 子 BOM | MyBatis-Flex 1.11.7 / MyBatis / PageHelper 6.1.1 |
| `jsf-redis-dependencies` | 子 BOM | Redisson 3.49.0 |

## 可用 JSF 构件

引入 BOM 后可直接按 groupId `io.soil.jsf` 声明（无需 version）：

| ArtifactId | 用途 | 文档 skill |
|-----------|------|-----------|
| `jsf-util` | 通用工具集（日期/JSON/集合/树/IP/分页/异常基类） | jsf-util-doc |
| `jsf-wsf` | Web 服务框架（全局异常/Jackson/探针/客户端 IP） | jsf-wsf-doc |
| `jsf-pojo` | 通用 POJO（PagedResult 等） | - |
| `jsf-leaf-id` | Leaf 分布式 ID 生成 | - |
| `jsf-mongodb` | MongoDB 文档基类与自动配置 | jsf-mongodb-doc |
| `jsf-mq-core` | RocketMQ 生产者/消费者/Outbox 核心 | jsf-mq-core-doc |
| `jsf-mq-mongodb` | mq-core 存储接口的 MongoDB 实现 | jsf-mq-mongodb-doc |
| `jsf-unit-test` | 测试支持（YamlPropertySourceFactory） | - |
| `jsf-mail-sender` | 邮件发送 | - |

## 参考

- 完整依赖版本清单与第三方托管项：[dependency-catalog](references/dependency-catalog.md)
- 多环境 Profile、Docker 构建与插件版本：[build-and-profiles](references/build-and-profiles.md)
