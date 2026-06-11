# jsf-spring-dependencies

Spring Boot & Spring Cloud 依赖管理模块。

## 功能说明

统一管理 Spring 生态核心依赖的版本，确保 Spring Boot 与 Spring Cloud 版本兼容。

## 管理的依赖

| 依赖 | 版本 | 说明 |
|------|------|------|
| spring-boot-dependencies | 3.5.13 | Spring Boot BOM |
| spring-cloud-dependencies | 2025.0.2 | Spring Cloud BOM |

## 使用方式

```xml
<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>io.soil</groupId>
            <artifactId>jsf-spring-dependencies</artifactId>
            <version>0.0.1</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>
```

## 版本兼容性

| Spring Boot | Spring Cloud |
|-------------|--------------|
| 3.5.13 | 2025.0.2 |