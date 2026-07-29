package io.soil.jsf.mq.mongodb.doc;

import io.soil.jsf.mongodb.doc.BaseMongoDoc;
import io.soil.jsf.mq.core.outbox.MqOutboxStatus;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * Outbox 消息文档，collection {@code mq_outbox}。
 * <p>
 * 复合索引 {@code (status, nextRetryAt)} 支撑 relay 的 fetchPending 扫描；
 * 状态字段按枚举 {@code name()} 存为字符串。
 * </p>
 *
 * @author zeno.w
 */
@Getter
@Setter
@Document(collection = "mq_outbox")
@CompoundIndex(name = "idx_status_next_retry", def = "{'status': 1, 'nextRetryAt': 1}")
public class MqOutboxDoc extends BaseMongoDoc<Long> {

    @Field("topic")
    private String topic;

    @Field("tag")
    private String tag;

    /** 业务消息体 JSON */
    @Field("payload")
    private String payload;

    /** 状态（枚举，按 name() 存字符串） */
    @Field("status")
    private MqOutboxStatus status;

    /** 已尝试发送次数 */
    @Field("attempt")
    private int attempt;

    /** 下次可重试时间（epoch millis） */
    @Field("nextRetryAt")
    private long nextRetryAt;

    /** 认领锁过期时间（epoch millis） */
    @Field("lockExpireAt")
    private long lockExpireAt;
}
