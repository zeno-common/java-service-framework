package io.soil.jsf.leaf.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Leaf ID 生成器配置属性
 * <p>
 * 通过 {@code jsf.leaf} 前缀绑定配置文件中的属性，
 * 控制工作节点协调器的类型和 IP 协调参数。
 * </p>
 * <p>
 * 注意：{@code @ConfigurationProperties} 绑定前缀为 {@code jsf.leaf}，
 * 而 {@link io.soil.jsf.leaf.LeafAutoConfig} 中的条件注解使用 {@code gaea.leaf} 前缀，
 * 两者分别控制属性绑定和 Bean 激活条件。
 * </p>
 * <p>
 * 配置示例：
 * <pre>
 * # Bean 激活条件（gaea.leaf 前缀）
 * gaea.leaf.holder-type=jdbc
 *
 * # 属性绑定（jsf.leaf 前缀，Spring Boot 自动转换驼峰为 kebab-case）
 * jsf.leaf.data-center-id=1
 * jsf.leaf.worker-id=0
 * jsf.leaf.ip-coordinator.ip=192.168.1.100
 * jsf.leaf.ip-coordinator.port=3306
 * </pre>
 * </p>
 *
 * @see io.soil.jsf.leaf.LeafAutoConfig
 */
@Data
@ConfigurationProperties(prefix = "jsf.leaf")
public class LeafProperties {

    /**
     * 工作节点协调器类型
     * <ul>
     *   <li>{@code default} — 静态配置 workerId（默认值）</li>
     *   <li>{@code jdbc} — 基于 JDBC 数据库自动分配 workerId</li>
     * </ul>
     */
    private String holderType;

    /**
     * 数据中心 ID，范围 0 ~ 31（5 bit）
     * <p>
     * 用于区分不同机房或部署单元，在 ID 中占 5 bit。
     * 默认为 0，表示未配置数据中心。
     * </p>
     * <p>
     * 配置路径：{@code jsf.leaf.data-center-id}
     * </p>
     */
    private Integer dataCenterId;

    /**
     * IP 协调器配置
     * <p>
     * 配置路径：{@code jsf.leaf.ip-coordinator.*}（Spring Boot 自动转换驼峰为 kebab-case）
     * </p>
     */
    private ipCoordinator ipCoordinator;

    /**
     * IP 协调器参数
     * <p>
     * 通过探测本机到远程目标的出口 IP，拼接服务端口生成 nodeKey（格式：IP:Port），
     * 用于在数据库中唯一标识和区分不同工作节点。
     * </p>
     * <p>
     * 配置示例：
     * <pre>
     * jsf.leaf.ip-coordinator.ip=192.168.1.100
     * jsf.leaf.ip-coordinator.port=3306
     * </pre>
     * </p>
     */
    @Data
    public static class ipCoordinator {
        /** 远程目标 IP，用于探测本机到该目标的出口 IP */
        private String ip;

        /** 远程目标端口 */
        private Integer port;
    }
}
