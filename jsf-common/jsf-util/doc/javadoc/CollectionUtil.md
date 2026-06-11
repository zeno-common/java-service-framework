# CollectionUtil

`io.soil.common.collection.CollectionUtil`

集合工具类，提供集合转换、映射等常用操作。

## 类签名

```java
public class CollectionUtil
```

## 静态方法

### toMap(Collection<VALUE>, Function)

```java
public static <KEY, VALUE> Map<KEY, VALUE> toMap(
    Collection<VALUE> values,
    Function<VALUE, KEY> keyMapper
)
```

将集合转换为 Map，使用指定的 key 映射函数提取键，值保持不变。

```java
List<User> users = List.of(user1, user2);
Map<Long, User> userMap = CollectionUtil.toMap(users, User::getId);
// {1=user1, 2=user2}
```

### mapList(Collection<T>, Function<T,R>)

```java
public static <T, R> List<R> mapList(
    Collection<T> source,
    Function<T, R> mapper
)
```

将源集合中的元素映射为新的列表。源集合为空或 `null` 时返回空列表。

```java
List<User> users = List.of(user1, user2);
List<String> names = CollectionUtil.mapList(users, User::getName);
// ["Alice", "Bob"]

// 空集合安全
List<String> empty = CollectionUtil.mapList(null, User::getName);
// []
```

### mapList(Collection<S>, FunctionWithParam, P)

```java
public static <S, R, P> List<R> mapList(
    Collection<S> source,
    FunctionWithParam<S, R, P> mapper,
    P param1
)
```

将源集合中的元素使用带一个额外参数的映射函数映射为新的列表。

```java
List<Order> orders = List.of(order1, order2);
List<OrderVO> vos = CollectionUtil.mapList(orders, (order, ctx) -> {
    OrderVO vo = new OrderVO();
    vo.setName(order.getName());
    vo.setUserName(ctx.getUserName(order.getUserId()));
    return vo;
}, userContext);
```

### mapList(Collection<S>, FunctionWith2Param, P1, P2)

```java
public static <S, R, P1, P2> List<R> mapList(
    Collection<S> source,
    FunctionWith2Param<S, R, P1, P2> mapper,
    P1 p1, P2 p2
)
```

将源集合中的元素使用带两个额外参数的映射函数映射为新的列表。

```java
List<Order> orders = List.of(order1, order2);
List<OrderVO> vos = CollectionUtil.mapList(orders,
    (order, userCtx, config) -> convertOrder(order, userCtx, config),
    userContext, appConfig
);
```

### mapList(Collection<S>, FunctionWith3Param, P1, P2, P3)

```java
public static <S, R, P1, P2, P3> List<R> mapList(
    Collection<S> source,
    FunctionWith3Param<S, R, P1, P2, P3> mapper,
    P1 p1, P2 p2, P3 p3
)
```

将源集合中的元素使用带三个额外参数的映射函数映射为新的列表。

```java
List<Order> orders = List.of(order1, order2);
List<OrderVO> vos = CollectionUtil.mapList(orders,
    (order, ctx, cfg, locale) -> convertOrder(order, ctx, cfg, locale),
    userContext, appConfig, locale
);
```