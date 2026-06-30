﻿﻿# jsf-bom

JSF 框架的 Maven BOM（Bill of Materials）模块，统一管理所有依赖版本、构建配置和多环境 Profile。

## 模块结构

| 模块 | 说明 |
|------|------|
| [jsf-dependencies](./jsf-dependencies) | 聚合 BOM 入口，整合所有子 BOM 并直接管理部分依赖 |
| [jsf-parent](./jsf-parent) | 父 POM，提供多环境 Profile 和 Docker 构建能力 |
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
    ├── jsf-alibaba-dependencies      (Spring Cloud Alibaba 2025.0.0.0 / Nacos 2.5.1 / Dubbo 3.0.0)
    ├── jsf-common-dependencies       (jsf-pojo / jsf-util / jsf-waf)
    ├── jsf-components-dependencies   (jsf-waf 等组件)
    ├── jsf-jdbc-dependencies         (MyBatis-Flex 1.11.7 / PageHelper 6.1.1)
    ├── jsf-redis-dependencies        (Redisson 3.49.0)
    ├── jsf-tools-dependencies        (Jackson 2.18.4 / Caffeine 3.2.0 / MapStruct-Plus 1.4.8 / Commons)
    └── [直接管理]                    (Sa-Token Dubbo3 / RocketMQ / Lombok / FastJSON / SkyWalking / ...)
```

## 使用方式

### 方式一：继承 jsf-parent（推荐，用于启动项目）

```xml
<parent>
    <groupId>io.soil.jsf</groupId>
    <artifactId>jsf-parent</artifactId>
    <version>0.0.1</version>
</parent>
```

自动获得：jsf-dependencies 全部依赖版本管理、Java 21 配置、多环境 Profile、Docker 镜像构建能力。

### 方式二：继承 jsf-dependencies（用于非启动项目）

```xml
<parent>
    <groupId>io.soil.jsf</groupId>
    <artifactId>jsf-dependencies</artifactId>
    <version>0.0.1</version>
</parent>
```

### 方式三：导入 jsf-dependencies BOM（用于非启动项目）

```xml
<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>io.soil.jsf</groupId>
            <artifactId>jsf-dependencies</artifactId>
            <version>0.0.1</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>
```

## 声明依赖规则

引入 BOM/父 POM 后，使用 jsf-bom 中的依赖声明，无需重复声明版本号。声明具体依赖时**必须省略 `<version>` 标签**。

✅ 正确写法：
```xml
<dependency>
    <groupId>io.soil.jsf</groupId>
    <artifactId>jsf-util</artifactId>
    <!-- 无 <version>，由 BOM 管理 -->
</dependency>
```

❌ 错误写法：
```xml
<dependency>
    <groupId>io.soil.jsf</groupId>
    <artifactId>jsf-util</artifactId>
    <version>0.0.1</version>  <!-- 不要写！ -->
</dependency>
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
| MySQL Connector | 8.0.33 | jsf-jdbc-dependencies（版本预留） |
| Druid | 1.2.25 | jsf-jdbc-dependencies（版本预留） |
| PageHelper | 6.1.1 | jsf-jdbc-dependencies |

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

### 其他

| 依赖 | 版本 | 来源 |
|------|------|------|
| FastJSON | 1.2.83 | jsf-dependencies 直接管理 |
| SkyWalking Toolkit | 9.3.0 | jsf-dependencies 直接管理 |
| Logstash Logback Encoder | 7.2 | jsf-dependencies 直接管理 |

## 多环境 Profile

通过 `-P <profileId>` 激活对应环境：

| Profile ID | env 值 | Docker Push | 用途 |
|------------|--------|-------------|------|
| `d-local` | local | — | 本地开发 |
| `d-backend` | backend | — | 后端开发环境 |
| `d-dev` | dev | — | 开发环境 |
| `d-uat` | uat | ✅ 阿里云 ACR | 用户验收测试 |
| `d-prod` | prod | ✅ 阿里云 ACR | 生产环境 |

### Docker 构建

激活 `pd-docker` Profile 启用镜像构建，需与环境 Profile 组合使用：

```bash
# 开发环境 + Docker
mvn clean package -P d-dev,pd-docker

# 生产环境打包
mvn clean package -P d-prod
```

Docker 配置：

| 配置项 | 值 |
|--------|-----|
| 基础镜像 | `eclipse-temurin:21.0.4_7-jre-alpine` |
| 镜像名 | `soil/${project.artifactId}` |
| Tag 规则 | `${env}-${project.version}` / `${env}-latest` |
| 入口命令 | `java -Dspring.profiles.active=${env} -jar ${project.build.finalName}.jar` |

## 构建插件

| 插件 | 版本 | 说明 |
|------|------|------|
| maven-compiler-plugin | 3.15.0 | 注解处理器含 Lombok |
| maven-source-plugin | 3.4.0 | package 阶段生成源码 jar |
| maven-jar-plugin | 3.5.0 | |
| maven-resources-plugin | 3.5.0 | |
| maven-shade-plugin | 3.3.0 | |
| maven-dependency-plugin | 3.11.0 | |
| maven-surefire-plugin | 2.22.2 | |
| maven-checkstyle-plugin | 3.2.0 | |
| spotbugs-maven-plugin | 4.8.6.2 | |
| sonar-maven-plugin | 3.9.1.2184 | |
| docker-maven-plugin | 0.48.1 | Fabric8 Docker 插件 |
| dokka-maven-plugin | 2.2.0 | Kotlin/Java 文档生成 |
| smart-doc-maven-plugin | 3.1.2 | API 文档生成 |

## 完整启动项目 POM 模板

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
  <modelVersion>4.0.0</modelVersion>

  <parent>
    <groupId>io.soil.jsf</groupId>
    <artifactId>jsf-parent</artifactId>
    <version>0.0.1</version>
  </parent>

  <artifactId>my-service</artifactId>
  <packaging>jar</packaging>

  <dependencies>
    <!-- 版本由 jsf-dependencies BOM 管理，无需写 version -->
    <dependency>
      <groupId>io.soil.jsf</groupId>
      <artifactId>jsf-util</artifactId>
    </dependency>
    <dependency>
      <groupId>io.soil.jsf</groupId>
      <artifactId>jsf-waf</artifactId>
    </dependency>
  </dependencies>
</project>
```