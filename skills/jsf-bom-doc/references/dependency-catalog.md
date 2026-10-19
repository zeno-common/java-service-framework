# jsf-bom 依赖版本清单

> 所有条目均已在 BOM 的 `dependencyManagement` 中托管，声明时**省略 `<version>`**。数据取自 `jsf-bom/**/pom.xml`（权威来源），版本 `jsf.0.2.1`。

## Spring / 微服务框架

| 依赖 | 版本 | 来源 |
|------|------|------|
| Spring Boot | 3.5.16 | jsf-spring-dependencies（导入 spring-boot-dependencies） |
| Spring Cloud | 2025.0.3 | jsf-spring-dependencies（导入 spring-cloud-dependencies） |
| Spring Cloud Alibaba | 2025.0.0.0 | jsf-alibaba-dependencies（导入其 BOM） |
| Nacos Client | 3.2.4 | jsf-alibaba-dependencies |
| Sentinel | 由 spring-cloud-alibaba BOM 供版 | jsf-alibaba-dependencies |
| Seata | 由 spring-cloud-alibaba BOM 供版 | jsf-alibaba-dependencies |
| Dubbo（starter/actuator/dubbo） | 3.0.0 | jsf-alibaba-dependencies |

## 数据库 / ORM

| 依赖 | 版本 | 来源 |
|------|------|------|
| MyBatis-Flex（spring-boot3-starter / core） | 1.11.7 | jsf-jdbc-dependencies（排除 HikariCP） |
| MyBatis | 3.5.19 | jsf-jdbc-dependencies |
| mybatis-spring-boot-starter | 3.0.4 | jsf-jdbc-dependencies |
| PageHelper | 6.1.1 | jsf-jdbc-dependencies |
| MySQL Connector（8.0.33）、Druid（1.2.25） | *仅在 properties 预留，未纳入 depMgmt* | jsf-jdbc-dependencies |

## 缓存 / NoSQL

| 依赖 | 版本 | 来源 |
|------|------|------|
| Redisson（redisson-spring-boot-starter） | 3.49.0 | jsf-redis-dependencies |

## jsf-dependencies 直接管理

| 依赖 | 版本 | 备注 |
|------|------|------|
| lombok | 1.18.46 | scope=provided |
| jackson-core / jackson-annotations / jackson-databind | 2.18.4 | |
| log4j-slf4j-impl | 2.24.3 | |
| logstash-logback-encoder | 7.2 | |
| disruptor | 4.0.0 | |
| caffeine | 3.2.0 | |
| commons-io | 2.19.0 | |
| commons-lang3 | 3.17.0 | |
| commons-collections4 | 4.5.0 | |
| jakarta.validation-api | 3.1.1 | |
| fastjson | 1.2.83 | 安全临时固定 |
| ip2region | 2.7.0 | 离线 IP 定位 |
| mapstruct-plus-spring-boot-starter | 1.4.8 | Dubbo 映射用 |
| apm-toolkit-logback-1.x / apm-toolkit-trace | 9.3.0 | SkyWalking |

## JSF 自有构件（io.soil.jsf，均 jsf.0.2.1）

| 来源子 BOM | 构件 |
|-----------|------|
| jsf-tools-dependencies | jsf-pojo、jsf-util、jsf-wsf、jsf-leaf-id、jsf-unit-test |
| jsf-common-dependencies | jsf-mongodb、jsf-mq-core、jsf-mq-mongodb |

## jsf-dependencies 继承给子项目的直接依赖

以下依赖由 `jsf-dependencies` 以 `<dependencies>` 声明，继承其 parent 的项目会自动获得：

| 依赖 | scope |
|------|-------|
| lombok | compile |
| spring-boot-starter-test（排除 spring-boot-starter-logging） | test |
| junit-jupiter | test |
| mockito-core | test |
