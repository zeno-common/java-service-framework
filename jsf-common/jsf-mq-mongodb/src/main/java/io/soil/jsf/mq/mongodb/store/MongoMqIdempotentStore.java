package io.soil.jsf.mq.mongodb.store;

import io.soil.jsf.mq.core.idempotent.MqIdempotentStore;
import io.soil.jsf.mq.mongodb.doc.MqIdempotentDoc;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.Date;

/**
 * {@link MqIdempotentStore} 的 MongoDB 实现。
 * <p>
 * 原子性保证：
 * <ul>
 *   <li>{@code tryClaim} 依赖 {@code key} 唯一索引 —— insert 成功即声明成功；
 *       DuplicateKey 时尝试 findAndModify 原子接管"声明已过期"的记录（消费者崩溃场景）；</li>
 *   <li>{@code release} 仅删除 CLAIMED 状态记录，不会误删已完成（PROCESSED）的终态记录。</li>
 * </ul>
 * 历史记录由 {@code expireAt} TTL 索引自动清理（保留期默认 7 天）。
 * </p>
 *
 * @author zeno.w
 */
public class MongoMqIdempotentStore implements MqIdempotentStore {

    private static final long DEFAULT_RETENTION_MILLIS = 7L * 24 * 3600 * 1000;

    private final MongoTemplate mongoTemplate;
    private final long retentionMillis;

    public MongoMqIdempotentStore(MongoTemplate mongoTemplate) {
        this(mongoTemplate, DEFAULT_RETENTION_MILLIS);
    }

    public MongoMqIdempotentStore(MongoTemplate mongoTemplate, long retentionMillis) {
        this.mongoTemplate = mongoTemplate;
        this.retentionMillis = retentionMillis;
    }

    @Override
    public boolean tryClaim(String key, long ttlSeconds) {
        long now = System.currentTimeMillis();
        MqIdempotentDoc doc = new MqIdempotentDoc();
        doc.setKey(key);
        doc.setStatus(MqIdempotentDoc.STATUS_CLAIMED);
        doc.setClaimExpireAt(now + ttlSeconds * 1000L);
        doc.setExpireAt(new Date(now + retentionMillis));
        try {
            mongoTemplate.insert(doc);
            return true;
        } catch (DuplicateKeyException e) {
            // 已存在：仅当旧声明为 CLAIMED 且已过期时原子接管（防消费者崩溃后 key 永久占用）
            Query query = Query.query(Criteria.where("key").is(key)
                    .and("status").is(MqIdempotentDoc.STATUS_CLAIMED)
                    .and("claimExpireAt").lt(now));
            Update update = Update.update("claimExpireAt", now + ttlSeconds * 1000L);
            return mongoTemplate.findAndModify(query, update, MqIdempotentDoc.class) != null;
        }
    }

    @Override
    public void markProcessed(String key) {
        Query query = Query.query(Criteria.where("key").is(key));
        Update update = Update.update("status", MqIdempotentDoc.STATUS_PROCESSED);
        mongoTemplate.updateFirst(query, update, MqIdempotentDoc.class);
    }

    @Override
    public void release(String key) {
        Query query = Query.query(Criteria.where("key").is(key)
                .and("status").is(MqIdempotentDoc.STATUS_CLAIMED));
        mongoTemplate.remove(query, MqIdempotentDoc.class);
    }
}
