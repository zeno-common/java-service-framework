# MqConsumeFailureHandler

> 消费失败处理器：封装失败落库与重放编排，屏蔽底层 [MqConsumeFailureStore](#MqConsumeFailureStore) 细节。

- **包**: `io.soil.jsf.mq.core.failure`
- **注解**: `@Slf4j`
- **构造**: `MqConsumeFailureHandler(MqConsumeFailureStore store)`

## 方法

### save

`void save(MqConsumeFailureRecord record)`

> 持久化失败记录。存储异常仅记日志、不向上抛出，避免"写失败记录失败"反噬消费线程。

**参数**: `record` (MqConsumeFailureRecord) — 失败记录

### replay

`int replay(int limit, Consumer<MqConsumeFailureRecord> replayer)`

> 重放待处理记录：拉取 PENDING 记录逐条交给业务回调，成功即标记 REPLAYED，单条失败保持 PENDING 不影响其余。通常由管理端接口或定时任务触发。

**参数**:
- `limit` (int) — 单次最大重放条数
- `replayer` (Consumer&lt;MqConsumeFailureRecord&gt;) — 业务重放逻辑（抛异常则该条保持 PENDING）

**返回**: int — 成功重放条数

**示例**:
```java
int n = failureHandler.replay(100, record -> {
    OrderDto dto = JsonMapper.toObject(record.getPayload(), OrderDto.class);
    orderService.retryHandle(dto); // 业务重放
});
```

- **相关**: [MqConsumeFailureStore](#MqConsumeFailureStore)、[MqConsumeFailureRecord](#MqConsumeFailureRecord)
