# CollectionUtil

> 集合工具类，提供集合转换、映射等常用操作。

- **包**: `io.soil.jsf.common.collection`

## 方法

### toMap

`static <KEY,VALUE> Map<KEY,VALUE> toMap(Collection<VALUE> values, Function<VALUE,KEY> keyMapper)`

> 以 keyMapper 提取的键为键、原元素为值构建 Map。

**参数**:
- `values` (Collection&lt;VALUE&gt;) — 源集合
- `keyMapper` (Function&lt;VALUE,KEY&gt;) — 键映射函数

**返回**: Map&lt;KEY,VALUE&gt;

### mapList

`static <T,R> List<R> mapList(Collection<T> source, Function<T,R> mapper)`
`static <S,R,P> List<R> mapList(Collection<S> source, FunctionWithParam<S,R,P> mapper, P param1)`
`static <S,R,P1,P2> List<R> mapList(Collection<S> source, FunctionWith2Param<S,R,P1,P2> mapper, P1 p1, P2 p2)`
`static <S,R,P1,P2,P3> List<R> mapList(Collection<S> source, FunctionWith3Param<S,R,P1,P2,P3> mapper, P1 p1, P2 p2, P3 p3)`

> 将源集合元素映射为新列表，支持 0~3 个额外参数。源集合为空时返回空列表。

**参数**:
- `source` (Collection) — 源集合
- `mapper` — 映射函数（[FunctionWithParam](#FunctionWithParam) 等）
- `param1` / `p1` / `p2` / `p3` — 额外参数

**返回**: List&lt;R&gt; — 映射后的新列表

**示例**:
```java
Map<Long, User> byId = CollectionUtil.toMap(users, User::getId);
List<String> names = CollectionUtil.mapList(users, User::getName);
```
