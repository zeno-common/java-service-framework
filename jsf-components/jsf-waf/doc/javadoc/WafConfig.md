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
public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilder()
```

配置 Jackson ObjectMapper 构建器，统一 JSON 序列化/反序列化规则。

### JavaTimeModule 配置

通过 `JavaTimeModule` 注册自定义的序列化器和反序列化器：

| 类型 | 方向 | 序列化器/反序列化器 | 说明 |
|------|------|---------------------|------|
| `Long` | 序列化 | `ToStringSerializer.instance` | 防止 JS 中数字精度丢失，转为字符串 |
| `long` | 序列化 | `ToStringSerializer.instance` | 防止 JS 中数字精度丢失，转为字符串 |
| `BigInteger` | 序列化 | `ToStringSerializer.instance` | 防止 JS 中数字精度丢失，转为字符串 |
| `LocalDateTime` | 序列化 | `LocalDateTimeSerializer` | 使用 `DateTimeFormatter.ISO_OFFSET_DATE_TIME`（`yyyy-MM-dd'T'HH:mm:ssXXX`）格式 |
| `LocalDateTime` | 反序列化 | `LocalDateTimeDeserializer` | 使用 `DateTimeFormatter.ISO_OFFSET_DATE_TIME`（`yyyy-MM-dd'T'HH:mm:ssXXX`）格式 |

> 时间格式化器来源于 `io.soil.common.date.OffsetDateTimeUtil.OFFSET_DATE_TIME_FORMATTER`。

### 特性配置

| 配置项 | 值 | 说明 |
|--------|-----|------|
| WRITE_ENUMS_USING_TO_STRING | 启用 | 枚举序列化使用 `toString()` |
| READ_ENUMS_USING_TO_STRING | 启用 | 枚举反序列化使用 `toString()` |
| FAIL_ON_UNKNOWN_PROPERTIES | 禁用 | 忽略未知 JSON 属性 |
| WRITE_DATES_AS_TIMESTAMPS | 禁用 | 日期序列化为字符串而非时间戳 |

### 时区配置

使用 JVM 默认时区：`TimeZone.getDefault()`

## 使用方式

无需手动配置，引入 `jsf-waf` 模块后自动激活。如需排除：

```java
@SpringBootApplication(exclude = {WafConfig.class})
public class MyApp {}
```