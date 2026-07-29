package io.soil.jsf.mq.core.idempotent;

/**
 * 消费幂等存储接口（依赖倒置：核心只定义协议，落库方式由实现模块提供，如 jsf-mq-mongodb）。
 * <p>
 * 协议为三段式 claim / markProcessed / release：
 * <ol>
 *   <li>消费前 {@link #tryClaim(String, long)} 原子声明处理权，返回 false 表示重复投递应跳过；</li>
 *   <li>处理成功后 {@link #markProcessed(String)} 标记完成（终态，后续重复投递直接跳过）；</li>
 *   <li>处理失败时 {@link #release(String)} 释放声明，允许 broker 重试再次进入。</li>
 * </ol>
 * 声明自带过期时间（ttlSeconds），避免消费者崩溃后声明永久占用导致消息丢失。
 * </p>
 *
 * @author zeno.w
 */
public interface MqIdempotentStore {

    /**
     * 原子声明消息处理权。
     *
     * @param key        幂等键（如 messageId / bizKey）
     * @param ttlSeconds 声明有效期（秒）。超过该时间未标记完成的声明视为失效，可被重新声明
     * @return true=首次声明成功，可以处理；false=已被声明或已处理完成，应跳过
     */
    boolean tryClaim(String key, long ttlSeconds);

    /**
     * 处理成功后标记为已完成（终态）。
     *
     * @param key 幂等键
     */
    void markProcessed(String key);

    /**
     * 处理失败时释放声明，允许后续重试再次进入处理。
     *
     * @param key 幂等键
     */
    void release(String key);
}
