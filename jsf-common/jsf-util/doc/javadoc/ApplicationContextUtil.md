# ApplicationContextUtil

`io.soil.util.spring.ApplicationContextUtil`

Spring ApplicationContext 工具类，提供在非 Spring 管理的类中获取 Bean 的能力。通过 `ApplicationContextAware` 接口自动注入应用上下文。

## 类签名

```java
@Component
public class ApplicationContextUtil implements ApplicationContextAware
```

## 静态方法

### getBean(String name)

```java
public static <T> T getBean(String name) throws BeansException
```

根据名称获取 Bean 实例。

```java
UserService userService = ApplicationContextUtil.getBean("userService");
```

### getBean(Class<T> clazz)

```java
public static <T> T getBean(Class<T> clazz) throws BeansException
```

根据类型获取 Bean 实例。

```java
UserService userService = ApplicationContextUtil.getBean(UserService.class);
```

## 使用场景

在非 Spring 管理的类（如工具类、枚举、实体类）中获取 Spring Bean：

```java
public class OrderStatusHandler {

    public void process(Order order) {
        // 在非 Spring 管理的类中获取 Bean
        OrderService orderService = ApplicationContextUtil.getBean(OrderService.class);
        orderService.updateStatus(order.getId(), order.getStatus());
    }
}
```