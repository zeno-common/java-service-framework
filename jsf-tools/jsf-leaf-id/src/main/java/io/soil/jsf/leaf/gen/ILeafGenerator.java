package io.soil.jsf.leaf.gen;

/**
 * Leaf 工作节点协调器接口
 * <p>
 * 负责在分布式环境中为当前节点分配唯一的工作节点 ID（workerId）和数据中心 ID（dataCenterId），
 * 是 Snowflake 算法中区分不同实例的核心机制。
 * </p>
 * <p>
 * 当前提供两种实现：
 * <ul>
 *   <li>{@link LeafDefaultGenerator} — 静态配置 dataCenterId 和 workerId，适用于单机或固定节点场景</li>
 *   <li>{@link LeafJdbcGenerator} — 基于 JDBC 数据库协调，自动分配和回收 workerId</li>
 * </ul>
 * </p>
 *
 * @see LeafDefaultGenerator
 * @see LeafJdbcGenerator
 */
public interface ILeafGenerator {

    /**
     * 初始化工作节点协调器
     * <p>
     * 执行 workerId 和 dataCenterId 的分配逻辑，例如从数据库抢占、从本地文件恢复等。
     * 必须在调用 {@link #workerId()} 或 {@link #dataCenterId()} 之前完成初始化。
     * </p>
     */
    void init();

    /**
     * 获取当前节点的工作节点 ID
     *
     * @return 工作节点 ID，范围 0 ~ 255（8 bit）
     */
    default Integer workerId() {
        return 0;
    }

    /**
     * 获取当前节点所属的数据中心 ID
     * <p>
     * 数据中心 ID 用于区分不同机房或部署单元，占 5 bit，范围 0 ~ 31。
     * 默认返回 0，表示未配置数据中心。
     * </p>
     *
     * @return 数据中心 ID，范围 0 ~ 31（5 bit）
     */
    default Integer dataCenterId() {
        return 0;
    }
}
