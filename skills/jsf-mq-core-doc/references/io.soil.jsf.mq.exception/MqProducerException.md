# MqProducerException

> jsf-mq 生产者侧统一异常，`type()` 为 `SYS`。

- **包**: `io.soil.jsf.mq.exception`
- **父类**: `io.soil.jsf.common.exception.BaseException`

错误码采用 `[业务]-[编码]` 命名规约。消息支持 `MessageFormat` 格式化。

## 常量

| 常量 | 值 | 说明 |
|------|----|------|
| `MQ_SEND_FAILED` | `MQ-SEND-FAILED` | 消息发送失败 |
| `MQ_PRODUCER_NOT_AVAILABLE` | `MQ-PRODUCER-NOT-AVAILABLE` | 生产者不可用（RocketMQ 未初始化 / Template 缺失） |

## 构造

| 签名 | 说明 |
|------|------|
| `MqProducerException(String code, String msgPattern, Object... msgArgs)` | 码 + 消息模板 |
| `MqProducerException(String code, Throwable cause, String msgPattern, Object... msgArgs)` | 码 + 原因 + 消息模板 |

## 方法

### sendFailed

`static MqProducerException sendFailed(Throwable cause, String msgPattern, Object... msgArgs)`

> 构造消息发送失败异常（保留原始异常链），code=`MQ-SEND-FAILED`。

### producerNotAvailable

`static MqProducerException producerNotAvailable(String msgPattern, Object... msgArgs)`

> 构造生产者不可用异常，code=`MQ-PRODUCER-NOT-AVAILABLE`。

### type

`ExceptionType type()`

> 固定返回 `SYS`。

- **相关**: [MqProducer](#MqProducer)、`io.soil.jsf.common.exception.BaseException`
