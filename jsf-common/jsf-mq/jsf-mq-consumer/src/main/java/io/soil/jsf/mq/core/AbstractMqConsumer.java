package io.soil.jsf.mq.core;

import io.soil.jsf.mq.core.failure.MqConsumeFailureHandler;
import io.soil.jsf.mq.core.failure.MqConsumeFailureRecord;
import io.soil.jsf.mq.core.idempotent.MqIdempotentStore;
import io.soil.jsf.mq.exception.MqConsumerException;
import io.soil.jsf.util.json.JsonMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.OffsetDateTime;

/**
 * jsf-mq 抽象消息消费者基类。
 * <p>
 * 子类通过 {@code @RocketMQMessageListener} 注解声明消费的 topic / group，并实现
 * {@link #handleMessage(Object)} 返回 {@link ConsumeStatus} 三态结果：
 * <ul>
 *   <li>{@link ConsumeStatus#SUCCESS} — 正常确认（ack）；</li>
 *   <li>{@link ConsumeStatus#RETRY_LATER} — 抛出 {@link MqConsumerException} 触发 broker 重试
 *       （重试耗尽后自动进入 {@code %DLQ%+group} 死信队列）；处理逻辑抛出的异常默认按此处理；</li>
 *   <li>{@link ConsumeStatus#DISCARD} — 不重试，交由 {@link MqConsumeFailureHandler} 落库，供后续重放。</li>
 * </ul>
 * </p>
 * <p>
 * <b>幂等（可选）</b>：覆写 {@link #idempotentKey(Object)} 返回非空幂等键，且容器中存在
 * {@link MqIdempotentStore} 实现（如引入 jsf-mq-mongodb）时自动启用：
 * 消费前 tryClaim 去重，成功 markProcessed，RETRY_LATER / DISCARD 时 release
 * （DISCARD 释放声明是为了不阻塞后续人工重放）。
 * RocketMQ 为 at-least-once 语义，<b>强烈建议</b>所有业务消费者启用幂等。
 * </p>
 *
 * @param <T> 消息体类型
 * @author zeno.w
 */
@Slf4j
public abstract class AbstractMqConsumer<T> implements RocketMQListener<T> {

    private MqIdempotentStore idempotentStore;

    private MqConsumeFailureHandler failureHandler;

    @Autowired(required = false)
    public void setIdempotentStore(MqIdempotentStore idempotentStore) {
        this.idempotentStore = idempotentStore;
    }

    @Autowired(required = false)
    public void setFailureHandler(MqConsumeFailureHandler failureHandler) {
        this.failureHandler = failureHandler;
    }

    @Override
    public void onMessage(T message) {
        String key = idempotentKey(message);
        boolean claimed = false;
        if (key != null && idempotentStore != null) {
            if (!idempotentStore.tryClaim(key, idempotentTtlSeconds())) {
                log.info("重复消息已跳过 consumer={}, idempotentKey={}", getClass().getSimpleName(), key);
                return;
            }
            claimed = true;
        }

        ConsumeStatus status;
        Exception error = null;
        try {
            status = handleMessage(message);
            if (status == null) {
                status = ConsumeStatus.SUCCESS;
            }
        } catch (Exception e) {
            error = e;
            status = ConsumeStatus.RETRY_LATER;
        }

        switch (status) {
            case SUCCESS -> {
                if (claimed) {
                    idempotentStore.markProcessed(key);
                }
            }
            case RETRY_LATER -> {
                if (claimed) {
                    idempotentStore.release(key);
                }
                throw MqConsumerException.consumeFailed(error, "消息消费失败，触发 broker 重试 consumer={0}", getClass().getName());
            }
            case DISCARD -> {
                if (claimed) {
                    idempotentStore.release(key);
                }
                saveFailure(message, key, error);
            }
        }
    }

    /**
     * 处理消息业务逻辑，由子类实现。
     *
     * @param message 反序列化后的消息体
     * @return 消费结果三态（返回 null 视为 SUCCESS）
     * @throws Exception 业务处理异常（默认按 {@link ConsumeStatus#RETRY_LATER} 处理）
     */
    protected abstract ConsumeStatus handleMessage(T message) throws Exception;

    /**
     * 幂等键（如 messageId / bizKey）。默认返回 {@code null} 表示不启用幂等去重。
     *
     * @param message 消息体
     * @return 幂等键，null 则不启用幂等
     */
    protected String idempotentKey(T message) {
        return null;
    }

    /**
     * 幂等声明有效期（秒），默认 300 秒。
     * 超过该时间未标记完成的声明视为失效（防消费者崩溃后声明永久占用）。
     */
    protected long idempotentTtlSeconds() {
        return 300L;
    }

    private void saveFailure(T message, String bizKey, Exception error) {
        if (failureHandler == null) {
            log.error("消息已判定 DISCARD 但未配置 MqConsumeFailureHandler，消息将丢失！consumer={}, payload={}",
                    getClass().getName(), serializePayload(message), error);
            return;
        }
        MqConsumeFailureRecord record = new MqConsumeFailureRecord();
        RocketMQMessageListener listener = getClass().getAnnotation(RocketMQMessageListener.class);
        if (listener != null) {
            record.setTopic(listener.topic());
            record.setTag(listener.selectorExpression());
            record.setConsumerGroup(listener.consumerGroup());
        }
        record.setConsumerClass(getClass().getName());
        record.setBizKey(bizKey);
        record.setPayload(serializePayload(message));
        record.setFailedAt(OffsetDateTime.now());
        if (error != null) {
            record.setErrorMsg(error.getMessage());
            StringWriter sw = new StringWriter();
            error.printStackTrace(new PrintWriter(sw));
            record.setStackTrace(sw.toString());
        } else {
            record.setErrorMsg("consumer 主动 DISCARD（无异常）");
        }
        failureHandler.save(record);
    }

    private String serializePayload(T message) {
        if (message instanceof String s) {
            return s;
        }

      return JsonMapper.toString(message);
    }
}
