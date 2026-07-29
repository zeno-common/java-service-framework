package io.soil.jsf.mq.mongodb.doc;

import io.soil.jsf.mongodb.doc.BaseMongoDoc;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

/**
 * 消费幂等记录文档，collection {@code mq_idempotent}。
 * <p>
 * {@code key} 唯一索引保证 tryClaim 原子性（重复 insert 触发 DuplicateKeyException）；
 * {@code expireAt} TTL 索引自动清理历史记录（需开启 {@code spring.data.mongodb.auto-index-creation}
 * 或手工建索引）。
 * </p>
 *
 * @author zeno.w
 */
@Getter
@Setter
@Document(collection = "mq_idempotent")
public class MqIdempotentDoc extends BaseMongoDoc<Long> {

    /** 声明中 */
    public static final String STATUS_CLAIMED = "CLAIMED";
    /** 已处理完成（终态） */
    public static final String STATUS_PROCESSED = "PROCESSED";

    /** 幂等键（唯一） */
    @Indexed(unique = true)
    @Field("key")
    private String key;

    /** 状态：CLAIMED / PROCESSED */
    @Field("status")
    private String status;

    /** 声明过期时间（epoch millis），CLAIMED 超过该时间视为失效可被接管 */
    @Field("claimExpireAt")
    private long claimExpireAt;

    /** 文档 TTL 清理时间（Mongo TTL 索引，expireAfterSeconds=0） */
    @Indexed(expireAfterSeconds = 0)
    @Field("expireAt")
    private Date expireAt;
}
