# WsfConfig

> WSF 配置类，注册 Jackson 定制器并组件扫描 `ProbeController`、`RestGlobalExceptionResolver`。

- **包**: `io.soil.jsf.wsf.config`

## 方法

### jackson2ObjectMapperBuilder

`Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilder()`

> 统一 JSON 序列化/反序列化规则，作为 `@Bean` 交由 Spring Boot Jackson 自动装配应用。

**配置要点**:
- Long / BigInteger → String（防 JS 精度丢失）
- LocalDateTime 按 `OffsetDateTimeUtil.OFFSET_DATE_TIME_FORMATTER` 序列化/反序列化
- 枚举按字符串读写：启用 `WRITE_ENUMS_USING_TO_STRING` / `READ_ENUMS_USING_TO_STRING`
- 禁用：`FAIL_ON_UNKNOWN_PROPERTIES`、`WRITE_DATES_AS_TIMESTAMPS`
- 时区：`TimeZone.getDefault()`

**返回**: Jackson2ObjectMapperBuilderCustomizer — Jackson ObjectMapper 构建定制器
