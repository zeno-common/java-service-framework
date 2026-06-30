# jsf-redis-dependencies

Redis 依赖管理模块，统一管理 Redis 相关中间件的版本。

## 功能说明

管理 Redis 客户端及分布式锁等 Redis 生态依赖的版本。

## 管理的依赖

| 依赖 | 版本 | 说明 |
|------|------|------|
| redisson-spring-boot-starter | 3.49.0 | Redisson Spring Boot Starter，提供分布式锁、分布式集合等功能 |

## 使用方式

```xml
<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>io.soil.jsf</groupId>
            <artifactId>jsf-redis-dependencies</artifactId>
            <version>0.0.1</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>
```