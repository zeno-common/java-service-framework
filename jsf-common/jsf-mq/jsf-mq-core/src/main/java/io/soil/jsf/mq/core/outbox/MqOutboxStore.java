package io.soil.jsf.mq.core.outbox;

import java.util.List;

/**
 * Outbox 存储接口（依赖倒置：核心只定义协议，落库方式由实现模块提供，如 jsf-mq-mongodb）。
 * <p>
 * 实现方须保证 {@link #claim(Long, long)} 的<b>原子性</b>（如 MongoDB findAndModify、
 * SQL {@code UPDATE ... WHERE status='PENDING'} 判断影响行数），以支撑集群多实例并发安全。
 * </p>
 *
 * @author zeno.w
 */
public interface MqOutboxStore {

    /**
     * 插入 Outbox 消息（应在业务事务内调用），实现方须回填 {@link MqOutboxMessage#setId(Long)}。
     *
     * @param msg Outbox 消息
     */
    void insert(MqOutboxMessage msg);

    /**
     * 拉取可投递的消息：
     * <ul>
     *   <li>{@code status=PENDING 且 nextRetryAt <= now}</li>
     *   <li>{@code status=SENDING 且 lockExpireAt < now}（僵尸行：认领后崩溃未完成）</li>
     * </ul>
     *
     * @param limit     最大条数
     * @param nowMillis 当前时间（epoch millis）
     * @return 待投递消息列表（按 nextRetryAt 升序）
     */
    List<MqOutboxMessage> fetchPending(int limit, long nowMillis);

    /**
     * 原子认领：{@code PENDING → SENDING}（或接管锁过期的 SENDING 僵尸行）并写入 lockExpireAt。
     *
     * @param id           消息 ID
     * @param lockExpireAt 锁过期时间（epoch millis）
     * @return true=认领成功可发送；false=已被其他实例认领或状态不符
     */
    boolean claim(Long id, long lockExpireAt);

    /**
     * 标记发送成功（终态 {@link MqOutboxStatus#SENT}）。
     *
     * @param id 消息 ID
     */
    void markSent(Long id);

    /**
     * 标记单次发送失败：回到 {@link MqOutboxStatus#PENDING} 并设置退避后的下次重试时间。
     *
     * @param id          消息 ID
     * @param attempt     累计尝试次数
     * @param nextRetryAt 下次可重试时间（epoch millis）
     */
    void markFailed(Long id, int attempt, long nextRetryAt);

    /**
     * 标记重试耗尽（终态 {@link MqOutboxStatus#FAILED}，需人工介入）。
     *
     * @param id      消息 ID
     * @param attempt 累计尝试次数
     */
    void markDead(Long id, int attempt);
}
