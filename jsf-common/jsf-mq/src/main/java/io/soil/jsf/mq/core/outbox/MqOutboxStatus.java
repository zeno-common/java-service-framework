package io.soil.jsf.mq.core.outbox;

/**
 * Outbox 消息状态。
 * <p>
 * 状态机：{@code PENDING → SENDING → SENT}；发送失败且未耗尽重试回到 {@code PENDING}（带退避），
 * 重试耗尽进入终态 {@code FAILED}（需人工介入）。
 * </p>
 *
 * @author zeno.w
 */
public enum MqOutboxStatus {

    /** 待发送（含发送失败后等待退避重试） */
    PENDING,

    /** 已被认领，发送中（认领带锁超时 lockExpireAt，锁过期视为僵尸行可被重新认领） */
    SENDING,

    /** 发送成功（终态） */
    SENT,

    /** 重试耗尽，终态失败（需人工介入） */
    FAILED
}
