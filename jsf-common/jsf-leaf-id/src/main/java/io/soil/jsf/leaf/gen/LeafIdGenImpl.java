package io.soil.jsf.leaf.gen;

import io.soil.jsf.leaf.IDGen;
import io.soil.jsf.leaf.exception.LeafIdException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * 基于 Twitter Snowflake 算法改进的分布式唯一 ID 生成器
 * <p>
 * 生成的 64 位 ID 结构如下（高位 → 低位）：
 * <pre>
 * | 1 bit  |       42 bit        |   5 bit    |   8 bit    |   8 bit   |
 * | 符号位  |  时间戳（相对 EPOCH） |  数据中心ID  |  工作节点ID  |  毫秒内序号  |
 * |   0    |  约 139 年可用        |  0 ~ 31    |  0 ~ 255   |  0 ~ 255  |
 * </pre>
 * </p>
 * <p>
 * 关键设计：
 * <ul>
 *   <li>时间戳基于自定义纪元（EPOCH = 2026-01-01 UTC），可用约 139 年</li>
 *   <li>数据中心 ID 占 5 bit，最大支持 32 个数据中心</li>
 *   <li>工作节点 ID 占 8 bit，每个数据中心最大支持 256 个节点</li>
 *   <li>毫秒内序号占 8 bit，每毫秒可生成 256 个 ID</li>
 *   <li>时钟回拨容忍 5ms 以内，通过等待补偿；超过 5ms 直接抛异常</li>
 * </ul>
 * </p>
 *
 * @see IDGen
 * @see LeafId
 */
public class LeafIdGenImpl implements IDGen {

    private static final Logger LOGGER = LoggerFactory.getLogger(LeafIdGenImpl.class);

    /**
     * 自定义纪元起始时间：2026-01-01 00:00:00 UTC
     * <p>
     * 时间戳存储的是相对此纪元的毫秒偏移量，42 bit 可表示约 139 年。
     * </p>
     */
    private static final long EPOCH = LocalDateTime.of(2026, 1, 1, 0, 0, 0)
      .toInstant(ZoneOffset.UTC).toEpochMilli();

    /** 数据中心 ID 占用 bit 数，5 bit 支持最大 32 个数据中心 */
    private static final long DATA_CENTER_BITS = 5L;

    /** 数据中心 ID 最大值：31 */
    private static final long MAXIMUM_DATA_CENTER = ~(-1L << DATA_CENTER_BITS);

    /** 工作节点 ID 占用 bit 数，8 bit 支持最大 256 个节点 */
    private static final long WORKER_ID_BITES = 8L;

    /** 工作节点 ID 最大值：255 */
    private static final long MAXIMUM_WORKER = ~(-1L << WORKER_ID_BITES);

    /** 毫秒内序号占用 bit 数，8 bit 支持每毫秒 256 个 ID */
    private static final long SEQUENCE_BITS = 8L;

    /** 工作节点 ID 在最终 ID 中的左移位数 */
    private static final long WORKER_ID_SHIFT = SEQUENCE_BITS;

    /** 数据中心 ID 在最终 ID 中的左移位数 = 序号位数 + 工作节点位数 */
    private static final long DATA_CENTER_SHIFT = SEQUENCE_BITS + WORKER_ID_BITES;

    /** 时间戳在最终 ID 中的左移位数 = 序号位数 + 工作节点位数 + 数据中心位数 */
    private static final long TIMESTAMP_LEFT_SHIFT = SEQUENCE_BITS + WORKER_ID_BITES + DATA_CENTER_BITS;

    /** 序号掩码，用于对序号做取模运算，保证序号不超过 8 bit 范围（0 ~ 255） */
    private static final long SEQUENCE_MASK = ~(-1L << SEQUENCE_BITS);

    /** 当前数据中心 ID */
    private long dataCenterId = 0;

    /** 当前工作节点 ID */
    private long workerId = 0;

    /** 当前毫秒内的序号 */
    private long sequence = 0L;

    /** 上一次生成 ID 的时间戳，用于检测时钟回拨和同毫秒序号递增 */
    private long lastTimestamp = -1L;

    /**
     * 默认构造函数，dataCenterId 和 workerId 均为 0
     * <p>
     * 适用于单机场景，或作为 Spring 容器启动前的兜底实例。
     * </p>
     *
     * @throws LeafIdException 如果当前时间早于 EPOCH
     */
    public LeafIdGenImpl() {
        if (timeGen() < EPOCH) {
            throw new LeafIdException("Leaf not support twepoch gt currentTime");
        }
    }

    /**
     * 指定 workerId 的构造函数，dataCenterId 默认为 0
     *
     * @param workerId 工作节点 ID，范围 0 ~ 255
     * @throws LeafIdException 如果 workerId 超出合法范围
     */
    public LeafIdGenImpl(int workerId) {
        this(0, workerId);
    }

    /**
     * 指定 dataCenterId 和 workerId 的构造函数
     *
     * @param dataCenterId 数据中心 ID，范围 0 ~ 31
     * @param workerId     工作节点 ID，范围 0 ~ 255
     * @throws LeafIdException 如果 dataCenterId 或 workerId 超出合法范围
     */
    public LeafIdGenImpl(int dataCenterId, int workerId) {
        if (timeGen() < EPOCH) {
            throw new LeafIdException("Leaf not support twepoch gt currentTime");
        }
        setDataCenterId(dataCenterId);
        setWorkerId(workerId);
    }

    /**
     * 设置数据中心 ID，校验范围合法性
     *
     * @param dataCenterId 数据中心 ID，范围 0 ~ 31
     * @throws LeafIdException 如果 dataCenterId 超出合法范围
     */
    private void setDataCenterId(int dataCenterId) {
        if (dataCenterId < 0 || dataCenterId > MAXIMUM_DATA_CENTER) {
            throw new LeafIdException("dataCenterID must gte 0 and lte {0}", MAXIMUM_DATA_CENTER);
        }
        this.dataCenterId = dataCenterId;
    }

    /**
     * 设置工作节点 ID，校验范围合法性
     *
     * @param workerId 工作节点 ID，范围 0 ~ 255
     * @throws LeafIdException 如果 workerId 超出合法范围
     */
    private void setWorkerId(int workerId) {
        if(workerId < 0 || workerId > MAXIMUM_WORKER){
            throw new LeafIdException( "workerID must gte 0 and lt {0}", MAXIMUM_WORKER);
        }

        this.workerId = workerId;
    }

    /**
     * 生成下一个全局唯一 ID（线程安全）
     * <p>
     * 核心逻辑：
     * <ol>
     *   <li>获取当前时间戳，检测是否发生时钟回拨</li>
     *   <li>若回拨 ≤ 5ms，等待 2 倍偏移时间后重试；若回拨 &gt; 5ms，直接抛异常</li>
     *   <li>同一毫秒内，序号递增（取模 256）；序号溢出时自旋等待下一毫秒</li>
     *   <li>新毫秒开始时，序号重置为 0</li>
     *   <li>按位拼接：时间戳 | 数据中心ID | 工作节点ID | 序号</li>
     * </ol>
     * </p>
     *
     * @return 64 位全局唯一 long 型 ID
     * @throws LeafIdException 时钟回拨超过容忍阈值或等待被中断
     */
    @Override
    public synchronized long nextId() {
        long timestamp = timeGen();

        // 时钟回拨检测与补偿
        if (timestamp < lastTimestamp) {
            long offset = lastTimestamp - timestamp;
            if (offset <= 5) {
                // 回拨 ≤ 5ms，等待 2 倍偏移时间后重新获取时间
                try {
                    wait(offset << 1);
                    timestamp = timeGen();
                    if (timestamp < lastTimestamp) {
                        throw new LeafIdException("Leaf Id Gen 异常，当前时间({})大于最后生成时间戳({}),补偿偏移后时间依赖没有对齐",timestamp,lastTimestamp);
                    }
                } catch (InterruptedException e) {
                    LOGGER.error("wait interrupted");
                    throw new LeafIdException("Leaf Id nextId 异常，补尝时间发生 Interrupted Exception");
                }
            } else {
                // 回拨 > 5ms，无法容忍，直接抛异常
                throw new LeafIdException("Leaf Id nextId 异常，最后时间戳({})大于当前时间戳({})",timestamp,lastTimestamp);
            }
        }

        if (lastTimestamp == timestamp) {
            // 同一毫秒内，序号递增并取模
            sequence = (sequence + 1) & SEQUENCE_MASK;
            if (sequence == 0) {
                // 序号溢出（256），自旋等待下一毫秒
                timestamp = tilNextMillis();
            }
        } else {
            // 新毫秒，序号重置为 0
            sequence = 0;
        }
        lastTimestamp = timestamp;

        // 按位拼接：时间戳 | 数据中心ID | 工作节点ID | 序号
        return  ((timestamp - EPOCH) << TIMESTAMP_LEFT_SHIFT)
                | (dataCenterId << DATA_CENTER_SHIFT)
                | (workerId << WORKER_ID_SHIFT)
                | sequence;
    }

    /**
     * 自旋等待直到下一毫秒
     * <p>
     * 当同一毫秒内序号耗尽时调用，避免生成重复 ID。
     * </p>
     *
     * @return 下一毫秒的时间戳
     */
    protected long tilNextMillis() {
        long timestamp = timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = timeGen();
        }
        return timestamp;
    }

    /**
     * 获取当前时间戳（毫秒）
     * <p>
     * 独立方法便于测试时 Mock 时间。
     * </p>
     *
     * @return 当前时间的毫秒时间戳
     */
    protected long timeGen() {
        return System.currentTimeMillis();
    }
}
