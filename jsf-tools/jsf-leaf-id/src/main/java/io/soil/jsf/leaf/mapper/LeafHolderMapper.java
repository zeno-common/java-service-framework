package io.soil.jsf.leaf.mapper;

import io.soil.jsf.leaf.mapper.po.LeafHolderPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * Leaf 工作节点持有者 Mapper
 * <p>
 * 操作 {@code jsf_leaf_holder} 表，提供工作节点的查询、抢占和心跳更新功能。
 * 配合 {@link io.soil.jsf.leaf.gen.LeafJdbcGenerator} 实现基于数据库的 workerId 协调。
 * </p>
 */
@Mapper
public interface LeafHolderMapper {

    /**
     * 根据节点标识查询工作节点记录
     * <p>
     * 用于节点重启时复用之前分配的 workerId。
     * </p>
     *
     * @param nodeKey 节点唯一标识（IP:Port）
     * @return 工作节点记录，不存在则返回 null
     */
    @Select("SELECT id, node_key, last_heartbeat, create_time FROM jsf_leaf_holder WHERE node_key = #{nodeKey}")
    LeafHolderPO getWorkerIdByNodeKey(@Param("nodeKey") String nodeKey);

    /**
     * 查询一个心跳时间早于指定时间戳的空闲工作节点（带行锁）
     * <p>
     * 使用 {@code FOR UPDATE} 行锁保证并发场景下同一 worker 不会被多个节点同时选中。
     * 心跳超过 12 小时未更新的 worker 视为空闲可回收。
     * </p>
     *
     * @param timestamp 心跳阈值时间戳，早于此时间的 worker 视为空闲
     * @return 空闲的工作节点记录，无可用节点时返回 null
     */
    @Select("SELECT id, node_key, last_heartbeat, create_time FROM jsf_leaf_holder WHERE last_heartbeat < #{timestamp} limit 1 for update")
    LeafHolderPO getOneWorkerBefore(@Param("timestamp") Long timestamp);

    /**
     * 使用乐观锁抢占工作节点
     * <p>
     * 以 {@code last_heartbeat} 作为版本号，只有当数据库中的 last_heartbeat 与传入值一致时才更新成功，
     * 防止并发场景下同一 worker 被多个节点抢占。
     * </p>
     *
     * @param id            工作节点 ID
     * @param nodeKey       当前节点的唯一标识
     * @param lastHeartBeat 乐观锁版本号（上次心跳时间戳）
     * @param timestamp     新的心跳时间戳
     * @return 抢占成功返回 true，版本号不匹配返回 false
     */
    @Update("UPDATE jsf_leaf_holder SET node_key=#{nodeKey}, last_heartbeat = #{timestamp} WHERE id = #{id} and last_heartbeat=#{lastHeartBeat}")
    Boolean occupyWorker(@Param("id")Integer id,@Param("nodeKey")String nodeKey,@Param("lastHeartBeat") Long lastHeartBeat,@Param("timestamp") Long timestamp);

    /**
     * 更新工作节点的心跳时间戳
     * <p>
     * 由定时心跳任务调用，保持 worker 在数据库中的活跃状态，
     * 防止因长时间未更新心跳而被其他节点回收。
     * </p>
     *
     * @param id        工作节点 ID
     * @param timestamp 当前时间戳
     * @return 更新成功返回 true
     */
    @Update("UPDATE jsf_leaf_holder SET last_heartbeat = #{timestamp} WHERE id = #{id}")
    Boolean updateLastHeartbeat(@Param("id")Integer id,@Param("timestamp") Long timestamp);
}
