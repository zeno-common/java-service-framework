package io.soil.jsf.mq.mongodb.store;

import io.soil.jsf.mq.core.failure.MqConsumeFailureRecord;
import io.soil.jsf.mq.core.failure.MqConsumeFailureStatus;
import io.soil.jsf.mq.core.failure.MqConsumeFailureStore;
import io.soil.jsf.mq.mongodb.doc.MqConsumeFailureDoc;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.time.OffsetDateTime;
import java.util.List;

/**
 * {@link MqConsumeFailureStore} 的 MongoDB 实现，collection {@code mq_consume_failure}。
 *
 * @author zeno.w
 */
public class MongoMqConsumeFailureStore implements MqConsumeFailureStore {

    private final MongoTemplate mongoTemplate;

    public MongoMqConsumeFailureStore(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public void save(MqConsumeFailureRecord record) {
        MqConsumeFailureDoc doc = new MqConsumeFailureDoc();
        doc.setTopic(record.getTopic());
        doc.setTag(record.getTag());
        doc.setConsumerGroup(record.getConsumerGroup());
        doc.setConsumerClass(record.getConsumerClass());
        doc.setMessageId(record.getMessageId());
        doc.setBizKey(record.getBizKey());
        doc.setPayload(record.getPayload());
        doc.setErrorMsg(record.getErrorMsg());
        doc.setStackTrace(record.getStackTrace());
        doc.setStatus(record.getStatus());
        doc.setFailedAt(record.getFailedAt());
        mongoTemplate.insert(doc);
        record.setId(doc.getId());
    }

    @Override
    public List<MqConsumeFailureRecord> fetchPending(int limit) {
        Query query = Query.query(Criteria.where("status").is(MqConsumeFailureStatus.PENDING))
                .with(Sort.by(Sort.Direction.ASC, "failedAt"))
                .limit(limit);
        return mongoTemplate.find(query, MqConsumeFailureDoc.class).stream()
                .map(MongoMqConsumeFailureStore::toRecord)
                .toList();
    }

    @Override
    public void markReplayed(Long id) {
        Query query = Query.query(Criteria.where("_id").is(id));
        Update update = Update.update("status", MqConsumeFailureStatus.REPLAYED)
                .set("replayedAt", OffsetDateTime.now());
        mongoTemplate.updateFirst(query, update, MqConsumeFailureDoc.class);
    }

    private static MqConsumeFailureRecord toRecord(MqConsumeFailureDoc doc) {
        MqConsumeFailureRecord record = new MqConsumeFailureRecord();
        record.setId(doc.getId());
        record.setTopic(doc.getTopic());
        record.setTag(doc.getTag());
        record.setConsumerGroup(doc.getConsumerGroup());
        record.setConsumerClass(doc.getConsumerClass());
        record.setMessageId(doc.getMessageId());
        record.setBizKey(doc.getBizKey());
        record.setPayload(doc.getPayload());
        record.setErrorMsg(doc.getErrorMsg());
        record.setStackTrace(doc.getStackTrace());
        record.setStatus(doc.getStatus());
        record.setFailedAt(doc.getFailedAt());
        record.setReplayedAt(doc.getReplayedAt());
        return record;
    }
}
