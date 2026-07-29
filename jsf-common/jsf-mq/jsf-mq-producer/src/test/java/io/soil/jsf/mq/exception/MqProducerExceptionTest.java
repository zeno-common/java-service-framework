package io.soil.jsf.mq.exception;

import io.soil.jsf.common.exception.ExceptionType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MqProducerExceptionTest {

    @Test
    void sendFailed_containsCodeAndTypeAndMessage() {
        MqProducerException ex = MqProducerException.sendFailed(new RuntimeException("boom"),
                "同步消息发送失败 destination={0}", "order-topic");
        assertEquals("MQ-SEND-FAILED", ex.code());
        assertEquals(ExceptionType.SYS, ex.type());
        assertTrue(ex.getMessage().contains("order-topic"));
        assertNotNull(ex.getCause());
        assertEquals("boom", ex.getCause().getMessage());
    }

    @Test
    void producerNotAvailable_hasCorrectCodeAndType() {
        MqProducerException ex = MqProducerException.producerNotAvailable("producer 未初始化");
        assertEquals("MQ-PRODUCER-NOT-AVAILABLE", ex.code());
        assertEquals(ExceptionType.SYS, ex.type());
        assertTrue(ex.getMessage().contains("producer 未初始化"));
    }
}
