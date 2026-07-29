package io.soil.jsf.mq.exception;

import io.soil.jsf.common.exception.ExceptionType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MqConsumerExceptionTest {

    @Test
    void consumeFailed_containsCodeAndTypeAndMessage() {
        MqConsumerException ex = MqConsumerException.consumeFailed(new RuntimeException("boom"),
                "消息消费失败 consumer={0}", "OrderConsumer");
        assertEquals("MQ-CONSUME-FAILED", ex.code());
        assertEquals(ExceptionType.SYS, ex.type());
        assertTrue(ex.getMessage().contains("OrderConsumer"));
        assertNotNull(ex.getCause());
        assertEquals("boom", ex.getCause().getMessage());
    }

    @Test
    void consumeFailed_withoutCause() {
        MqConsumerException ex = MqConsumerException.consumeFailed(null, "消费失败");
        assertEquals("MQ-CONSUME-FAILED", ex.code());
        assertEquals(ExceptionType.SYS, ex.type());
    }
}
