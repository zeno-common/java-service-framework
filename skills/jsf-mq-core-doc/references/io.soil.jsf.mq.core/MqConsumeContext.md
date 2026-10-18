# MqConsumeContext

> 消费上下文，封装消息元信息（不侵入业务 payload）。

- **包**: `io.soil.jsf.mq.core`
- **构造**: `MqConsumeContext(MessageExt messageExt)`

由 [AbstractMqConsumer](#AbstractMqConsumer) 从 RocketMQ `MessageExt` 提取，供 `handleMessage` / `idempotentKey` 读取 msgId/keys/traceId 等，用于幂等、链路追踪与失败排查。

## 方法

### getMessageId

`String getMessageId()` — 消息 ID（RocketMQ msgId）

### getTopic

`String getTopic()` — 消息 topic

### getTag

`String getTag()` — 消息 tag（真实 tag，非消费端 selector 表达式）

### getBornTimestamp

`long getBornTimestamp()` — born 时间戳（epoch millis）

### getKeys

`String getKeys()` — RocketMQ keys（发送时设置的 `MqSendOptions.keys`）

### getProperties

`Map<String,String> getProperties()` — 原始属性表（系统 + 用户属性，不可变）

### getProperty

`String getProperty(String key)` — 读取指定自定义用户属性（如 traceId / eventType）

- **相关**: [MqSendOptions](#MqSendOptions)、[AbstractMqConsumer](#AbstractMqConsumer)
