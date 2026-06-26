---
name: "jsf-bom-doc"
description: "Use when coding with jsf-bom, need reference for Maven BOM/parent POM configuration, dependency version management, multi-environment profiles, or Docker build setup."
---

# jsf-bom SDK 参考

JSF BOM（Bill of Materials）— 统一管理 JSF 框架及第三方依赖的版本，提供父 POM 多环境 Profile 和 Docker 构建配置。

## 引入方式

### 方式一：继承 jsf-parent（推荐，用于启动项目）

```xml
<parent>
  <groupId>io.soil</groupId>
  <artifactId>jsf-parent</artifactId>
  <version>0.0.1</version>
</parent>
```

自动获得：jsf-dependencies 全部依赖版本管理、Java 21 配置、多环境 Profile、Docker 镜像构建能力。

> 详见 [jsf-parent.md](references/jsf-parent.md)

### 方式二：继承 jsf-dependencies（用于非启动项目）

```xml
<parent>
  <groupId>io.soil</groupId>
  <artifactId>jsf-dependencies</artifactId>
  <version>0.0.1</version>
</parent>
```

> 详见 [jsf-dependencies.md](references/jsf-dependencies.md)

### 方式三：导入 jsf-dependencies BOM（用于非启动项目）

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

## 声明依赖规则

引入 BOM/父 POM 后，声明具体依赖时**必须省略 `<version>` 标签**。

## 依赖速查表

### JSF 内部模块

| 功能 | groupId | artifactId | 版本 |
|------|---------|-----------|------|
| 通用 POJO/DTO | `io.soil` | `jsf-pojo` | 0.0.1 |
| 通用工具类 | `io.soil` | `jsf-util` | 0.0.1 |
| Web 应用框架 | `io.soil` | `jsf-waf` | 0.0.1 |

### Spring 生态

| 功能 | groupId | artifactId | 版本 |
|------|---------|-----------|------|
| Spring Boot | `org.springframework.boot` | `spring-boot-dependencies` | 3.5.15 |
| Spring Cloud | `org.springframework.cloud` | `spring-cloud-dependencies` | 2025.0.3 |
| Spring Cloud Alibaba | `com.alibaba.cloud` | `spring-cloud-alibaba-dependencies` | 2025.0.0.0 |

### 数据库 / ORM

| 功能 | groupId | artifactId | 版本 |
|------|---------|-----------|------|
| MyBatis-Flex (Boot3) | `com.mybatis-flex` | `mybatis-flex-spring-boot3-starter` | 1.11.7 |
| MyBatis-Flex 核心 | `com.mybatis-flex` | `mybatis-flex-core` | 1.11.7 |
| MyBatis (Spring Boot) | `org.mybatis.spring.boot` | `mybatis-spring-boot-starter` | 3.0.4 |
| PageHelper 分页 | `com.github.pagehelper` | `pagehelper` | 6.1.1 |

### 缓存 / Redis

| 功能 | groupId | artifactId | 版本 |
|------|---------|-----------|------|
| Redisson | `org.redisson` | `redisson-spring-boot-starter` | 3.49.0 |

### 工具库

| 功能 | groupId | artifactId | 版本 |
|------|---------|-----------|------|
| Hutool (BOM) | `cn.hutool` | `hutool-bom` | 5.8.35 |
| Jackson | `com.fasterxml.jackson.core` | `jackson-databind` | 2.18.4 |
| Caffeine 缓存 | `com.github.ben-manes.caffeine` | `caffeine` | 3.2.0 |
| MapStruct-Plus | `io.github.linpeilie` | `mapstruct-plus-spring-boot-starter` | 1.4.8 |
| Commons Lang3 | `org.apache.commons` | `commons-lang3` | 3.17.0 |
| Commons IO | `commons-io` | `commons-io` | 2.19.0 |
| Lombok | `org.projectlombok` | `lombok` | 1.18.46 |

### 认证 / 加密 / 第三方

| 功能 | groupId | artifactId | 版本 |
|------|---------|-----------|------|
| Sa-Token | `cn.dev33` | `sa-token-spring-boot3-starter` | 1.44.0 |
| Sa-Token JWT | `cn.dev33` | `sa-token-jwt` | 1.44.0 |
| Bouncy Castle | `org.bouncycastle` | `bcprov-jdk15to18` | 1.80 |
| JustAuth 第三方登录 | `me.zhyd.oauth` | `JustAuth` | 1.16.7 |
| AWS S3 SDK | `software.amazon.awssdk` | `s3` | 2.28.22 |
| SMS4J 短信 | `org.dromara.sms4j` | `sms4j-spring-boot-starter` | 3.3.4 |
| RocketMQ | `org.apache.rocketmq` | `rocketmq-spring-boot-starter` | 2.3.0 |
| SkyWalking 链路 | `org.apache.skywalking` | `apm-toolkit-trace` | 9.3.0 |
| IP2Region | `org.lionsoul` | `ip2region` | 2.7.0 |

## 文档索引

| 文档 | 说明 |
|------|------|
| [jsf-dependencies.md](references/jsf-dependencies.md) | 完整依赖版本清单与插件管理 |
| [jsf-parent.md](references/jsf-parent.md) | 父 POM 的 Profile 与 Docker 构建配置 |

## Agent POM 生成检查清单

- [ ] 是否通过方式一或方式二引入了 BOM？
- [ ] 所有被 BOM 管理的依赖是否都**省略了 `<version>`**？
- [ ] Java 版本是否为 21（若继承 jsf-parent 则自动配置）？
- [ ] 是否需要激活环境 Profile（如 `-P d-dev`）？
- [ ] 是否需要 Docker 构建（加 `-P pd-docker`）？
- [ ] Lombok 依赖 scope 是否为 `provided`（BOM 已默认配置）？