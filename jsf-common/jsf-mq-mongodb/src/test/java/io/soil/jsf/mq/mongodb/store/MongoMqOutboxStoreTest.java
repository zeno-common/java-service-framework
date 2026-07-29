package io.soil.jsf.mq.mongodb.store;

import io.soil.jsf.mq.core.outbox.MqOutboxMessage;
import io.soil.jsf.mq.core.outbox.MqOutboxStatus;
import io.soil.jsf.mq.mongodb.doc.MqOutboxDoc;
import org.junit.jupiter.api.Test;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class MongoMqOutboxStoreTest {

    @Test
    void insert_backfillsId() {
        MongoTemplate template = mock(MongoTemplate.class);
        doAnswer(inv -> {
            MqOutboxDoc doc = inv.getArgument(0);
            doc.setId(123L);
            return doc;
        }).when(template).insert(any(MqOutboxDoc.class));
        MongoMqOutboxStore store = new MongoMqOutboxStore(template);

        MqOutboxMessage msg = new MqOutboxMessage();
        msg.setTopic("t");
        msg.setPayload("{}");
        store.insert(msg);

        assertEquals(123L, msg.getId());
    }

    @Test
    void claim_findAndModifyNull_returnsFalse() {
        MongoTemplate template = mock(MongoTemplate.class);
        when(template.findAndModify(any(Query.class), any(Update.class), eq(MqOutboxDoc.class)))
                .thenReturn(null);
        MongoMqOutboxStore store = new MongoMqOutboxStore(template);

        assertFalse(store.claim(123L, System.currentTimeMillis() + 60_000));
    }

    @Test
    void claim_findAndModifyHits_returnsTrue() {
        MongoTemplate template = mock(MongoTemplate.class);
        when(template.findAndModify(any(Query.class), any(Update.class), eq(MqOutboxDoc.class)))
                .thenReturn(new MqOutboxDoc());
        MongoMqOutboxStore store = new MongoMqOutboxStore(template);

        assertTrue(store.claim(123L, System.currentTimeMillis() + 60_000));
    }

    @Test
    void fetchPending_mapsDocToMessage() {
        MongoTemplate template = mock(MongoTemplate.class);
        MqOutboxDoc doc = new MqOutboxDoc();
        doc.setId(1L);
        doc.setTopic("t");
        doc.setTag("tag");
        doc.setPayload("{}");
        doc.setStatus(MqOutboxStatus.PENDING);
        doc.setAttempt(2);
        when(template.find(any(Query.class), eq(MqOutboxDoc.class))).thenReturn(List.of(doc));
        MongoMqOutboxStore store = new MongoMqOutboxStore(template);

        List<MqOutboxMessage> list = store.fetchPending(10, System.currentTimeMillis());

        assertEquals(1, list.size());
        MqOutboxMessage msg = list.get(0);
        assertEquals(1L, msg.getId());
        assertEquals("t:tag", msg.destination());
        assertEquals(MqOutboxStatus.PENDING, msg.getStatus());
        assertEquals(2, msg.getAttempt());
    }

    @Test
    void markSentFailedDead_updateStatus() {
        MongoTemplate template = mock(MongoTemplate.class);
        MongoMqOutboxStore store = new MongoMqOutboxStore(template);

        store.markSent(1L);
        store.markFailed(1L, 3, 12345L);
        store.markDead(1L, 16);

        verify(template, org.mockito.Mockito.times(3))
                .updateFirst(any(Query.class), any(Update.class), eq(MqOutboxDoc.class));
    }
}
