package io.soil.jsf.mq.core;

import io.soil.jsf.mq.exception.MqProducerException;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.MessageConst;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

import java.util.function.Consumer;

/**
 * jsf-mq 统一消息生产者，封装 {@link RocketMQTemplate} 的常用发送能力。
 * <p>
 * 对外提供同步发送、异步发送（带回调）、单向发送以及延迟消息四类核心 API，
 * 所有发送异常统一包装为 {@link MqProducerException} 并保留原始异常链。
 * </p>
 * <p>
 * destination 支持 {@code topic} 及 {@code topic:tag} 两种格式。
 * </p>
 * <p>
 * 元信息（keys / 自定义属性）通过 {@link MqSendOptions} 承载，底层写入 RocketMQ 的 keys / 用户属性，
 * 不会把业务 payload 包成信封，保持链路字节为纯业务 JSON，兼容 Outbox 存储与既有消费者。
 * 不携带元信息的重载直接发送原始 payload（与旧版行为一致）；携带元信息的重载才会将 payload 包装为带 headers 的 Message。
 * </p>
 *
 * @author zeno.w
 */
public class MqProducer {

    private final RocketMQTemplate rocketMQTemplate;

    public MqProducer(RocketMQTemplate rocketMQTemplate) {
        this.rocketMQTemplate = rocketMQTemplate;
    }

    /**
     * 同步发送消息（不携带元信息）
     *
     * @param destination topic 或 topic:tag
     * @param payload     消息体（对象会自动序列化）
     * @return 发送结果
     */
    public SendResult send(String destination, Object payload) {
        try {
            return rocketMQTemplate.syncSend(destination, payload);
        } catch (Exception e) {
            throw MqProducerException.sendFailed(e, "同步消息发送失败 destination={0}", destination);
        }
    }

    /**
     * 同步发送消息（携带元信息）
     *
     * @param destination topic 或 topic:tag
     * @param payload     消息体
     * @param options     元信息选项（keys / 自定义属性）
     * @return 发送结果
     */
    public SendResult send(String destination, Object payload, MqSendOptions options) {
        try {
            return rocketMQTemplate.syncSend(destination, buildMessage(payload, options));
        } catch (Exception e) {
            throw MqProducerException.sendFailed(e, "同步消息发送失败 destination={0}", destination);
        }
    }

    /**
     * 异步发送消息（仅成功回调，异常将包装为 {@link MqProducerException} 抛出）
     *
     * @param destination topic 或 topic:tag
     * @param payload     消息体
     * @param onSuccess   成功回调
     */
    public void sendAsync(String destination, Object payload, Consumer<SendResult> onSuccess) {
        sendAsync(destination, payload, onSuccess, t -> {
            if (t instanceof RuntimeException re) {
                throw re;
            }
            throw new RuntimeException(t);
        });
    }

    /**
     * 异步发送消息（成功与失败回调分离）
     *
     * @param destination topic 或 topic:tag
     * @param payload     消息体
     * @param onSuccess   成功回调
     * @param onError     失败回调（入参为包装后的 {@link MqProducerException}）
     */
    public void sendAsync(String destination, Object payload,
                           Consumer<SendResult> onSuccess, Consumer<Throwable> onError) {
        rocketMQTemplate.asyncSend(destination, payload, new SendCallback() {
            @Override
            public void onSuccess(SendResult result) {
                onSuccess.accept(result);
            }

            @Override
            public void onException(Throwable throwable) {
                onError.accept(MqProducerException.sendFailed(throwable, "异步消息发送失败 destination={0}", destination));
            }
        });
    }

    /**
     * 异步发送消息（仅成功回调，携带元信息）
     *
     * @param destination topic 或 topic:tag
     * @param payload     消息体
     * @param options     元信息选项
     * @param onSuccess   成功回调
     */
    public void sendAsync(String destination, Object payload, MqSendOptions options, Consumer<SendResult> onSuccess) {
        sendAsync(destination, payload, options, onSuccess, t -> {
            if (t instanceof RuntimeException re) {
                throw re;
            }
            throw new RuntimeException(t);
        });
    }

    /**
     * 异步发送消息（成功与失败回调分离，携带元信息）
     *
     * @param destination topic 或 topic:tag
     * @param payload     消息体
     * @param options     元信息选项
     * @param onSuccess   成功回调
     * @param onError     失败回调（入参为包装后的 {@link MqProducerException}）
     */
    public void sendAsync(String destination, Object payload, MqSendOptions options,
                           Consumer<SendResult> onSuccess, Consumer<Throwable> onError) {
        rocketMQTemplate.asyncSend(destination, buildMessage(payload, options), new SendCallback() {
            @Override
            public void onSuccess(SendResult result) {
                onSuccess.accept(result);
            }

            @Override
            public void onException(Throwable throwable) {
                onError.accept(MqProducerException.sendFailed(throwable, "异步消息发送失败 destination={0}", destination));
            }
        });
    }

    /**
     * 单向发送消息（不关心结果，无返回、不保证可靠）
     *
     * @param destination topic 或 topic:tag
     * @param payload     消息体
     */
    public void sendOneway(String destination, Object payload) {
        try {
            rocketMQTemplate.sendOneWay(destination, payload);
        } catch (Exception e) {
            throw MqProducerException.sendFailed(e, "单向消息发送失败 destination={0}", destination);
        }
    }

    /**
     * 单向发送消息（携带元信息）
     *
     * @param destination topic 或 topic:tag
     * @param payload     消息体
     * @param options     元信息选项
     */
    public void sendOneway(String destination, Object payload, MqSendOptions options) {
        try {
            rocketMQTemplate.sendOneWay(destination, buildMessage(payload, options));
        } catch (Exception e) {
            throw MqProducerException.sendFailed(e, "单向消息发送失败 destination={0}", destination);
        }
    }

    /**
     * 发送延迟消息（同步）
     *
     * @param destination topic 或 topic:tag
     * @param payload     消息体
     * @param delayLevel  RocketMQ 延迟级别（1~18）
     * @return 发送结果
     */
    public SendResult sendDelay(String destination, Object payload, int delayLevel) {
        try {
            Message<?> message = MessageBuilder.withPayload(payload).build();
            return rocketMQTemplate.syncSend(destination, message, 3000L, delayLevel);
        } catch (Exception e) {
            throw MqProducerException.sendFailed(e, "延迟消息发送失败 destination={0}, delayLevel={1}", destination, delayLevel);
        }
    }

    /**
     * 发送延迟消息（同步，携带元信息）
     *
     * @param destination topic 或 topic:tag
     * @param payload     消息体
     * @param options     元信息选项
     * @param delayLevel  RocketMQ 延迟级别（1~18）
     * @return 发送结果
     */
    public SendResult sendDelay(String destination, Object payload, MqSendOptions options, int delayLevel) {
        try {
            Message<?> message = buildMessage(payload, options);
            return rocketMQTemplate.syncSend(destination, message, 3000L, delayLevel);
        } catch (Exception e) {
            throw MqProducerException.sendFailed(e, "延迟消息发送失败 destination={0}, delayLevel={1}", destination, delayLevel);
        }
    }

    /**
     * 将 payload 与元信息选项组装为 Spring {@link Message}：keys 写入 RocketMQ KEYS 属性，
     * 自定义属性逐个写入用户属性（user properties）。
     */
    private Message<?> buildMessage(Object payload, MqSendOptions options) {
        MessageBuilder<?> builder = MessageBuilder.withPayload(payload);
        if (options != null) {
            if (options.getKeys() != null) {
                builder.setHeader(MessageConst.PROPERTY_KEYS, options.getKeys());
            }
            if (options.getProperties() != null) {
                options.getProperties().forEach(builder::setHeader);
            }
        }
        return builder.build();
    }
}
