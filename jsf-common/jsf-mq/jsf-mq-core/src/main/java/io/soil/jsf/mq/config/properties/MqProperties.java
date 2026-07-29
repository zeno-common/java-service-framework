package io.soil.jsf.mq.config.properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * jsf-mq 模块配置属性，前缀 {@code jsf.mq}。
 * <p>
 * NameServer 默认读取 RocketMQ Spring Starter 的 {@code rocketmq.name-server} 配置，
 * 未配置时回退到 {@code 127.0.0.1:9876}。
 * </p>
 *
 * @author zeno.w
 */
@ConfigurationProperties(prefix = "jsf.mq")
public class MqProperties {

    @Value("${rocketmq.name-server:127.0.0.1:9876}")
    private String nameServer;

    private final Producer producer = new Producer();

    private final Consumer consumer = new Consumer();

    private final Outbox outbox = new Outbox();

    public String getNameServer() {
        return nameServer;
    }

    public void setNameServer(String nameServer) {
        this.nameServer = nameServer;
    }

    public Producer getProducer() {
        return producer;
    }

    public Consumer getConsumer() {
        return consumer;
    }

    public Outbox getOutbox() {
        return outbox;
    }

    /** 生产者相关配置 */
    public static class Producer {
        /** 默认生产者组 */
        private String group = "jsf-mq-producer";

        public String getGroup() {
            return group;
        }

        public void setGroup(String group) {
            this.group = group;
        }
    }

    /** 消费者相关配置 */
    public static class Consumer {
        /** 是否启用消费者（本地开发可关闭以避免连接 RocketMQ） */
        private boolean enabled = true;

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }
    }

    /** Outbox（可靠发送）相关配置，前缀 {@code jsf.mq.outbox} */
    public static class Outbox {
        /** 是否在事务提交后立即投递（false 则纯 relay 轮询模式） */
        private boolean immediateSend = true;
        /** 最大发送尝试次数，耗尽后置 FAILED（人工介入终态） */
        private int maxAttempts = 16;
        /** 认领锁时长（秒），SENDING 超过该时长视为僵尸行可被重新认领 */
        private long lockSeconds = 60;
        /** 首次退避（秒），同时作为 insert 时 nextRetryAt 的缓冲，避免 relay 与立即投递竞争 */
        private long initialBackoffSeconds = 10;
        /** 最大退避（秒） */
        private long maxBackoffSeconds = 3600;

        private final Relay relay = new Relay();

        public boolean isImmediateSend() {
            return immediateSend;
        }

        public void setImmediateSend(boolean immediateSend) {
            this.immediateSend = immediateSend;
        }

        public int getMaxAttempts() {
            return maxAttempts;
        }

        public void setMaxAttempts(int maxAttempts) {
            this.maxAttempts = maxAttempts;
        }

        public long getLockSeconds() {
            return lockSeconds;
        }

        public void setLockSeconds(long lockSeconds) {
            this.lockSeconds = lockSeconds;
        }

        public long getInitialBackoffSeconds() {
            return initialBackoffSeconds;
        }

        public void setInitialBackoffSeconds(long initialBackoffSeconds) {
            this.initialBackoffSeconds = initialBackoffSeconds;
        }

        public long getMaxBackoffSeconds() {
            return maxBackoffSeconds;
        }

        public void setMaxBackoffSeconds(long maxBackoffSeconds) {
            this.maxBackoffSeconds = maxBackoffSeconds;
        }

        public Relay getRelay() {
            return relay;
        }

        /** Outbox relay 兜底补发配置，前缀 {@code jsf.mq.outbox.relay} */
        public static class Relay {
            /** 是否启用 relay 定时兜底（需应用开启 @EnableScheduling） */
            private boolean enabled = true;
            /** 扫描间隔（毫秒） */
            private long interval = 5000;
            /** 单轮扫描批量条数 */
            private int batchSize = 100;

            public boolean isEnabled() {
                return enabled;
            }

            public void setEnabled(boolean enabled) {
                this.enabled = enabled;
            }

            public long getInterval() {
                return interval;
            }

            public void setInterval(long interval) {
                this.interval = interval;
            }

            public int getBatchSize() {
                return batchSize;
            }

            public void setBatchSize(int batchSize) {
                this.batchSize = batchSize;
            }
        }
    }
}
