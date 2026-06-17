# `JsonMapper`

`package io.soil.util.json`

`@Slf4j`

JSON 映射工具类，封装 Jackson ObjectMapper 提供对象与 JSON 之间的序列化/反序列化操作。

内置配置了 Java 8 时间模块、枚举字符串序列化等特性。

- **Author:** zeno.w
- **Since:** 2021/12/9

---

## Methods

### `static String toString(Object object)`

→ **String** — json 字符串

Java 对象转换成 json 字符串

| Param | Type | Description |
|-------|------|-------------|
| `object` | `Object` | 转换对象 |

---

### `static JsonNode toJsonNode(Object object)`

→ **JsonNode** — JsonNode 对象

java 对象转换成 JsonNode 对象

| Param | Type | Description |
|-------|------|-------------|
| `object` | `Object` | java 对象 |

---

### `static JsonNode toJsonNode(String jsonContent)`

→ **JsonNode** — JsonNode 对象

json 字符串转成 JsonNode 对象

| Param | Type | Description |
|-------|------|-------------|
| `jsonContent` | `String` | json 字符串 |

---

### `static <T> T toObject(String jsonContent, Class<T> clazz)`

→ **T** — java 对象

json 字符串转成 java 对象

| Param | Type | Description |
|-------|------|-------------|
| `jsonContent` | `String` | json 字符串 |
| `clazz` | `Class<T>` | java Class |
| `<T>` | | 要转换的对象类型 |

---

### `static <T> T toObject(InputStream in, Class<T> clazz)`

→ **T** — java 对象

字符输入流对象转成 java 对象

| Param | Type | Description |
|-------|------|-------------|
| `in` | `InputStream` | 字符输入流对象 |
| `clazz` | `Class<T>` | java Class |
| `<T>` | | 要转换的对象类型 |

---

### `static <T> T toObject(String jsonString, TypeReference<T> typeRef)`

→ **T** — java 对象

字符输入流对象转成 java 对象

| Param | Type | Description |
|-------|------|-------------|
| `jsonString` | `String` | json 字符串 |
| `typeRef` | `TypeReference<T>` | 类型引用 |
| `<T>` | | 要转换的对象类型 |

---

### `static <T> T toObject(JsonNode value, Class<T> clazz)`

→ **T** — java 对象

JsonNode 对象转换成 java 对象

| Param | Type | Description |
|-------|------|-------------|
| `value` | `JsonNode` | JsonNode 对象 |
| `clazz` | `Class<T>` | java Class |
| `<T>` | | 要转换的对象类型 |

---

### `static <T> T toObject(InputStream in, TypeReference<T> typeRef)`

→ **T** — java 对象

字符输入流对象转成 java 对象

| Param | Type | Description |
|-------|------|-------------|
| `in` | `InputStream` | 字符输入流对象 |
| `typeRef` | `TypeReference<T>` | 类型引用 |
| `<T>` | | 要转换的对象类型 |