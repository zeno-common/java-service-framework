package io.soil.jsf.mq.core.outbox;

import io.soil.jsf.mq.config.properties.MqProducerProperties;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class MqOutboxRelayTest {

    @Test
    void relay_dispatchesEachPendingMessage() {
        MqOutboxStore store = mock(MqOutboxStore.class);
        MqOutbox outbox = mock(MqOutbox.class);
        MqProducerProperties.Outbox props = new MqProducerProperties.Outbox();
        MqOutboxRelay relay = new MqOutboxRelay(store, outbox, props);

        MqOutboxMessage m1 = new MqOutboxMessage();
        m1.setId(1L);
        m1.setTopic("t");
        MqOutboxMessage m2 = new MqOutboxMessage();
        m2.setId(2L);
        m2.setTopic("t");
        when(store.fetchPending(anyInt(), anyLong())).thenReturn(List.of(m1, m2));
        when(outbox.dispatch(m1)).thenReturn(true);
        when(outbox.dispatch(m2)).thenReturn(false);

        assertEquals(1, relay.relay());

        verify(outbox).dispatch(m1);
        verify(outbox).dispatch(m2);
    }

    @Test
    void relay_emptyBatch_doesNothing() {
        MqOutboxStore store = mock(MqOutboxStore.class);
        MqOutbox outbox = mock(MqOutbox.class);
        MqOutboxRelay relay = new MqOutboxRelay(store, outbox, new MqProducerProperties.Outbox());

        when(store.fetchPending(anyInt(), anyLong())).thenReturn(List.of());

        assertEquals(0, relay.relay());
        verify(outbox, never()).dispatch(any());
    }

    @Test
    void relay_singleDispatchException_doesNotAbortBatch() {
        MqOutboxStore store = mock(MqOutboxStore.class);
        MqOutbox outbox = mock(MqOutbox.class);
        MqOutboxRelay relay = new MqOutboxRelay(store, outbox, new MqProducerProperties.Outbox());

        MqOutboxMessage bad = new MqOutboxMessage();
        bad.setId(1L);
        bad.setTopic("t");
        MqOutboxMessage ok = new MqOutboxMessage();
        ok.setId(2L);
        ok.setTopic("t");
        when(store.fetchPending(anyInt(), anyLong())).thenReturn(List.of(bad, ok));
        when(outbox.dispatch(bad)).thenThrow(new RuntimeException("boom"));
        when(outbox.dispatch(ok)).thenReturn(true);

        assertEquals(1, relay.relay());
    }
}
