# UnknownEnumException

> 未知枚举状态值异常，`type()` 为 `UNDEFINED`。

- **包**: `io.soil.jsf.common.exception`
- **父类**: [BaseException](#BaseException)

## 构造

| 签名 | 说明 |
|------|------|
| `UnknownEnumException(Object status, Class<? extends Enum> enumClass)` | 消息为 `unknown status '<status>' for enum: <enumClass>` |

## 方法

### type

`ExceptionType type()`

> 固定返回 `UNDEFINED`。

- **相关**: 常在枚举 `of(int)` 反查失败时抛出，见 [EnumSample#of](#EnumSample)
