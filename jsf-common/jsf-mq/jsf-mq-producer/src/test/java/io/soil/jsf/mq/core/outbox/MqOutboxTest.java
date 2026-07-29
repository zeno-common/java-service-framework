package io.soil.jsf.mq.core.outbox;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.soil.jsf.mq.config.properties.MqProperties;
import io.soil.jsf.mq.core.MqProducer;
import io.soil.jsf.mq.exception.MqException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class MqOutboxTest {

    private MqOutboxStore store;
    private MqProducer producer;
    private MqProperties.Outbox props;
    private MqOutbox outbox;

    @BeforeEach
    void setUp() {
        store = mock(MqOutboxStore.class);
        producer = mock(MqProducer.class);
        props = new MqProperties.Outbox();
        outbox = new MqOutbox(store, producer, new ObjectMapper(), props);
        // insert 回填 id（模拟存储实现行为）
        doAnswer(inv -> {
            ((MqOutboxMessage) inv.getArgument(0)).setId(100L);
            return null;
        }).when(store).insert(any(MqOutboxMessage.class));
    }

    @Test
    void save_withoutTx_insertsAndDispatchesImmediately() {
        when(store.claim(eq(100L), anyLong())).thenReturn(true);

        MqOutboxMessage msg = outbox.save("order-topic", "created", Map.of("orderId", 1));

        assertNotNull(msg.getId());
        assertEquals(MqOutboxStatus.PENDING, msg.getStatus());
        verify(store).insert(any(MqOutboxMessage.class));
        verify(producer).send(eq("order-topic:created"), anyString());
        verify(store).markSent(100L);
    }

    @Test
    void save_immediateSendDisabled_onlyInserts() {
        props.setImmediateSend(false);

        outbox.save("order-topic", null, Map.of("orderId", 1));

        verify(store).insert(any(MqOutboxMessage.class));
        verify(store, never()).claim(anyLong(), anyLong());
        verify(producer, never()).send(anyString(), any());
    }

    @Test
    void dispatch_claimFails_skipsSend() {
        when(store.claim(eq(100L), anyLong())).thenReturn(false);
        MqOutboxMessage msg = message();

        assertFalse(outbox.dispatch(msg));

        verify(producer, never()).send(anyString(), any());
    }

    @Test
    void dispatch_sendFails_marksFailedWithBackoff() {
        when(store.claim(eq(100L), anyLong())).thenReturn(true);
        when(producer.send(anyString(), any())).thenThrow(MqException.sendFailed(new RuntimeException("down"), "x"));
        MqOutboxMessage msg = message();

        assertFalse(outbox.dispatch(msg));

        verify(store).markFailed(eq(100L), eq(1), anyLong());
        verify(store, never()).markSent(anyLong());
    }

    @Test
    void dispatch_attemptsExhausted_marksDead() {
        when(store.claim(eq(100L), anyLong())).thenReturn(true);
        when(producer.send(anyString(), any())).thenThrow(MqException.sendFailed(new RuntimeException("down"), "x"));
        MqOutboxMessage msg = message();
        msg.setAttempt(props.getMaxAttempts() - 1);

        assertFalse(outbox.dispatch(msg));

        verify(store).markDead(100L, props.getMaxAttempts());
        verify(store, never()).markFailed(anyLong(), anyInt(), anyLong());
    }

    @Test
    void backoff_isExponentialAndCapped() {
        assertEquals(props.getInitialBackoffSeconds() * 1000L, outbox.backoffMillis(1));
        assertEquals(props.getInitialBackoffSeconds() * 2000L, outbox.backoffMillis(2));
        assertTrue(outbox.backoffMillis(30) <= props.getMaxBackoffSeconds() * 1000L);
    }

    private MqOutboxMessage message() {
        MqOutboxMessage msg = new MqOutboxMessage();
        msg.setId(100L);
        msg.setTopic("order-topic");
        msg.setPayload("{}");
        msg.setStatus(MqOutboxStatus.PENDING);
        return msg;
    }
}
