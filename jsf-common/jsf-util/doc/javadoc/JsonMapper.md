# JsonMapper

`io.soil.util.json.JsonMapper`

JSON 映射工具类，封装 Jackson `ObjectMapper` 提供对象与 JSON 之间的序列化/反序列化操作。内置配置了 Java 8 时间模块、枚举字符串序列化等特性。

## 类签名

```java
public final class JsonMapper
```

## 内置配置

| 配置项 | 值 | 说明 |
|--------|-----|------|
| JavaTimeModule | 注册 | 支持 Java 8 日期时间类型 |
| 日期格式 | ISO 8601 | `yyyy-MM-dd'T'HH:mm:ss.SSSZ` |
| WRITE_ENUMS_USING_TO_STRING | 启用 | 枚举序列化使用 `toString()` |
| READ_ENUMS_USING_TO_STRING | 启用 | 枚举反序列化使用 `toString()` |
| FAIL_ON_UNKNOWN_PROPERTIES | 禁用 | 忽略未知 JSON 属性 |
| WRITE_DATES_AS_TIMESTAMPS | 禁用 | 日期序列化为字符串而非时间戳 |

## 静态方法

### toString(Object)

```java
public static String toString(Object object)
```

Java 对象转换成 JSON 字符串。对象为 `null` 时返回 `"{}"`。

```java
User user = new User();
user.setId(1L);
user.setName("Alice");

String json = JsonMapper.toString(user);
// {"id":1,"name":"Alice"}

String empty = JsonMapper.toString(null);
// "{}"
```

### toJsonNode(Object)

```java
public static JsonNode toJsonNode(Object object)
```

Java 对象转换成 `JsonNode` 对象。

```java
User user = new User();
user.setName("Alice");

JsonNode node = JsonMapper.toJsonNode(user);
String name = node.get("name").asText();
// "Alice"
```

### toJsonNode(String)

```java
public static JsonNode toJsonNode(String jsonContent)
```

JSON 字符串转换成 `JsonNode` 对象。解析失败时返回 `NullNode`。

```java
JsonNode node = JsonMapper.toJsonNode("{\"name\":\"Alice\",\"age\":30}");
String name = node.get("name").asText();
int age = node.get("age").asInt();
```

### toObject(String, Class<T>)

```java
public static <T> T toObject(String jsonContent, Class<T> clazz)
```

JSON 字符串转换成 Java 对象。输入为 `null` 或解析失败时返回 `null`。

```java
String json = "{\"id\":1,\"name\":\"Alice\"}";
User user = JsonMapper.toObject(json, User.class);
```

### toObject(InputStream, Class<T>)

```java
public static <T> T toObject(InputStream in, Class<T> clazz)
```

输入流转换成 Java 对象。输入为 `null` 或解析失败时返回 `null`。

```java
InputStream is = new FileInputStream("user.json");
User user = JsonMapper.toObject(is, User.class);
```

### toObject(String, TypeReference<T>)

```java
public static <T> T toObject(String jsonString, TypeReference<T> typeRef)
```

JSON 字符串转换成带泛型的 Java 对象。适用于泛型集合等场景。

```java
String json = "[{\"id\":1},{\"id\":2}]";
List<User> users = JsonMapper.toObject(json, new TypeReference<List<User>>() {});
```

### toObject(JsonNode, Class<T>)

```java
public static <T> T toObject(JsonNode value, Class<T> clazz)
```

JsonNode 对象转换成 Java 对象。

```java
JsonNode node = JsonMapper.toJsonNode("{\"id\":1,\"name\":\"Alice\"}");
User user = JsonMapper.toObject(node, User.class);
```

### toObject(InputStream, TypeReference<T>)

```java
public static <T> T toObject(InputStream in, TypeReference<T> typeRef)
```

输入流转换成带泛型的 Java 对象。

```java
InputStream is = new FileInputStream("users.json");
List<User> users = JsonMapper.toObject(is, new TypeReference<List<User>>() {});
```