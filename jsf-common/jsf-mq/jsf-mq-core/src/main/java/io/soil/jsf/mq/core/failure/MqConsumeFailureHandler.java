package io.soil.jsf.mq.core.failure;

import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.function.Consumer;

/**
 * 消费失败处理器：封装失败落库与重放编排，屏蔽底层 {@link MqConsumeFailureStore} 细节。
 * <p>
 * <b>落库</b>：{@link #save(MqConsumeFailureRecord)} 内部吞掉存储异常（仅记日志），
 * 避免"失败记录写入失败"反过来影响消费线程。
 * </p>
 * <p>
 * <b>重放</b>：{@link #replay(int, Consumer)} 拉取待重放记录逐条交给业务回调，
 * 回调成功即标记 REPLAYED，单条失败不影响其余记录。通常由管理端接口或定时任务触发。
 * </p>
 *
 * @author zeno.w
 */
@Slf4j
public class MqConsumeFailureHandler {

    private final MqConsumeFailureStore store;

    public MqConsumeFailureHandler(MqConsumeFailureStore store) {
        this.store = store;
    }

    /**
     * 持久化失败记录（存储异常仅记日志，不向上抛出）。
     *
     * @param record 失败记录
     */
    public void save(MqConsumeFailureRecord record) {
        try {
            store.save(record);
            log.info("消费失败记录已落库 id={}, topic={}, consumer={}, error={}",
                    record.getId(), record.getTopic(), record.getConsumerClass(), record.getErrorMsg());
        } catch (Exception e) {
            log.error("消费失败记录落库失败（消息上下文见本条日志）topic={}, consumer={}, payload={}",
                    record.getTopic(), record.getConsumerClass(), record.getPayload(), e);
        }
    }

    /**
     * 重放待处理的失败记录。
     *
     * @param limit    单次最大重放条数
     * @param replayer 业务重放逻辑（抛异常则该条保持 PENDING，下次可再重放）
     * @return 成功重放条数
     */
    public int replay(int limit, Consumer<MqConsumeFailureRecord> replayer) {
        List<MqConsumeFailureRecord> records = store.fetchPending(limit);
        int success = 0;
        for (MqConsumeFailureRecord record : records) {
            try {
                replayer.accept(record);
                store.markReplayed(record.getId());
                success++;
            } catch (Exception e) {
                log.error("失败记录重放未成功，保持 PENDING id={}, topic={}", record.getId(), record.getTopic(), e);
            }
        }
        return success;
    }
}
