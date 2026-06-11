# JsfCommonAutoConfig

`io.soil.util.config.JsfCommonAutoConfig`

JSF 通用模块自动配置类，通过 Spring Boot 自动配置机制扫描 `io.soil.util` 包下的组件。

## 类签名

```java
@Configuration
@ComponentScan({"io.soil.util"})
public class JsfCommonAutoConfig
```

## 自动配置

本类通过 `META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports` 注册为自动配置类，引入 `jsf-util` 依赖后自动生效。

自动扫描范围：

| 包路径 | 包含的组件 |
|--------|-----------|
| `io.soil.util` | ApplicationContextUtil 等 |

## 使用方式

无需手动配置，引入 `jsf-util` 模块后自动激活。如需排除：

```java
@SpringBootApplication(exclude = {JsfCommonAutoConfig.class})
public class MyApp {}
```