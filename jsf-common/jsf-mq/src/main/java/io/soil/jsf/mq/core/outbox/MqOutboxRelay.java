package io.soil.jsf.mq.core.outbox;

import io.soil.jsf.mq.config.properties.MqProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;

/**
 * Outbox 兜底补发器：定时扫描待投递行（PENDING 到期 + SENDING 锁过期僵尸行），
 * 逐条走 {@link MqOutbox#dispatch(MqOutboxMessage)}（含原子认领），集群多实例并发安全。
 * <p>
 * 依赖应用开启 {@code @EnableScheduling}；扫描间隔由 {@code jsf.mq.outbox.relay.interval} 控制（默认 5000ms）。
 * </p>
 *
 * @author zeno.w
 */
@Slf4j
public class MqOutboxRelay {

    private final MqOutboxStore store;
    private final MqOutbox outbox;
    private final MqProperties.Outbox props;

    public MqOutboxRelay(MqOutboxStore store, MqOutbox outbox, MqProperties.Outbox props) {
        this.store = store;
        this.outbox = outbox;
        this.props = props;
    }

    /**
     * 扫描并补发一批待投递消息。
     *
     * @return 本轮成功发送条数
     */
    @Scheduled(fixedDelayString = "${jsf.mq.outbox.relay.interval:5000}")
    public int relay() {
        List<MqOutboxMessage> batch = store.fetchPending(props.getRelay().getBatchSize(), System.currentTimeMillis());
        if (batch.isEmpty()) {
            return 0;
        }
        int sent = 0;
        for (MqOutboxMessage msg : batch) {
            try {
                if (outbox.dispatch(msg)) {
                    sent++;
                }
            } catch (Exception e) {
                log.warn("Outbox relay 补发异常 id={}, destination={}", msg.getId(), msg.destination(), e);
            }
        }
        log.info("Outbox relay 本轮扫描 {} 条，成功补发 {} 条", batch.size(), sent);
        return sent;
    }
}
