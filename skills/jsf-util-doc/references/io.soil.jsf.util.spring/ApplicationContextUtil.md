# ApplicationContextUtil

> Spring ApplicationContext 工具类，提供在非 Spring 管理的类中获取 Bean 的能力。

- **包**: `io.soil.jsf.util.spring`
- **注解**: `@Component`
- **实现**: `ApplicationContextAware`

## 方法

### setApplicationContext

`void setApplicationContext(ApplicationContext applicationContext) throws BeansException`

> Spring 回调注入上下文（框架调用，无需手动使用）。

### getBean

`static <T> T getBean(String name) throws BeansException`
`static <T> T getBean(Class<T> clazz) throws BeansException`

> 按名称或类型从上下文获取 Bean。

**参数**:
- `name` (String) — Bean 注册名称
- `clazz` (Class&lt;T&gt;) — Bean 类型

**返回**: T — Bean 实例

**异常**: `BeansException` — 获取失败

**示例**:
```java
UserService svc = ApplicationContextUtil.getBean(UserService.class);
```
