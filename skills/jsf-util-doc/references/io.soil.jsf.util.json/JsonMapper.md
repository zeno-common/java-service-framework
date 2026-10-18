# JsonMapper

> JSON 映射工具类，封装 Jackson `ObjectMapper` 提供对象与 JSON 的序列化/反序列化。

- **包**: `io.soil.jsf.util.json`
- **修饰**: `final`，私有构造（纯静态工具）
- **内置配置**: JavaTimeModule、ISO8601 日期格式、枚举 toString 序列化/反序列化；禁用 FAIL_ON_UNKNOWN_PROPERTIES 与 WRITE_DATES_AS_TIMESTAMPS

序列化异常时记录日志并返回兜底值（`"{}"` / `NullNode` / `null`），不抛出。

## 方法

### toString

`static String toString(Object object)`

> Java 对象转 JSON 字符串；null 或异常时返回 `"{}"`。

### toJsonNode

`static JsonNode toJsonNode(Object object)` — Java 对象转 JsonNode
`static JsonNode toJsonNode(String jsonContent)` — JSON 字符串转 JsonNode（异常返回 NullNode）

### toObject

`static <T> T toObject(String jsonContent, Class<T> clazz)` — JSON 字符串转对象
`static <T> T toObject(InputStream in, Class<T> clazz)` — 输入流转对象
`static <T> T toObject(String jsonString, TypeReference<T> typeRef)` — JSON 字符串转泛型对象
`static <T> T toObject(JsonNode value, Class<T> clazz)` — JsonNode 转对象
`static <T> T toObject(InputStream in, TypeReference<T> typeRef)` — 输入流转泛型对象

> 入参为 null 或解析异常时返回 null。

**参数**:
- `clazz` (Class&lt;T&gt;) — 目标类型
- `typeRef` (TypeReference&lt;T&gt;) — 泛型类型引用

**返回**: T — 反序列化对象

**示例**:
```java
String json = JsonMapper.toString(user);
User user = JsonMapper.toObject(json, User.class);
List<User> users = JsonMapper.toObject(json, new TypeReference<List<User>>() {});
```
