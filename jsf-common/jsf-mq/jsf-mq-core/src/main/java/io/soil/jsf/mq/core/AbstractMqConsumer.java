package io.soil.jsf.mq.core;

import io.soil.jsf.mq.core.failure.MqConsumeFailureHandler;
import io.soil.jsf.mq.core.failure.MqConsumeFailureRecord;
import io.soil.jsf.mq.core.failure.MqConsumeFailureReason;
import io.soil.jsf.mq.core.idempotent.MqIdempotentStore;
import io.soil.jsf.mq.exception.MqConsumerException;
import io.soil.jsf.util.json.JsonMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ResolvableType;

import java.io.ByteArrayInputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.time.OffsetDateTime;

/**
 * jsf-mq 抽象消息消费者基类。
 * <p>
 * 子类通过 {@code @RocketMQMessageListener} 注解声明消费的 topic / group，并实现
 * {@link #handleMessage(Object, MqConsumeContext)} 返回 {@link ConsumeStatus} 三态结果：
 * <ul>
 *   <li>{@link ConsumeStatus#SUCCESS} — 正常确认（ack）；</li>
 *   <li>{@link ConsumeStatus#RETRY_LATER} — 抛出 {@link MqConsumerException} 触发 broker 重试
 *       （重试耗尽后自动进入 {@code %DLQ%+group} 死信队列）；处理逻辑抛出的异常默认按此处理；</li>
 *   <li>{@link ConsumeStatus#DISCARD} — 不重试，交由 {@link MqConsumeFailureHandler} 落库，供后续重放。</li>
 * </ul>
 * </p>
 * <p>
 * 基类直接消费 {@link MessageExt}，自动将 body 反序列化为 {@code T} 并提取元信息封装为
 * {@link MqConsumeContext}（msgId / topic / tag / keys / 自定义属性）。如需在业务中使用元信息
 * （如用 msgId/keys 做幂等、读取 traceId 做链路追踪），在 {@code handleMessage(T, MqConsumeContext)}
 * 中通过 {@code ctx} 读取，或覆写 {@code idempotentKey(T, MqConsumeContext)} 自定义幂等键。
 * </p>
 * <p>
 * <b>幂等（可选）</b>：覆写 {@link #idempotentKey(Object, MqConsumeContext)} 返回非空幂等键，且容器中存在
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
public abstract class AbstractMqConsumer<T> implements RocketMQListener<MessageExt> {

    /** RocketMQ 默认最大重试次数（注解未显式配置 maxReconsumeTimes 时回退此值）。 */
    private static final int DEFAULT_MAX_RECONSUME_TIMES = 16;

    private final Class<?> payloadType;

    /** 构建时从 {@link RocketMQMessageListener} 读取的消费者组，落库失败记录时使用。 */
    private final String consumerGroup;

    /** 构建时从 {@link RocketMQMessageListener} 读取的最大重试次数（未配置则回退默认 16）。 */
    private final int maxReconsumeTimes;

    private MqIdempotentStore idempotentStore;

    private MqConsumeFailureHandler failureHandler;

    protected AbstractMqConsumer() {
        this.payloadType = resolvePayloadType();
        RocketMQMessageListener listener = getClass().getAnnotation(RocketMQMessageListener.class);
        if (listener != null) {
            this.consumerGroup = listener.consumerGroup();
            int configured = listener.maxReconsumeTimes();
            this.maxReconsumeTimes = configured > 0 ? configured : DEFAULT_MAX_RECONSUME_TIMES;
        } else {
            this.consumerGroup = null;
            this.maxReconsumeTimes = DEFAULT_MAX_RECONSUME_TIMES;
        }
    }

    @Autowired(required = false)
    public void setIdempotentStore(MqIdempotentStore idempotentStore) {
        this.idempotentStore = idempotentStore;
    }

    @Autowired(required = false)
    public void setFailureHandler(MqConsumeFailureHandler failureHandler) {
        this.failureHandler = failureHandler;
    }

    @Override
    public void onMessage(MessageExt messageExt) {
        T payload = toPayload(messageExt);
        MqConsumeContext ctx = new MqConsumeContext(messageExt);

        String key = idempotentKey(payload, ctx);
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
            status = handleMessage(payload, ctx);
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
                // broker 重试耗尽（最后一次投递）时落库，避免每次重试都重复记录；之后仍抛异常进死信队列
                if (isFinalAttempt(messageExt)) {
                    saveFailure(payload, key, error, ctx, MqConsumeFailureReason.RETRY_EXHAUSTED);
                }
                throw MqConsumerException.consumeFailed(error, "消息消费失败，触发 broker 重试 consumer={0}", getClass().getName());
            }
            case DISCARD -> {
                if (claimed) {
                    idempotentStore.release(key);
                }
                saveFailure(payload, key, error, ctx, MqConsumeFailureReason.DISCARDED);
            }
        }
    }

    /**
     * 处理消息业务逻辑（唯一入口），由子类实现。基类 {@link #onMessage(MessageExt)} 始终以本方法派发，
     * 并携带 {@link MqConsumeContext} 元信息（msgId / keys / topic / tag / 自定义属性）。
     * <p>
     * 不需要元信息时可直接忽略 {@code ctx} 参数；需要 msgId / keys / 链路追踪等元信息时通过 {@code ctx} 读取。
     *
     * @param message 反序列化后的消息体
     * @param ctx     消息元信息上下文
     * @return 消费结果三态（返回 null 视为 SUCCESS）
     * @throws Exception 业务处理异常（默认按 {@link ConsumeStatus#RETRY_LATER} 处理）
     */
    protected abstract ConsumeStatus handleMessage(T message, MqConsumeContext ctx) throws Exception;

    /**
     * 幂等键（如 messageId / bizKey）。默认返回 {@code null} 表示不启用幂等去重。
     *
     * @param message 消息体
     * @return 幂等键，null 则不启用幂等
     */
    /**
     * 幂等键（唯一入口），默认返回 {@code null} 表示不启用幂等去重。基类 {@link #onMessage(MessageExt)} 始终以本方法读取幂等键，
     * 并携带 {@link MqConsumeContext} 元信息（msgId / keys / topic / tag / 自定义属性）。
     * <p>
     * 需要基于消息元信息（如 {@code ctx.getKeys()} / {@code ctx.getMessageId()}）构造幂等键时，在 {@code ctx} 中读取；
     * 不需要元信息时可直接用 {@code message} 构造，忽略 {@code ctx} 参数。返回非空值且容器中存在
     * {@link MqIdempotentStore} 实现（如引入 jsf-mq-mongodb）时自动启用幂等去重。
     *
     * @param message 消息体
     * @param ctx     消息元信息上下文
     * @return 幂等键，null 则不启用幂等
     */
    protected String idempotentKey(T message, MqConsumeContext ctx) {
        return null;
    }

    /**
     * 幂等声明有效期（秒），默认 300 秒。
     * 超过该时间未标记完成的声明视为失效（防消费者崩溃后声明永久占用）。
     */
    protected long idempotentTtlSeconds() {
        return 300L;
    }

    /**
     * 将 {@link MessageExt} 的 body 反序列化为 {@code T}。
     * String / byte[] 走原生转换，其余类型走项目统一 JSON 反序列化（与 Outbox / 序列化契约一致）。
     */
    @SuppressWarnings("unchecked")
    private T toPayload(MessageExt messageExt) {
        byte[] body = messageExt.getBody();
        if (payloadType == String.class) {
            return (T) new String(body, StandardCharsets.UTF_8);
        }
        if (payloadType == byte[].class) {
            return (T) body;
        }

        ByteArrayInputStream bais = new ByteArrayInputStream(body);
        T parsed = JsonMapper.toObject(bais, (Class<T>) payloadType);
        if (parsed == null) {
            throw MqConsumerException.consumeFailed(null,
                    "消息体反序列化失败 consumer={0}, topic={1}, msgId={2}",
                    getClass().getName(), messageExt.getTopic(), messageExt.getMsgId());
        }
        return parsed;
    }

    /**
     * 解析子类声明的 {@code T} 实际类型，用于反序列化。
     * 形如 {@code class X extends AbstractMqConsumer<Foo>} 可正确解析为 Foo；
     * 若中间存在未绑定泛型或裸类型，则退化为 Object。
     */
    private Class<?> resolvePayloadType() {
        Class<?> resolved = ResolvableType.forClass(AbstractMqConsumer.class, getClass()).getGeneric(0).resolve();
        return resolved != null ? resolved : Object.class;
    }

    private void saveFailure(T message, String bizKey, Exception error, MqConsumeContext ctx, MqConsumeFailureReason reason) {
        if (failureHandler == null) {
            log.error("消息判定为失败（{}）但未配置 MqConsumeFailureHandler，消息将丢失！consumer={}, payload={}",
                    reason, getClass().getName(), serializePayload(message), error);
            return;
        }
        MqConsumeFailureRecord record = new MqConsumeFailureRecord();
        record.setTopic(ctx.getTopic());
        record.setTag(ctx.getTag());
        record.setConsumerClass(getClass().getName());
        record.setMessageId(ctx.getMessageId());
        record.setKeys(ctx.getKeys());
        record.setBizKey(bizKey);
        record.setReason(reason);
        record.setPayload(serializePayload(message));
        record.setFailedAt(OffsetDateTime.now());
        record.setConsumerGroup(consumerGroup);

        if (error != null) {
            record.setErrorMsg(error.getMessage());
            StringWriter sw = new StringWriter();
            error.printStackTrace(new PrintWriter(sw));
            record.setStackTrace(sw.toString());
        } else {
            record.setErrorMsg(switch (reason) {
                case DISCARDED -> "consumer 主动 DISCARD（无异常）";
                case RETRY_EXHAUSTED -> "broker 重试耗尽，即将进入死信队列（无异常）";
            });
        }
        failureHandler.save(record);
    }

    /**
     * 是否已是 broker 的最后一次投递（再返回 RETRY_LATER 将进入死信队列）。
     * 用于失败时仅在末次投递落库，避免每次重试都重复写失败记录。
     */
    private boolean isFinalAttempt(MessageExt messageExt) {
        return messageExt.getReconsumeTimes() >= maxReconsumeTimes - 1;
    }

    private String serializePayload(T message) {
        if (message instanceof String s) {
            return s;
        }

      return JsonMapper.toString(message);
    }
}
