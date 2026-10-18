# jsf-util

JSF 通用工具集，沉淀与业务无关的基础能力：日期时间、JSON、集合、树模型、IP、分页、Spring 上下文、异常基类等。引入即通过 `JsfCommonAutoConfig` 完成组件扫描。

## 功能特性

| 分类 | 关键类 | 能力 |
|------|--------|------|
| 日期时间 | `OffsetDateTimeUtil` / `DateTimeConst` | ISO 8601 格式化与解析、当天起止时间、毫秒时间戳、多种 `Temporal` 互转 |
| JSON | `JsonMapper` | 封装 Jackson `ObjectMapper`：对象↔JSON↔JsonNode，内置 Java 8 时间模块、枚举字符串化、忽略未知属性、禁用时间戳 |
| 集合 | `CollectionUtil` | `toMap`、`mapList`（支持 1~3 个额外参数的映射函数），空集合安全返回 |
| 树模型 | `TreeUtils` / `Node` | `list ↔ tree ↔ map` 三态转换 |
| IP | `IpUtil` | 获取本地与指定远程 IP:Port 连接时使用的本机地址 |
| 分页 | `JsfPagingUtil` / `JsfUrlParams` | 基于 REST URL 参数（pageNo/pageSize 或 offset/limit）驱动 PageHelper 分页与排序 |
| Spring | `ApplicationContextUtil` | 在非 Spring 托管类中按名称/类型获取 Bean |
| 异常体系 | `BaseException` 及派生类 | 统一异常基类，含类型、状态码、`MessageFormat` 消息模板 |

## 引入依赖

前置条件：通过 [jsf-bom](../../jsf-bom/README.md) 管理依赖版本。

```xml
<dependency>
    <groupId>io.soil.jsf</groupId>
    <artifactId>jsf-util</artifactId>
</dependency>
```

> `spring-context` / `spring-web` / `pagehelper` / `mybatis-flex-core` / `jakarta.servlet-api` 等均为 `provided` 作用域，仅在对应能力（分页、Web、Spring 上下文）被使用时由宿主项目提供。

## 自动配置

引入后自动加载 `JsfCommonAutoConfig`，扫描 `io.soil.jsf.util` 包下的组件（如 `ApplicationContextUtil`），无需手动配置。

## 使用示例

### 日期时间

```java
String iso = OffsetDateTimeUtil.toString(OffsetDateTime.now());     // ISO 8601 带时区
String dayStart = OffsetDateTimeUtil.toDayStartString(localDate);   // 当天 00:00:00
OffsetDateTime odt = OffsetDateTimeUtil.from(instant);              // 多种 Temporal 归一
```

### JSON

```java
String json = JsonMapper.toString(obj);
User user = JsonMapper.toObject(json, User.class);
List<User> list = JsonMapper.toObject(json, new TypeReference<List<User>>() {});
```

### 集合与树

```java
Map<Long, User> byId = CollectionUtil.toMap(users, User::getId);
List<String> names = CollectionUtil.mapList(users, User::getName);
List<Category> tree = TreeUtils.toTree(flatList);                   // list → tree
```

### 分页

```java
Page<User> page = JsfPagingUtil.pageByUrlParam();                   // 读取 URL 的 pageNo/pageSize/offset/limit/order
```

### 异常基类

业务异常继承 `BaseException`，实现 `type()` 返回 `ExceptionType`（BIZ / SYS / PARAM 等），支持状态码与消息模板：

```java
public class BizException extends BaseException {
    public BizException(String code, String msgPattern, Object... args) {
        super(code, null, msgPattern, args);
    }
    @Override
    public ExceptionType type() {
        return ExceptionType.BIZ;
    }
}
// 用法：throw new BizException("UC-NOT-FOUND", "用户 {0} 不存在", userId);
```

> `BaseException.getStackTraceString()` 仅在 DEBUG 级别返回完整堆栈，非 DEBUG 返回空串，避免生产日志冗余。

## 项目结构

```
io.soil.jsf/
├── common/
│   ├── collection/   CollectionUtil + FunctionWith{,2,3}Param
│   ├── date/         OffsetDateTimeUtil, DateTimeConst
│   ├── exception/    BaseException, BizException, SysException, ParamException, Error, ExceptionType, UnknownEnumException
│   ├── tree/         Node, TreeUtils
│   └── constant/     EnumSample
└── util/
    ├── config/       JsfCommonAutoConfig
    ├── ip/           IpUtil
    ├── jdbc/         JsfPagingUtil, JsfUrlParams, JsfUrlParamsPage, JsfUrlParamConst
    ├── json/         JsonMapper
    └── spring/       ApplicationContextUtil
```
