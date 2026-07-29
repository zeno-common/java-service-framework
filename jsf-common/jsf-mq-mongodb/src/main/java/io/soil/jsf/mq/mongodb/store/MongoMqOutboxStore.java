package io.soil.jsf.mq.mongodb.store;

import io.soil.jsf.mq.core.outbox.MqOutboxMessage;
import io.soil.jsf.mq.core.outbox.MqOutboxStatus;
import io.soil.jsf.mq.core.outbox.MqOutboxStore;
import io.soil.jsf.mq.mongodb.doc.MqOutboxDoc;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.List;

/**
 * {@link MqOutboxStore} 的 MongoDB 实现。
 * <p>
 * {@code claim} 使用 findAndModify 原子完成 {@code PENDING → SENDING}（或接管锁过期的
 * SENDING 僵尸行），集群多实例并发下保证单行只被一个实例认领。
 * </p>
 * <p>
 * 注意：{@code insert} 与业务写库的原子性依赖调用方处于同一个 Mongo 事务
 * （{@code MongoTransactionManager} + 副本集）。
 * </p>
 *
 * @author zeno.w
 */
public class MongoMqOutboxStore implements MqOutboxStore {

    private final MongoTemplate mongoTemplate;

    public MongoMqOutboxStore(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public void insert(MqOutboxMessage msg) {
        MqOutboxDoc doc = new MqOutboxDoc();
        doc.setTopic(msg.getTopic());
        doc.setTag(msg.getTag());
        doc.setPayload(msg.getPayload());
        doc.setStatus(msg.getStatus());
        doc.setAttempt(msg.getAttempt());
        doc.setNextRetryAt(msg.getNextRetryAt());
        doc.setLockExpireAt(msg.getLockExpireAt());
        mongoTemplate.insert(doc);
        msg.setId(doc.getId());
    }

    @Override
    public List<MqOutboxMessage> fetchPending(int limit, long nowMillis) {
        Criteria pendingDue = Criteria.where("status").is(MqOutboxStatus.PENDING)
                .and("nextRetryAt").lte(nowMillis);
        Criteria zombieSending = Criteria.where("status").is(MqOutboxStatus.SENDING)
                .and("lockExpireAt").lt(nowMillis);
        Query query = Query.query(new Criteria().orOperator(pendingDue, zombieSending))
                .with(Sort.by(Sort.Direction.ASC, "nextRetryAt"))
                .limit(limit);
        return mongoTemplate.find(query, MqOutboxDoc.class).stream()
                .map(MongoMqOutboxStore::toMessage)
                .toList();
    }

    @Override
    public boolean claim(Long id, long lockExpireAt) {
        long now = System.currentTimeMillis();
        Criteria pending = Criteria.where("status").is(MqOutboxStatus.PENDING);
        Criteria zombieSending = Criteria.where("status").is(MqOutboxStatus.SENDING)
                .and("lockExpireAt").lt(now);
        Query query = Query.query(Criteria.where("_id").is(id)
                .orOperator(pending, zombieSending));
        Update update = Update.update("status", MqOutboxStatus.SENDING)
                .set("lockExpireAt", lockExpireAt);
        return mongoTemplate.findAndModify(query, update, MqOutboxDoc.class) != null;
    }

    @Override
    public void markSent(Long id) {
        mongoTemplate.updateFirst(byId(id),
                Update.update("status", MqOutboxStatus.SENT), MqOutboxDoc.class);
    }

    @Override
    public void markFailed(Long id, int attempt, long nextRetryAt) {
        Update update = Update.update("status", MqOutboxStatus.PENDING)
                .set("attempt", attempt)
                .set("nextRetryAt", nextRetryAt);
        mongoTemplate.updateFirst(byId(id), update, MqOutboxDoc.class);
    }

    @Override
    public void markDead(Long id, int attempt) {
        Update update = Update.update("status", MqOutboxStatus.FAILED)
                .set("attempt", attempt);
        mongoTemplate.updateFirst(byId(id), update, MqOutboxDoc.class);
    }

    private static Query byId(Long id) {
        return Query.query(Criteria.where("_id").is(id));
    }

    private static MqOutboxMessage toMessage(MqOutboxDoc doc) {
        MqOutboxMessage msg = new MqOutboxMessage();
        msg.setId(doc.getId());
        msg.setTopic(doc.getTopic());
        msg.setTag(doc.getTag());
        msg.setPayload(doc.getPayload());
        msg.setStatus(doc.getStatus());
        msg.setAttempt(doc.getAttempt());
        msg.setNextRetryAt(doc.getNextRetryAt());
        msg.setLockExpireAt(doc.getLockExpireAt());
        return msg;
    }
}
