# WafConfig

> WAF 配置类，配置 Jackson 序列化/反序列化规则和组件扫描

- **包**: io.soil.waf.config
- **注解**: `@Configuration`, `@ComponentScan`

## 方法

### jackson2ObjectMapperBuilder

`Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilder()`

> 配置 Jackson ObjectMapper 构建器，统一 JSON 序列化/反序列化规则

**返回**: `Jackson2ObjectMapperBuilderCustomizer` — 配置好的定制器

**配置项**:

| 类别 | 配置 | 说明 |
|------|------|------|
| 序列化 | `Long` / `Long.TYPE` / `BigInteger` → `ToStringSerializer` | 数字精度在 JS 中会丢失，转为字符串 |
| 序列化 | `LocalDateTime` → `LocalDateTimeSerializer` | 使用 `OFFSET_DATE_TIME_FORMATTER` 格式 |
| 反序列化 | `LocalDateTime` ← `LocalDateTimeDeserializer` | 使用 `OFFSET_DATE_TIME_FORMATTER` 格式 |
| 启用 | `WRITE_ENUMS_USING_TO_STRING` | 枚举使用字符串序列化 |
| 启用 | `READ_ENUMS_USING_TO_STRING` | 枚举使用字符串反序列化 |
| 禁用 | `FAIL_ON_UNKNOWN_PROPERTIES` | 忽略未知属性 |
| 禁用 | `WRITE_DATES_AS_TIMESTAMPS` | 日期不写为时间戳 |

**示例**:
```java
// WafConfig 通过 @Configuration 自动注册，无需手动使用
// 引入 jsf-waf 依赖后，Jackson 序列化规则自动生效
```
