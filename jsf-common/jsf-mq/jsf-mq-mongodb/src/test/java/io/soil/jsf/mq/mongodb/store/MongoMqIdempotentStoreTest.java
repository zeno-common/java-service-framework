package io.soil.jsf.mq.mongodb.store;

import io.soil.jsf.mq.mongodb.doc.MqIdempotentDoc;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class MongoMqIdempotentStoreTest {

    @Test
    void tryClaim_insertSucceeds_returnsTrue() {
        MongoTemplate template = mock(MongoTemplate.class);
        MongoMqIdempotentStore store = new MongoMqIdempotentStore(template);

        assertTrue(store.tryClaim("k1", 300));

        verify(template).insert(any(MqIdempotentDoc.class));
    }

    @Test
    void tryClaim_duplicate_andNotExpired_returnsFalse() {
        MongoTemplate template = mock(MongoTemplate.class);
        when(template.insert(any(MqIdempotentDoc.class))).thenThrow(new DuplicateKeyException("dup"));
        when(template.findAndModify(any(Query.class), any(Update.class), eq(MqIdempotentDoc.class)))
                .thenReturn(null);
        MongoMqIdempotentStore store = new MongoMqIdempotentStore(template);

        assertFalse(store.tryClaim("k1", 300));
    }

    @Test
    void tryClaim_duplicate_butStaleClaim_takesOver() {
        MongoTemplate template = mock(MongoTemplate.class);
        when(template.insert(any(MqIdempotentDoc.class))).thenThrow(new DuplicateKeyException("dup"));
        when(template.findAndModify(any(Query.class), any(Update.class), eq(MqIdempotentDoc.class)))
                .thenReturn(new MqIdempotentDoc());
        MongoMqIdempotentStore store = new MongoMqIdempotentStore(template);

        assertTrue(store.tryClaim("k1", 300));
    }

    @Test
    void markProcessed_updatesStatus() {
        MongoTemplate template = mock(MongoTemplate.class);
        MongoMqIdempotentStore store = new MongoMqIdempotentStore(template);

        store.markProcessed("k1");

        verify(template).updateFirst(any(Query.class), any(Update.class), eq(MqIdempotentDoc.class));
    }

    @Test
    void release_removesClaimedOnly() {
        MongoTemplate template = mock(MongoTemplate.class);
        MongoMqIdempotentStore store = new MongoMqIdempotentStore(template);

        store.release("k1");

        verify(template).remove(any(Query.class), eq(MqIdempotentDoc.class));
    }
}
