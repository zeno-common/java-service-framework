# jsf-alibaba-dependencies

Spring Cloud Alibaba 依赖管理模块，统一管理阿里巴巴微服务生态组件的版本。

## 功能说明

管理 Spring Cloud Alibaba 全家桶及相关中间件的依赖版本，包括 Nacos、Sentinel、Seata、Dubbo 等。

## 管理的依赖

### Spring Cloud Alibaba

| 依赖 | 版本 | 说明 |
|------|------|------|
| spring-cloud-alibaba-dependencies | 2025.0.0.0 | Spring Cloud Alibaba BOM |

### Nacos

| 依赖 | 版本 | 说明 |
|------|------|------|
| nacos-client | 2.5.1 | Nacos 客户端 |

### Sentinel

| 依赖 | 说明 |
|------|------|
| sentinel-core | 核心库 |
| sentinel-parameter-flow-control | 参数限流 |
| sentinel-datasource-extension | 数据源扩展 |
| sentinel-datasource-apollo | Apollo 数据源 |
| sentinel-datasource-zookeeper | ZooKeeper 数据源 |
| sentinel-datasource-nacos | Nacos 数据源 |
| sentinel-datasource-redis | Redis 数据源 |
| sentinel-datasource-consul | Consul 数据源 |
| sentinel-web-servlet | Servlet 适配 |
| sentinel-spring-cloud-gateway-adapter | Gateway 适配 |
| sentinel-transport-simple-http | 传输模块 |
| sentinel-annotation-aspectj | 注解支持 |
| sentinel-reactor-adapter | Reactor 适配 |
| sentinel-cluster-server-default | 集群服务端 |
| sentinel-cluster-client-default | 集群客户端 |
| sentinel-spring-webflux-adapter | WebFlux 适配 |
| sentinel-api-gateway-adapter-common | API 网关适配 |
| sentinel-spring-webmvc-v6x-adapter | Spring MVC 6 适配 |
| sentinel-dubbo-adapter | Dubbo 适配 |
| sentinel-apache-dubbo-adapter | Apache Dubbo 适配 |
| sentinel-apache-dubbo3-adapter | Apache Dubbo3 适配 |

### Seata

| 依赖 | 说明 |
|------|------|
| seata-spring-boot-starter | Seata Spring Boot Starter |
| seata-all | Seata 全量包 |

### Apache Dubbo

| 依赖 | 版本 | 说明 |
|------|------|------|
| dubbo-spring-boot-starter | 3.0.0 | Dubbo Spring Boot Starter |
| dubbo-spring-boot-actuator | 3.0.0 | Dubbo 监控 |
| dubbo | 3.0.0 | Dubbo 核心 |
| dubbo-metadata-report-redis | - | Redis 元数据报告 |

## 使用方式

```xml
<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>io.soil</groupId>
            <artifactId>jsf-alibaba-dependencies</artifactId>
            <version>0.0.1</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>
```