# FunctionWithParam<S,R,P>

> 带一个额外参数的函数式接口，扩展标准 Function。

- **包**: `io.soil.jsf.common.collection`
- **类型**: `@FunctionalInterface`

## 方法

### apply

`R apply(S s, P p)`

> 应用函数。

**参数**:
- `s` (S) — 源对象
- `p` (P) — 额外参数

**返回**: R — 执行结果

- **相关**: [CollectionUtil#mapList](#CollectionUtil)、`FunctionWith2Param`、`FunctionWith3Param`
