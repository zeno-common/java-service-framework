package io.soil.jsf.mq.mongodb.doc;

import io.soil.jsf.mongodb.doc.BaseMongoDoc;
import io.soil.jsf.mq.core.failure.MqConsumeFailureStatus;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.OffsetDateTime;

/**
 * 消费失败记录文档，collection {@code mq_consume_failure}。
 * <p>状态字段按枚举 {@code name()} 存为字符串，可直接在库里按 {@code status: "PENDING"} 查询。</p>
 *
 * @author zeno.w
 */
@Getter
@Setter
@Document(collection = "mq_consume_failure")
public class MqConsumeFailureDoc extends BaseMongoDoc<Long> {

    @Field("topic")
    private String topic;

    @Field("tag")
    private String tag;

    @Field("consumerGroup")
    private String consumerGroup;

    @Field("consumerClass")
    private String consumerClass;

    @Field("messageId")
    private String messageId;

    @Field("bizKey")
    private String bizKey;

    /** 消息体 JSON */
    @Field("payload")
    private String payload;

    @Field("errorMsg")
    private String errorMsg;

    @Field("stackTrace")
    private String stackTrace;

    /** 记录状态（枚举，按 name() 存字符串） */
    @Indexed
    @Field("status")
    private MqConsumeFailureStatus status;

    @Field("failedAt")
    private OffsetDateTime failedAt;

    @Field("replayedAt")
    private OffsetDateTime replayedAt;
}
