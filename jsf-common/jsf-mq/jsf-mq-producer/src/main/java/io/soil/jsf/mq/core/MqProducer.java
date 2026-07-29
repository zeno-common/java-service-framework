package io.soil.jsf.mq.core;

import io.soil.jsf.mq.exception.MqProducerException;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
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
 *
 * @author zeno.w
 */
public class MqProducer {

    private final RocketMQTemplate rocketMQTemplate;

    public MqProducer(RocketMQTemplate rocketMQTemplate) {
        this.rocketMQTemplate = rocketMQTemplate;
    }

    /**
     * 同步发送消息
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
     * 异步发送消息（仅成功回调，异常将包装为 {@link MqProducerException} 抛出）
     *
     * @param destination topic 或 topic:tag
     * @param payload     消息体
     * @param onSuccess  成功回调
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
}
