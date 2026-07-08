package io.soil.jsf.leaf.gen;

import io.soil.jsf.leaf.IDGen;
import org.springframework.stereotype.Component;

/**
 * Leaf ID 静态工具门面
 * <p>
 * 封装 {@link IDGen} 实例，提供全局静态方法 {@link #nextId()}，
 * 使业务代码无需注入任何 Bean 即可直接生成分布式唯一 ID。
 * </p>
 * <p>
 * 使用方式：
 * <pre>{@code
 *   long id = LeafId.nextId();
 * }</pre>
 * <p>
 * 初始化流程：
 * <ol>
 *   <li>Spring 容器启动前，使用默认 dataCenterId=0、workerId=0 的 {@link LeafIdGenImpl} 作为兜底</li>
 *   <li>Spring 容器启动后，由 {@link io.soil.jsf.leaf.LeafAutoConfig} 注入 {@link ILeafGenerator}，
 *       通过构造函数替换为携带正确 dataCenterId 和 workerId 的 {@link LeafIdGenImpl} 实例</li>
 * </ol>
 * </p>
 *
 * @see LeafIdGenImpl
 * @see io.soil.jsf.leaf.LeafAutoConfig
 */
@Component
public class LeafId {

    /**
     * 底层 ID 生成器实例
     * <p>
     * 使用 volatile 保证多线程可见性，确保 Spring 初始化替换后所有线程都能感知。
     * </p>
     */
    private static volatile IDGen ID_GEN = new LeafIdGenImpl();

    /**
     * 构造注入，用协调器分配的 dataCenterId 和 workerId 替换默认实例
     *
     * @param generator 工作节点协调器，提供当前节点的 dataCenterId 和 workerId
     */
    public LeafId(ILeafGenerator generator){
        ID_GEN = new LeafIdGenImpl(generator.dataCenterId(), generator.workerId());
    }

    /**
     * 生成下一个全局唯一 ID
     * <p>
     * 线程安全，内部通过 synchronized 保证同一时刻只有一个线程执行 ID 生成逻辑。
     * </p>
     *
     * @return 64 位全局唯一 long 型 ID
     */
    public static long nextId() {
        return ID_GEN.nextId();
    }
}
