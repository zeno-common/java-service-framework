package io.soil.jsf.mq.core;

import io.soil.jsf.mq.core.failure.MqConsumeFailureHandler;
import io.soil.jsf.mq.core.failure.MqConsumeFailureRecord;
import io.soil.jsf.mq.core.idempotent.MqIdempotentStore;
import io.soil.jsf.mq.exception.MqConsumerException;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AbstractMqConsumerTest {

    /** 便于测试的可配置消费者 */
    static class TestConsumer extends AbstractMqConsumer<String> {
        ConsumeStatus result = ConsumeStatus.SUCCESS;
        RuntimeException toThrow;
        String key;
        int handled;

        @Override
        protected ConsumeStatus handleMessage(String message) {
            handled++;
            if (toThrow != null) {
                throw toThrow;
            }
            return result;
        }

        @Override
        protected String idempotentKey(String message) {
            return key;
        }
    }

    @Test
    void success_acksNormally() {
        TestConsumer consumer = new TestConsumer();
        assertDoesNotThrow(() -> consumer.onMessage("hello"));
        assertEquals(1, consumer.handled);
    }

    @Test
    void nullStatus_treatedAsSuccess() {
        TestConsumer consumer = new TestConsumer();
        consumer.result = null;
        assertDoesNotThrow(() -> consumer.onMessage("x"));
    }

    @Test
    void exception_defaultsToRetryLater_throwsMqConsumerException() {
        TestConsumer consumer = new TestConsumer();
        consumer.toThrow = new RuntimeException("boom");
        assertThrows(MqConsumerException.class, () -> consumer.onMessage("x"));
    }

    @Test
    void retryLater_throwsMqConsumerException() {
        TestConsumer consumer = new TestConsumer();
        consumer.result = ConsumeStatus.RETRY_LATER;
        assertThrows(MqConsumerException.class, () -> consumer.onMessage("x"));
    }

    @Test
    void discard_savesFailureRecord_withoutThrowing() {
        TestConsumer consumer = new TestConsumer();
        consumer.result = ConsumeStatus.DISCARD;
        MqConsumeFailureHandler handler = mock(MqConsumeFailureHandler.class);
        consumer.setFailureHandler(handler);

        assertDoesNotThrow(() -> consumer.onMessage("bad-payload"));

        ArgumentCaptor<MqConsumeFailureRecord> captor = ArgumentCaptor.forClass(MqConsumeFailureRecord.class);
        verify(handler).save(captor.capture());
        assertEquals("bad-payload", captor.getValue().getPayload());
        assertEquals(TestConsumer.class.getName(), captor.getValue().getConsumerClass());
    }

    @Test
    void discard_withoutHandler_doesNotThrow() {
        TestConsumer consumer = new TestConsumer();
        consumer.result = ConsumeStatus.DISCARD;
        assertDoesNotThrow(() -> consumer.onMessage("x"));
    }

    @Test
    void idempotent_duplicate_skipsHandleMessage() {
        TestConsumer consumer = new TestConsumer();
        consumer.key = "k1";
        MqIdempotentStore store = mock(MqIdempotentStore.class);
        when(store.tryClaim(eq("k1"), anyLong())).thenReturn(false);
        consumer.setIdempotentStore(store);

        consumer.onMessage("dup");

        assertEquals(0, consumer.handled);
    }

    @Test
    void idempotent_success_marksProcessed() {
        TestConsumer consumer = new TestConsumer();
        consumer.key = "k1";
        MqIdempotentStore store = mock(MqIdempotentStore.class);
        when(store.tryClaim(eq("k1"), anyLong())).thenReturn(true);
        consumer.setIdempotentStore(store);

        consumer.onMessage("msg");

        verify(store).markProcessed("k1");
        verify(store, never()).release(any());
    }

    @Test
    void idempotent_retryLater_releasesClaim() {
        TestConsumer consumer = new TestConsumer();
        consumer.key = "k1";
        consumer.result = ConsumeStatus.RETRY_LATER;
        MqIdempotentStore store = mock(MqIdempotentStore.class);
        when(store.tryClaim(eq("k1"), anyLong())).thenReturn(true);
        consumer.setIdempotentStore(store);

        assertThrows(MqConsumerException.class, () -> consumer.onMessage("msg"));

        verify(store).release("k1");
        verify(store, never()).markProcessed(any());
    }

    @Test
    void idempotent_discard_releasesClaim() {
        TestConsumer consumer = new TestConsumer();
        consumer.key = "k1";
        consumer.result = ConsumeStatus.DISCARD;
        MqIdempotentStore store = mock(MqIdempotentStore.class);
        when(store.tryClaim(eq("k1"), anyLong())).thenReturn(true);
        consumer.setIdempotentStore(store);

        consumer.onMessage("msg");

        verify(store).release("k1");
    }
}
