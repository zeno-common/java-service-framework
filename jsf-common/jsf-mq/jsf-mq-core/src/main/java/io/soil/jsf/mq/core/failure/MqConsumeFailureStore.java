package io.soil.jsf.mq.core.failure;

import java.util.List;

/**
 * 消费失败记录存储接口（依赖倒置：核心只定义协议，落库方式由实现模块提供，如 jsf-mq-mongodb）。
 *
 * @author zeno.w
 */
public interface MqConsumeFailureStore {

    /**
     * 持久化失败记录，实现方须回填 {@link MqConsumeFailureRecord#setId(Long)}。
     *
     * @param record 失败记录
     */
    void save(MqConsumeFailureRecord record);

    /**
     * 拉取待重放（{@link MqConsumeFailureStatus#PENDING}）的失败记录。
     *
     * @param limit 最大条数
     * @return 待重放记录列表（按失败时间升序）
     */
    List<MqConsumeFailureRecord> fetchPending(int limit);

    /**
     * 标记记录为已重放（{@link MqConsumeFailureStatus#REPLAYED}）。
     *
     * @param id 记录 ID
     */
    void markReplayed(Long id);
}
