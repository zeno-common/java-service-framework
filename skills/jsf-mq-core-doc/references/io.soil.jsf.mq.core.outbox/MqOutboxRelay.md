# MqOutboxRelay

> Outbox 兜底补发器：定时扫描待投递行（PENDING 到期 + SENDING 锁过期僵尸行），逐条走 dispatch，集群多实例并发安全。

- **包**: `io.soil.jsf.mq.core.outbox`
- **注解**: `@Slf4j`
- **构造**: `MqOutboxRelay(MqOutboxStore store, MqOutbox outbox, MqProducerProperties.Outbox props)`

依赖应用开启 `@EnableScheduling`；扫描间隔由 `jsf.mq.producer.outbox.relay.interval` 控制（默认 5000ms）。

## 方法

### relay

`@Scheduled(fixedDelayString = "${jsf.mq.producer.outbox.relay.interval:5000}") public int relay()`

> 扫描并补发一批待投递消息（批量大小 = `relay.batchSize`）。

**返回**: int — 本轮成功发送条数

- **相关**: [MqOutbox](#MqOutbox)、[MqOutboxStore](#MqOutboxStore)
