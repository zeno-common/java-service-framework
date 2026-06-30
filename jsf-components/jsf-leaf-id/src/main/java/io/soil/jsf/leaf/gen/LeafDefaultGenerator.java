package io.soil.jsf.leaf.gen;


/**
 * 静态配置的 Leaf 工作节点协调器
 * <p>
 * 通过构造函数直接传入固定的 dataCenterId 和 workerId，不涉及任何远程协调逻辑。
 * 适用于单机部署或节点数量固定、可手动分配 workerId 的场景。
 * </p>
 * <p>
 * 使用方式：在配置文件中设置 {@code gaea.leaf.holder-type=default}（默认值），
 * 并通过 {@code gaea.leaf.worker-id} 指定 workerId，
 * 通过 {@code jsf.leaf.data-center-id} 指定 dataCenterId。
 * </p>
 *
 * @see ILeafGenerator
 * @see io.soil.jsf.leaf.LeafAutoConfig#defaultHolder(Integer, Integer)
 */
public class LeafDefaultGenerator implements ILeafGenerator {

    private final Integer dataCenterId;

    private final Integer workerId;

    /**
     * 构造静态工作节点协调器，dataCenterId 默认为 0
     *
     * @param workerId 固定的工作节点 ID，范围 0 ~ 255
     */
    public LeafDefaultGenerator(Integer workerId) {
        this(0, workerId);
    }

    /**
     * 构造静态工作节点协调器
     *
     * @param dataCenterId 固定的数据中心 ID，范围 0 ~ 31
     * @param workerId     固定的工作节点 ID，范围 0 ~ 255
     */
    public LeafDefaultGenerator(Integer dataCenterId, Integer workerId) {
        this.dataCenterId = dataCenterId;
        this.workerId = workerId;
    }

    /**
     * 静态模式无需初始化，此方法为空实现
     */
    @Override
    public void init() {}

    @Override
    public Integer dataCenterId() {
        return dataCenterId;
    }

    @Override
    public Integer workerId() {
        return workerId;
    }
}
