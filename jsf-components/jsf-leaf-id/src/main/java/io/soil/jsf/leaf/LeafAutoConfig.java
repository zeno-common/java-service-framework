package io.soil.jsf.leaf;

import io.soil.jsf.leaf.config.LeafProperties;
import io.soil.jsf.leaf.mapper.LeafHolderMapper;
import io.soil.jsf.leaf.gen.LeafDefaultGenerator;
import io.soil.jsf.leaf.gen.LeafJdbcGenerator;
import io.soil.jsf.leaf.gen.ILeafGenerator;
import io.soil.jsf.util.ip.IpUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Leaf ID 自动配置类
 * <p>
 * 根据 {@code gaea.leaf.holder-type} 配置项决定使用哪种工作节点协调器：
 * <ul>
 *   <li>{@code default}（默认）— 使用静态配置的 workerId</li>
 *   <li>{@code jdbc} — 使用数据库协调自动分配 workerId</li>
 * </ul>
 * </p>
 * <p>
 * 注册的 {@link ILeafGenerator} Bean 会被 {@link io.soil.jsf.leaf.gen.LeafId} 构造函数注入，
 * 用于初始化 Snowflake ID 生成器的 dataCenterId 和 workerId。
 * </p>
 */
@Configuration
@EnableConfigurationProperties({LeafProperties.class})
@ComponentScan({"io.soil.jsf.leaf"})
public class LeafAutoConfig {

  /**
   * 静态配置的工作节点协调器
   * <p>
   * 当 {@code gaea.leaf.holder-type} 为 {@code default} 或未配置时激活。
   * workerId 通过 {@code jsf.leaf.worker-id} 指定，默认为 0。
   * dataCenterId 通过 {@code jsf.leaf.data-center-id} 指定，默认为 0。
   * </p>
   *
   * @param workerId     配置的 workerId，默认 0
   * @param dataCenterId 配置的 dataCenterId，默认 0
   * @return 静态工作节点协调器
   */
  @Bean
  @ConditionalOnMissingBean
  @ConditionalOnProperty(prefix = "gaea.leaf", name = "holder-type", havingValue = "default", matchIfMissing = true)
  public ILeafGenerator defaultHolder(
    @Value("${jsf.leaf.data-center-id:0}") Integer dataCenterId,
    @Value("${jsf.leaf.worker-id:0}") Integer workerId) {
    return new LeafDefaultGenerator(dataCenterId, workerId);
  }

  /**
   * 基于 JDBC 的工作节点协调器
   * <p>
   * 当 {@code gaea.leaf.holder-type} 为 {@code jdbc} 时激活。
   * 通过数据库表 {@code jsf_leaf_holder} 自动分配和心跳维护 workerId。
   * </p>
   * <p>
   * nodeKey 由本机出口 IP + 服务端口组成，用于在数据库中唯一标识当前节点。
   * </p>
   *
   * @param properties   Leaf 配置属性
   * @param workerMapper 数据库 Mapper
   * @param serverPort   当前服务端口，用于构建 nodeKey
   * @return JDBC 工作节点协调器（已初始化）
   */
  @Bean
  @ConditionalOnProperty(prefix = "gaea.leaf", name = "holder-type", havingValue = "jdbc")
  public ILeafGenerator leafWorkerJdbcHolder(
    @Value("${jsf.leaf.data-center-id:0}") Integer dataCenterId,
    LeafProperties properties,
    LeafHolderMapper workerMapper,
    @Value("${server.port:0}") Integer serverPort) {

    LeafProperties.ipCoordinator ipCoordinator = properties.getIpCoordinator();
    // 通过探测到远程数据库的出口 IP 确定本机 IP，拼接端口作为节点唯一标识
    String nodeKey = IpUtil.getLocalConnectedIp(ipCoordinator.getIp(), ipCoordinator.getPort());
    nodeKey += ":" + serverPort;
    LeafJdbcGenerator generator = new LeafJdbcGenerator(dataCenterId, workerMapper, nodeKey);
    generator.init();
    return generator;
  }
}
