# MqConsumerException

> jsf-mq 消费者侧统一异常，`type()` 为 `SYS`。

- **包**: `io.soil.jsf.mq.exception`
- **父类**: `io.soil.jsf.common.exception.BaseException`

错误码采用 `[业务]-[编码]` 命名规约。消息支持 `MessageFormat` 格式化。

## 常量

| 常量 | 值 | 说明 |
|------|----|------|
| `MQ_CONSUME_FAILED` | `MQ-CONSUME-FAILED` | 消息消费失败 |

## 构造

| 签名 | 说明 |
|------|------|
| `MqConsumerException(String code, String msgPattern, Object... msgArgs)` | 码 + 消息模板 |
| `MqConsumerException(String code, Throwable cause, String msgPattern, Object... msgArgs)` | 码 + 原因 + 消息模板 |

## 方法

### consumeFailed

`static MqConsumerException consumeFailed(Throwable cause, String msgPattern, Object... msgArgs)`

> 构造消息消费失败异常（保留原始异常链），code=`MQ-CONSUME-FAILED`。常用于触发 broker 重试。

### type

`ExceptionType type()`

> 固定返回 `SYS`。

- **相关**: [AbstractMqConsumer](#AbstractMqConsumer)、`io.soil.jsf.common.exception.BaseException`
