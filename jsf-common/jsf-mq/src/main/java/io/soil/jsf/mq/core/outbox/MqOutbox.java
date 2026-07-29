package io.soil.jsf.mq.core.outbox;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.soil.jsf.mq.config.properties.MqProperties;
import io.soil.jsf.mq.core.MqProducer;
import io.soil.jsf.mq.exception.MqException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 * Outbox 编排入口：<b>事务内落库 + 事务提交后立即投递（immediate dispatch），relay 兜底</b>。
 * <p>
 * 时序：
 * <ol>
 *   <li>{@link #save(String, String, Object)} 在业务事务内 insert（PENDING）——与业务写库同事务，保证原子性；</li>
 *   <li>事务提交后（afterCommit 回调）执行 {@link #dispatch(MqOutboxMessage)}：claim → 发送 → markSent；</li>
 *   <li>立即投递失败仅记 warn（业务事务已提交，不能影响主流程），由 {@link MqOutboxRelay} 定时兜底补发；</li>
 *   <li>无活动事务时 insert 后直接投递。</li>
 * </ol>
 * insert 时 {@code nextRetryAt = now + initialBackoffSeconds}，让 relay 天然避开立即投递的窗口，减少竞争。
 * </p>
 * <p>
 * 语义为 at-least-once（如发送成功但 markSent 前崩溃，relay 会重发僵尸行），
 * 消费端必须配合 {@code MqIdempotentStore} 幂等去重。
 * </p>
 *
 * @author zeno.w
 */
@Slf4j
public class MqOutbox {

    private final MqOutboxStore store;
    private final MqProducer producer;
    private final ObjectMapper objectMapper;
    private final MqProperties.Outbox props;

    public MqOutbox(MqOutboxStore store, MqProducer producer,
                    ObjectMapper objectMapper, MqProperties.Outbox props) {
        this.store = store;
        this.producer = producer;
        this.objectMapper = objectMapper;
        this.props = props;
    }

    /**
     * 落库 Outbox 消息（应在业务事务内调用），事务提交后自动立即投递。
     *
     * @param topic   目标 topic
     * @param tag     消息 tag（可空）
     * @param payload 业务消息体（序列化为 JSON 存储）
     * @return 已落库的 Outbox 消息（id 已回填）
     */
    public MqOutboxMessage save(String topic, String tag, Object payload) {
        MqOutboxMessage msg = new MqOutboxMessage();
        msg.setTopic(topic);
        msg.setTag(tag);
        msg.setPayload(serialize(topic, payload));
        msg.setStatus(MqOutboxStatus.PENDING);
        msg.setAttempt(0);
        msg.setNextRetryAt(System.currentTimeMillis() + props.getInitialBackoffSeconds() * 1000L);
        store.insert(msg);

        if (props.isImmediateSend()) {
            if (TransactionSynchronizationManager.isSynchronizationActive()) {
                TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                    @Override
                    public void afterCommit() {
                        safeDispatch(msg);
                    }
                });
            } else {
                safeDispatch(msg);
            }
        }
        return msg;
    }

    /**
     * 投递单条 Outbox 消息：原子认领 → 发送 → 标记结果。供立即投递与 relay 兜底共用。
     *
     * @param msg Outbox 消息
     * @return true=发送成功；false=认领失败或发送失败（失败已按退避/终态落库）
     */
    public boolean dispatch(MqOutboxMessage msg) {
        long now = System.currentTimeMillis();
        if (!store.claim(msg.getId(), now + props.getLockSeconds() * 1000L)) {
            return false;
        }
        try {
            producer.send(msg.destination(), msg.getPayload());
            store.markSent(msg.getId());
            return true;
        } catch (Exception e) {
            int attempt = msg.getAttempt() + 1;
            if (attempt >= props.getMaxAttempts()) {
                store.markDead(msg.getId(), attempt);
                log.error("Outbox 消息重试耗尽，置为 FAILED（需人工介入）id={}, destination={}, attempt={}",
                        msg.getId(), msg.destination(), attempt, e);
            } else {
                long nextRetryAt = now + backoffMillis(attempt);
                store.markFailed(msg.getId(), attempt, nextRetryAt);
                log.warn("Outbox 消息发送失败，等待 relay 补发 id={}, destination={}, attempt={}, nextRetryAt={}",
                        msg.getId(), msg.destination(), attempt, nextRetryAt, e);
            }
            return false;
        }
    }

    /**
     * 指数退避：initialBackoff * 2^(attempt-1)，上限 maxBackoffSeconds。
     */
    long backoffMillis(int attempt) {
        long backoffSeconds = props.getInitialBackoffSeconds() << Math.min(attempt - 1, 20);
        return Math.min(backoffSeconds, props.getMaxBackoffSeconds()) * 1000L;
    }

    /** 立即投递路径：任何异常只记 warn，不影响业务主流程（relay 会兜底）。 */
    private void safeDispatch(MqOutboxMessage msg) {
        try {
            dispatch(msg);
        } catch (Exception e) {
            log.warn("Outbox 立即投递异常，等待 relay 兜底 id={}, destination={}", msg.getId(), msg.destination(), e);
        }
    }

    private String serialize(String topic, Object payload) {
        try {
            return objectMapper.writeValueAsString(payload);
        } catch (Exception e) {
            throw MqException.sendFailed(e, "Outbox 消息序列化失败 topic={0}", topic);
        }
    }
}
