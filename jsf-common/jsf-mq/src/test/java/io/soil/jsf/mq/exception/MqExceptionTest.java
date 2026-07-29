package io.soil.jsf.mq.exception;

import io.soil.jsf.common.exception.ExceptionType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MqExceptionTest {

    @Test
    void sendFailed_containsCodeAndTypeAndMessage() {
        MqException ex = MqException.sendFailed(new RuntimeException("boom"),
                "同步消息发送失败 destination={0}", "order-topic");
        assertEquals("MQ-SEND-FAILED", ex.code());
        assertEquals(ExceptionType.SYS, ex.type());
        assertTrue(ex.getMessage().contains("order-topic"));
        assertNotNull(ex.getCause());
        assertEquals("boom", ex.getCause().getMessage());
    }

    @Test
    void consumeFailed_hasCorrectCodeAndType() {
        MqException ex = MqException.consumeFailed(null, "消费失败");
        assertEquals("MQ-CONSUME-FAILED", ex.code());
        assertEquals(ExceptionType.SYS, ex.type());
    }

    @Test
    void producerNotAvailable_hasCorrectCodeAndType() {
        MqException ex = MqException.producerNotAvailable("producer 未初始化");
        assertEquals("MQ-PRODUCER-NOT-AVAILABLE", ex.code());
        assertEquals(ExceptionType.SYS, ex.type());
        assertTrue(ex.getMessage().contains("producer 未初始化"));
    }
}
