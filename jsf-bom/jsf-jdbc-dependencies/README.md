# jsf-jdbc-dependencies

数据库 & ORM 依赖管理模块，统一管理 JDBC 相关中间件的版本。

## 功能说明

管理 MyBatis 生态、数据库连接池、分页插件等数据库相关依赖的版本。

## 管理的依赖

| 依赖 | 版本 | 说明 |
|------|------|------|
| mybatis-flex-spring-boot3-starter | 1.11.7 | MyBatis-Flex Spring Boot 3 Starter（排除 HikariCP） |
| mybatis-flex-core | 1.11.7 | MyBatis-Flex 核心 |
| mybatis-spring-boot-starter | 3.0.4 | MyBatis Spring Boot Starter |
| mybatis | 3.5.19 | MyBatis 核心 |
| pagehelper | 6.1.1 | MyBatis 分页插件 |

## 使用方式

```xml
<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>io.soil</groupId>
            <artifactId>jsf-jdbc-dependencies</artifactId>
            <version>0.0.1</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>
```