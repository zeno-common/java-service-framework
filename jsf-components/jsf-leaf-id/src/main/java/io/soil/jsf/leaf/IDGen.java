package io.soil.jsf.leaf;

/**
 * ID 生成器顶层接口
 * <p>
 * 定义分布式唯一 ID 的生成契约，所有 ID 生成策略均需实现此接口。
 * 当前唯一实现为基于 Twitter Snowflake 算法改进的 {@link io.soil.jsf.leaf.gen.LeafIdGenImpl}。
 * </p>
 *
 * @see io.soil.jsf.leaf.gen.LeafIdGenImpl
 */
public interface IDGen {

    /**
     * 获取下一个唯一 ID
     *
     * @return 全局唯一的 64 位 long 型 ID
     */
    long nextId();
}
