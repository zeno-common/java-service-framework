# jsf-bom

Java Service Framework (JSF) 的 BOM（Bill of Materials）模块，负责统一管理整个框架的依赖版本。

## 模块概述

`jsf-bom` 采用分层依赖管理策略，将不同类别的依赖拆分到独立的子模块中，各子模块通过 `dependencyManagement` 统一声明版本号，避免版本冲突。

## 子模块结构

| 子模块 | 说明 |
|--------|------|
| [jsf-dependencies](./jsf-dependencies) | 总依赖聚合模块，整合所有子依赖 BOM |
| [jsf-parent](./jsf-parent) | 父 POM 模块，定义构建配置与多环境 Profile |
| [jsf-spring-dependencies](./jsf-spring-dependencies) | Spring Boot & Spring Cloud 依赖管理 |
| [jsf-alibaba-dependencies](./jsf-alibaba-dependencies) | Spring Cloud Alibaba 依赖管理 |
| [jsf-common-dependencies](./jsf-common-dependencies) | JSF 通用模块依赖管理 |
| [jsf-jdbc-dependencies](./jsf-jdbc-dependencies) | 数据库 & ORM 依赖管理 |
| [jsf-redis-dependencies](./jsf-redis-dependencies) | Redis 依赖管理 |
| [jsf-tools-dependencies](./jsf-tools-dependencies) | 工具库依赖管理 |

## 依赖层级关系

```
jsf-dependencies (聚合入口)
├── jsf-spring-dependencies      (Spring Boot / Spring Cloud)
├── jsf-alibaba-dependencies     (Spring Cloud Alibaba / Dubbo / Sentinel / Seata)
├── jsf-common-dependencies      (jsf-pojo / jsf-util / jsf-waf)
├── jsf-jdbc-dependencies        (MyBatis-Flex / PageHelper)
├── jsf-redis-dependencies       (Redisson)
└── jsf-tools-dependencies       (Hutool / Jackson / Lombok / Caffeine / MapStruct-Plus)
```

## 使用方式

### 作为依赖版本管理引入

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

### 作为父 POM 继承

业务项目可继承 `jsf-parent`，自动获得依赖版本管理和构建配置：

```xml
<parent>
    <groupId>io.soil</groupId>
    <artifactId>jsf-parent</artifactId>
    <version>0.0.1</version>
</parent>
```

## 版本信息

- **GroupId**: `io.soil`
- **当前版本**: `0.0.1`
- **Java 版本**: 21
- **Spring Boot**: 3.5.13
- **Spring Cloud**: 2025.0.2
- **Spring Cloud Alibaba**: 2025.0.0.0