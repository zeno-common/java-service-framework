# MqSendOptions

> 发送元信息选项，承载消息级元信息（不污染业务 payload）。

- **包**: `io.soil.jsf.mq.core`
- **Lombok**: `@Getter` `@Setter` `@Builder` `@NoArgsConstructor` `@AllArgsConstructor`

元信息通过 RocketMQ keys / 用户属性承载，保持链路字节为纯业务 JSON。消费侧通过 [MqConsumeContext](#MqConsumeContext) 读取。

## 字段

| 字段 | 类型 | 说明 |
|------|------|------|
| `keys` | String | RocketMQ keys（业务键/幂等键），消费侧 `getKeys()` 可读 |
| `properties` | Map&lt;String,String&gt; | 自定义用户属性（traceId/eventType/schemaVersion），消费侧 `getProperty(k)` 可读 |

## 常量

| 常量 | 类型 | 说明 |
|------|------|------|
| `EMPTY` | MqSendOptions | 空选项单例（等同不携带元信息） |

## Builder 模式

`@Builder` [Lombok 生成]：

```java
MqSendOptions options = MqSendOptions.builder()
    .keys(orderId)
    .properties(Map.of("traceId", tid, "eventType", "ORDER_CREATED"))
    .build();
producer.send("order-topic", payload, options);
```

- **相关**: [MqProducer](#MqProducer)、[MqConsumeContext](#MqConsumeContext)
