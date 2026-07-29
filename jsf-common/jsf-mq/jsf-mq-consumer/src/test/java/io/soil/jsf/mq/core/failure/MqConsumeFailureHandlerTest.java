package io.soil.jsf.mq.core.failure;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class MqConsumeFailureHandlerTest {

    @Test
    void save_delegatesToStore() {
        MqConsumeFailureStore store = mock(MqConsumeFailureStore.class);
        MqConsumeFailureHandler handler = new MqConsumeFailureHandler(store);
        MqConsumeFailureRecord record = new MqConsumeFailureRecord();
        record.setTopic("t");

        handler.save(record);

        verify(store).save(record);
    }

    @Test
    void save_storeThrows_isSwallowed() {
        MqConsumeFailureStore store = mock(MqConsumeFailureStore.class);
        doThrow(new RuntimeException("db down")).when(store).save(any());
        MqConsumeFailureHandler handler = new MqConsumeFailureHandler(store);

        assertDoesNotThrow(() -> handler.save(new MqConsumeFailureRecord()));
    }

    @Test
    void replay_marksReplayedOnSuccess() {
        MqConsumeFailureStore store = mock(MqConsumeFailureStore.class);
        MqConsumeFailureRecord r1 = new MqConsumeFailureRecord();
        r1.setId(1L);
        MqConsumeFailureRecord r2 = new MqConsumeFailureRecord();
        r2.setId(2L);
        when(store.fetchPending(anyInt())).thenReturn(List.of(r1, r2));
        MqConsumeFailureHandler handler = new MqConsumeFailureHandler(store);

        AtomicInteger replayed = new AtomicInteger();
        int success = handler.replay(10, r -> replayed.incrementAndGet());

        assertEquals(2, success);
        assertEquals(2, replayed.get());
        verify(store).markReplayed(1L);
        verify(store).markReplayed(2L);
    }

    @Test
    void replay_singleFailure_keepsPendingAndContinues() {
        MqConsumeFailureStore store = mock(MqConsumeFailureStore.class);
        MqConsumeFailureRecord bad = new MqConsumeFailureRecord();
        bad.setId(1L);
        MqConsumeFailureRecord ok = new MqConsumeFailureRecord();
        ok.setId(2L);
        when(store.fetchPending(anyInt())).thenReturn(List.of(bad, ok));
        MqConsumeFailureHandler handler = new MqConsumeFailureHandler(store);

        int success = handler.replay(10, r -> {
            if (Long.valueOf(1L).equals(r.getId())) {
                throw new RuntimeException("replay fail");
            }
        });

        assertEquals(1, success);
        verify(store, never()).markReplayed(1L);
        verify(store).markReplayed(2L);
    }
}
