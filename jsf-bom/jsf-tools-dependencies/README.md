# jsf-tools-dependencies

工具库依赖管理模块，统一管理通用工具库的版本。

## 功能说明

管理项目中使用的通用工具库依赖版本，包括 JSON、日志、缓存、对象映射等。

## 管理的依赖

| 依赖 | 版本 | 说明 |
|------|------|------|
| hutool-bom | 5.8.35 | Hutool 工具库 BOM |
| jackson-core | 2.18.4 | Jackson JSON 核心 |
| jackson-annotations | 2.18.4 | Jackson 注解 |
| jackson-databind | 2.18.4 | Jackson 数据绑定 |
| log4j-slf4j-impl | 2.24.3 | Log4j2 SLF4J 实现 |
| disruptor | 4.0.0 | LMAX Disruptor 高性能队列 |
| caffeine | 3.2.0 | Caffeine 本地缓存 |
| commons-io | 2.19.0 | Apache Commons IO |
| commons-lang3 | 3.17.0 | Apache Commons Lang3 |
| commons-collections4 | 4.5.0 | Apache Commons Collections4 |
| jakarta.validation-api | 3.1.1 | Jakarta Validation API |
| mapstruct-plus-spring-boot-starter | 1.4.8 | MapStruct-Plus 对象映射 |
| ip2region | 2.7.0 | 离线 IP 地址定位库 |

## 使用方式

```xml
<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>io.soil</groupId>
            <artifactId>jsf-tools-dependencies</artifactId>
            <version>0.0.1</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>
```