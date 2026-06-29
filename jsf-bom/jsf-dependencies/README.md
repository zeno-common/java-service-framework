# jsf-dependencies

总依赖聚合模块，整合所有子依赖 BOM，作为项目依赖版本管理的统一入口。

## 功能说明

本模块通过 `dependencyManagement` 将所有子依赖 BOM 以 `import` 方式聚合，同时额外管理以下依赖的版本：

- **SkyWalking** - 链路追踪与日志整合
- **IP2Region** - 离线 IP 地址定位
- **FastJSON** - JSON 解析
- **RocketMQ** - 消息队列
- **Logstash Logback Encoder** - 日志推送
- **MapStruct-Plus** - 对象映射
- **Lombok** - 代码简化

## 聚合的子 BOM

| 子 BOM | 说明 |
|--------|------|
| jsf-spring-dependencies | Spring Boot & Spring Cloud |
| jsf-alibaba-dependencies | Spring Cloud Alibaba |
| jsf-common-dependencies | JSF 通用模块 |
| jsf-components-dependencies | JSF 业务组件 |
| jsf-jdbc-dependencies | 数据库 & ORM |
| jsf-redis-dependencies | Redis |
| jsf-tools-dependencies | 工具库 |

## 默认依赖

本模块在 `dependencies` 中默认引入：

- `org.projectlombok:lombok`（provided 作用域）
- `junit:junit`（test 作用域）

## 构建插件管理

本模块通过 `pluginManagement` 统一管理以下 Maven 插件版本：

| 插件 | 版本 | 说明 |
|------|------|------|
| maven-compiler-plugin | 3.15.0 | 编译插件 |
| maven-resources-plugin | 3.5.0 | 资源处理 |
| maven-jar-plugin | 3.5.0 | JAR 打包 |
| maven-source-plugin | 3.4.0 | 源码打包 |
| maven-shade-plugin | 3.3.0 | Uber-JAR 打包 |
| maven-dependency-plugin | 3.11.0 | 依赖操作 |
| maven-checkstyle-plugin | 3.2.0 | 代码规范检查 |
| maven-surefire-plugin | 2.22.2 | 单元测试 |
| spotbugs-maven-plugin | 4.8.6.2 | Bug 检测 |
| sonar-maven-plugin | 3.9.1.2184 | 代码质量分析 |
| docker-maven-plugin | 0.48.1 | Docker 构建 |
| dokka-maven-plugin | 2.2.0 | Kotlin 文档生成 |
| smart-doc-maven-plugin | 3.1.2 | API 文档生成 |

## 使用方式

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