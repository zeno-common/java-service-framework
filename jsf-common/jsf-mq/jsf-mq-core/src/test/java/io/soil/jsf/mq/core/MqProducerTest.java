package io.soil.jsf.mq.core;

import io.soil.jsf.mq.exception.MqProducerException;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.Message;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MqProducerTest {

    @Mock
    private RocketMQTemplate rocketMQTemplate;

    private MqProducer mqProducer;

    @BeforeEach
    void setUp() {
        mqProducer = new MqProducer(rocketMQTemplate);
    }

    @Test
    void send_delegatesToTemplate() {
        SendResult result = new SendResult();
        when(rocketMQTemplate.syncSend(eq("topic:tag"), Mockito.<Object>any())).thenReturn(result);

        SendResult r = mqProducer.send("topic:tag", "payload");

        assertSame(result, r);
        verify(rocketMQTemplate).syncSend("topic:tag", "payload");
    }

    @Test
    void send_wrapsExceptionAsMqProducerException() {
        when(rocketMQTemplate.syncSend(anyString(), Mockito.<Object>any())).thenThrow(new RuntimeException("boom"));

        MqProducerException ex = assertThrows(MqProducerException.class, () -> mqProducer.send("t", "p"));
        assertEquals("MQ-SEND-FAILED", ex.code());
    }

    @Test
    void sendAsync_invokesSuccessCallback() {
        ArgumentCaptor<SendCallback> captor = ArgumentCaptor.forClass(SendCallback.class);
        doNothing().when(rocketMQTemplate).asyncSend(eq("t"), eq("p"), captor.capture());

        SendResult[] holder = new SendResult[1];
        mqProducer.sendAsync("t", "p", r -> holder[0] = r);

        SendResult sr = new SendResult();
        captor.getValue().onSuccess(sr);
        assertSame(sr, holder[0]);
    }

    @Test
    void sendAsync_invokesErrorCallbackWithMqProducerException() {
        ArgumentCaptor<SendCallback> captor = ArgumentCaptor.forClass(SendCallback.class);
        doNothing().when(rocketMQTemplate).asyncSend(eq("t"), eq("p"), captor.capture());

        Throwable[] holder = new Throwable[1];
        mqProducer.sendAsync("t", "p", r -> {}, e -> holder[0] = e);

        captor.getValue().onException(new RuntimeException("boom"));

        assertInstanceOf(MqProducerException.class, holder[0]);
        assertEquals("MQ-SEND-FAILED", ((MqProducerException) holder[0]).code());
    }

    @Test
    void sendOneway_delegatesToTemplate() {
        doNothing().when(rocketMQTemplate).sendOneWay(anyString(), Mockito.<Object>any());

        mqProducer.sendOneway("t", "p");

        verify(rocketMQTemplate).sendOneWay("t", "p");
    }

    @Test
    void sendDelay_delegatesWithDelayLevel() {
        SendResult result = new SendResult();
        when(rocketMQTemplate.syncSend(eq("t"), any(Message.class), anyLong(), eq(3))).thenReturn(result);

        SendResult r = mqProducer.sendDelay("t", "p", 3);

        assertSame(result, r);
        verify(rocketMQTemplate).syncSend(eq("t"), any(Message.class), eq(3000L), eq(3));
    }

    @Test
    void sendDelay_wrapsExceptionAsMqProducerException() {
        when(rocketMQTemplate.syncSend(anyString(), any(Message.class), anyLong(), anyInt()))
                .thenThrow(new RuntimeException("boom"));

        MqProducerException ex = assertThrows(MqProducerException.class, () -> mqProducer.sendDelay("t", "p", 2));
        assertEquals("MQ-SEND-FAILED", ex.code());
    }
}
