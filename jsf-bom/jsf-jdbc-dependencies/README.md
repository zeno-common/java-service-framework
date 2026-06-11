# jsf-jdbc-dependencies

数据库 & ORM 依赖管理模块，统一管理 JDBC 相关中间件的版本。

## 功能说明

管理 MyBatis 生态、数据库连接池、分页插件等数据库相关依赖的版本。

## 管理的依赖

| 依赖 | 版本 | 说明 |
|------|------|------|
| mybatis-plus-spring-boot3-starter | 3.5.16 | MyBatis-Plus Spring Boot 3 Starter（排除 HikariCP 和 mybatis-spring） |
| mybatis-plus-core | 3.5.16 | MyBatis-Plus 核心 |
| mybatis-plus-jsqlparser | 3.5.16 | MyBatis-Plus SQL 解析器 |
| mybatis-spring-boot-starter | 3.0.4 | MyBatis Spring Boot Starter |
| mybatis | 3.5.19 | MyBatis 核心 |
| dynamic-datasource-spring-boot3-starter | 4.3.1 | 动态数据源 |
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