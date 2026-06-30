package io.soil.jsf.leaf.mapper.po;

import lombok.Data;

import java.util.Date;

/**
 * Leaf 工作节点持有者持久化对象
 * <p>
 * 对应数据库表 {@code jsf_leaf_holder}，记录每个工作节点的占用状态和心跳信息。
 * </p>
 */
@Data
public class LeafHolderPO {

    /** 工作节点 ID，即 Snowflake 算法中的 workerId，范围 0 ~ 255 */
    private Integer id;

    /** 占用此 worker 的节点唯一标识，格式为 IP:Port */
    private String nodeKey;

    /** 上一次心跳时间戳（毫秒），用于判断 worker 是否仍在使用中 */
    private Long lastHeartbeat;

    /** 记录创建时间 */
    private Date createTime;
}
