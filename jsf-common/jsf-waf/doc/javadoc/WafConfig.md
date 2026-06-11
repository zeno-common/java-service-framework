# WafConfig

`io.soil.waf.config.WafConfig`

WAF（Web Application Framework）配置类，配置 Jackson 序列化/反序列化规则和组件扫描。

## 类签名

```java
@Configuration
@ComponentScan(basePackageClasses = {ProbeController.class})
public class WafConfig
```

## 自动配置

本类通过 `META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports` 注册为自动配置类，引入 `jsf-waf` 依赖后自动生效。

## Bean 定义

### jackson2ObjectMapperBuilder()

```java
@Bean
public Jackson2ObjectMapperBuilder jackson2ObjectMapperBuilder()
```

配置 Jackson ObjectMapper 构建器，统一 JSON 序列化/反序列化规则：

| 配置项 | 值 | 说明 |
|--------|-----|------|
| WRITE_ENUMS_USING_TO_STRING | 启用 | 枚举序列化使用 `toString()` |
| READ_ENUMS_USING_TO_STRING | 启用 | 枚举反序列化使用 `toString()` |
| FAIL_ON_UNKNOWN_PROPERTIES | 禁用 | 忽略未知 JSON 属性 |
| WRITE_DATES_AS_TIMESTAMPS | 禁用 | 日期序列化为字符串而非时间戳 |

## 使用方式

无需手动配置，引入 `jsf-waf` 模块后自动激活。如需排除：

```java
@SpringBootApplication(exclude = {WafConfig.class})
public class MyApp {}
```