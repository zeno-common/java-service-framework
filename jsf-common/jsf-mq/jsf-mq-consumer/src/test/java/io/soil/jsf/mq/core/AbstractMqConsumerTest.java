package io.soil.jsf.mq.core;

import io.soil.jsf.mq.core.failure.MqConsumeFailureHandler;
import io.soil.jsf.mq.core.failure.MqConsumeFailureRecord;
import io.soil.jsf.mq.core.failure.MqConsumeFailureReason;
import io.soil.jsf.mq.core.idempotent.MqIdempotentStore;
import io.soil.jsf.mq.exception.MqConsumerException;
import org.apache.rocketmq.common.message.MessageExt;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
        protected ConsumeStatus handleMessage(String message, MqConsumeContext ctx) {
            handled++;
            if (toThrow != null) {
                throw toThrow;
            }
            return result;
        }

        @Override
        protected String idempotentKey(String message, MqConsumeContext ctx) {
            return key;
        }
    }

    /** 用字符串体构造 MessageExt，模拟 RocketMQ 投递 */
    private static MessageExt msg(String body) {
        MessageExt ext = new MessageExt();
        ext.setTopic("t");
        ext.setTags("tag");
        ext.setBody(body.getBytes(StandardCharsets.UTF_8));
        return ext;
    }

    @Test
    void success_acksNormally() {
        TestConsumer consumer = new TestConsumer();
        assertDoesNotThrow(() -> consumer.onMessage(msg("hello")));
        assertEquals(1, consumer.handled);
    }

    @Test
    void nullStatus_treatedAsSuccess() {
        TestConsumer consumer = new TestConsumer();
        consumer.result = null;
        assertDoesNotThrow(() -> consumer.onMessage(msg("x")));
    }

    @Test
    void exception_defaultsToRetryLater_throwsMqConsumerException() {
        TestConsumer consumer = new TestConsumer();
        consumer.toThrow = new RuntimeException("boom");
        assertThrows(MqConsumerException.class, () -> consumer.onMessage(msg("x")));
    }

    @Test
    void retryLater_throwsMqConsumerException() {
        TestConsumer consumer = new TestConsumer();
        consumer.result = ConsumeStatus.RETRY_LATER;
        assertThrows(MqConsumerException.class, () -> consumer.onMessage(msg("x")));
    }

    @Test
    void discard_savesFailureRecord_withoutThrowing() {
        TestConsumer consumer = new TestConsumer();
        consumer.result = ConsumeStatus.DISCARD;
        MqConsumeFailureHandler handler = mock(MqConsumeFailureHandler.class);
        consumer.setFailureHandler(handler);

        assertDoesNotThrow(() -> consumer.onMessage(msg("bad-payload")));

        ArgumentCaptor<MqConsumeFailureRecord> captor = ArgumentCaptor.forClass(MqConsumeFailureRecord.class);
        verify(handler).save(captor.capture());
        assertEquals("bad-payload", captor.getValue().getPayload());
        assertEquals(TestConsumer.class.getName(), captor.getValue().getConsumerClass());
        assertEquals(MqConsumeFailureReason.DISCARDED, captor.getValue().getReason());
    }

    @Test
    void retryExhausted_savesFailureRecord_andThrows() {
        TestConsumer consumer = new TestConsumer();
        consumer.toThrow = new RuntimeException("boom");
        MqConsumeFailureHandler handler = mock(MqConsumeFailureHandler.class);
        consumer.setFailureHandler(handler);

        MessageExt m = msg("x");
        m.setReconsumeTimes(15); // 默认 maxReconsumeTimes=16，15 为最后一次投递

        assertThrows(MqConsumerException.class, () -> consumer.onMessage(m));

        ArgumentCaptor<MqConsumeFailureRecord> captor = ArgumentCaptor.forClass(MqConsumeFailureRecord.class);
        verify(handler).save(captor.capture());
        assertEquals(MqConsumeFailureReason.RETRY_EXHAUSTED, captor.getValue().getReason());
        assertEquals("boom", captor.getValue().getErrorMsg());
        assertNotNull(captor.getValue().getStackTrace());
    }

    @Test
    void retryNotExhausted_doesNotSaveFailureRecord() {
        TestConsumer consumer = new TestConsumer();
        consumer.toThrow = new RuntimeException("boom");
        MqConsumeFailureHandler handler = mock(MqConsumeFailureHandler.class);
        consumer.setFailureHandler(handler);

        // reconsumeTimes=0（首次投递），未到末次，不应落库
        assertThrows(MqConsumerException.class, () -> consumer.onMessage(msg("x")));
        verify(handler, never()).save(any());
    }

    @Test
    void discard_withoutHandler_doesNotThrow() {
        TestConsumer consumer = new TestConsumer();
        consumer.result = ConsumeStatus.DISCARD;
        assertDoesNotThrow(() -> consumer.onMessage(msg("x")));
    }

    @Test
    void idempotent_duplicate_skipsHandleMessage() {
        TestConsumer consumer = new TestConsumer();
        consumer.key = "k1";
        MqIdempotentStore store = mock(MqIdempotentStore.class);
        when(store.tryClaim(eq("k1"), anyLong())).thenReturn(false);
        consumer.setIdempotentStore(store);

        consumer.onMessage(msg("dup"));

        assertEquals(0, consumer.handled);
    }

    @Test
    void idempotent_success_marksProcessed() {
        TestConsumer consumer = new TestConsumer();
        consumer.key = "k1";
        MqIdempotentStore store = mock(MqIdempotentStore.class);
        when(store.tryClaim(eq("k1"), anyLong())).thenReturn(true);
        consumer.setIdempotentStore(store);

        consumer.onMessage(msg("msg"));

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

        assertThrows(MqConsumerException.class, () -> consumer.onMessage(msg("msg")));

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

        consumer.onMessage(msg("msg"));

        verify(store).release("k1");
    }
}
