# EnumSample

> 枚举定义样例，示范带 status 的枚举标准写法。

- **包**: `io.soil.jsf.common.constant`
- **类型**: `enum`，实现 `IEnum<Integer>`（MyBatis-Flex）

## 枚举值

| 名称 | status | desc |
|------|--------|------|
| `SAMPLE` | 0 | 示例 |

## 方法

### status

`Integer status()`

> 获取枚举状态值。

### of

`static EnumSample of(int status)`

> 按 status 反查枚举，遍历所有值匹配。

**参数**:
- `status` (int) — 状态值

**返回**: EnumSample

**异常**: `UnknownEnumException` — 状态值不在枚举定义范围内
