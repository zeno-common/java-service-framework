package io.soil.jsf.leaf.gen;

import io.soil.jsf.leaf.exception.LeafIdException;
import io.soil.jsf.leaf.mapper.LeafHolderMapper;
import io.soil.jsf.leaf.mapper.po.LeafHolderPO;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 基于 JDBC 数据库的 Leaf 工作节点协调器
 * <p>
 * 通过数据库表 {@code jsf_leaf_holder} 实现分布式环境下的 workerId 自动分配与回收，
 * 替代 ZooKeeper 方案，降低外部依赖。
 * </p>
 * <p>
 * 核心流程：
 * <ol>
 *   <li>启动时根据 nodeKey（IP:Port）查询是否已有分配记录</li>
 *   <li>若已有记录，复用原 workerId；否则抢占一个 12 小时内未心跳的空闲 worker</li>
 *   <li>使用乐观锁（last_heartbeat 作为版本号）保证并发抢占安全</li>
 *   <li>启动定时心跳线程，每 20 分钟更新一次 last_heartbeat</li>
 *   <li>将 workerId 缓存到本地文件，数据库不可用时作为降级恢复手段</li>
 * </ol>
 * </p>
 *
 * @see ILeafGenerator
 * @see io.soil.jsf.leaf.mapper.LeafHolderMapper
 */
public class LeafJdbcGenerator implements ILeafGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(LeafJdbcGenerator.class);

  /** 数据中心 ID */
  private int dataCenterID;

  /** 当前节点分配到的工作节点 ID */
  private int workerID;

    /** 本地 workerId 缓存文件路径，用于数据库不可用时的降级恢复 */
    private static final String PROP_PATH = "./jsf-leaf/workerID.properties";

    /** 当前节点的唯一标识，格式为 IP:Port */
    private final String nodeKey;

    /** 上一次心跳更新时间，用于防止时钟回拨导致重复更新 */
    private long lastUpdateTime;

    /** 12 小时的毫秒数，用于判断 worker 是否空闲可回收 */
    private static final long HALF_DAY_MILLIS = 12 * 60 * 60 * 1000L;

    /** 数据库操作 Mapper */
    private final LeafHolderMapper leafHolderMapper;

    /**
     * 构造 JDBC 工作节点协调器
     *
     * @param dataCenterID 数据中心 ID，范围 0 ~ 31
     * @param workerMapper 数据库 Mapper，用于操作 jsf_leaf_holder 表
     * @param nodeKey      当前节点的唯一标识，格式为 IP:Port
     */
    public LeafJdbcGenerator(Integer dataCenterID, LeafHolderMapper workerMapper, String nodeKey) {
        this.dataCenterID = dataCenterID;
        this.nodeKey = nodeKey;
        this.leafHolderMapper = workerMapper;
    }

    /**
     * 初始化：分配 workerId、缓存到本地文件、启动心跳
     * <p>
     * 优先从数据库获取 workerId；若数据库操作失败，则从本地缓存文件恢复。
     * 本地缓存是保证数据库不可用时服务仍能启动的降级手段。
     * </p>
     */
    @Override
    public void init() {
        try {
            workerID = doGetWorkerId(nodeKey);
            updateLocalWorkerID(workerID);
            startHeartbeat();
        } catch (Exception e) {
            LOGGER.error("Start node ERROR: ", e);
            try {
                // 数据库不可用，尝试从本地文件恢复 workerId
                Properties properties = new Properties();
                properties.load(Files.newInputStream(Paths.get(PROP_PATH)));
                workerID = Integer.parseInt(properties.getProperty("workerID"));
                LOGGER.warn("START FAILED, use local node file properties workerID-{}", workerID);
            } catch (Exception e2) {
                throw new RuntimeException("Read file error",e2);
            }
        }
    }

    @Override
    public Integer workerId() {
        return workerID;
    }

    @Override
    public Integer dataCenterId() {
        return dataCenterID;
    }

    /**
     * 从数据库获取当前节点的 workerId
     * <p>
     * 分配策略：
     * <ol>
     *   <li>根据 nodeKey 查询是否已有分配记录，有则复用</li>
     *   <li>无记录则查找 last_heartbeat 超过 12 小时的空闲 worker（行锁保证并发安全）</li>
     *   <li>使用乐观锁（last_heartbeat 作为版本号）抢占 worker</li>
     * </ol>
     * </p>
     *
     * @param nodeKey 当前节点的唯一标识
     * @return 分配到的 workerId
     * @throws LeafIdException 无可用 worker 或抢占失败
     */
    private Integer doGetWorkerId(String nodeKey){
        LeafHolderPO leafWorker = leafHolderMapper.getWorkerIdByNodeKey(nodeKey);
        if (Objects.isNull(leafWorker)){
            // 新节点，查找 12 小时内未心跳的空闲 worker
            long now = System.currentTimeMillis();
            leafWorker = leafHolderMapper.getOneWorkerBefore(now - HALF_DAY_MILLIS);
            if (leafWorker == null) {
                throw new LeafIdException("No available workerId, all workerId are in use!");
            }
            LOGGER.info("Obtain an new worker endpoint-{} workId-{} and start SUCCESS", nodeKey, leafWorker.getId());
        }else{
            // 已有记录，复用原 workerId
            LOGGER.info("Reuse an worker endpoint-{} workId-{} and start SUCCESS", nodeKey, leafWorker.getId());
        }

        // 乐观锁抢占：用 last_heartbeat 作为版本号，防止并发抢占同一 worker
        long now = System.currentTimeMillis();
        if(!leafHolderMapper.occupyWorker(leafWorker.getId(),nodeKey,leafWorker.getLastHeartbeat(),now)){
            throw new LeafIdException("Failed to occupy the leaf worker({0})",leafWorker.getId());
        }

        return leafWorker.getId();
    }

    /**
     * 启动心跳定时任务
     * <p>
     * 使用单线程定时调度器，每 20 分钟执行一次心跳更新，
     * 保持当前节点在数据库中的活跃状态，防止 workerId 被其他节点回收。
     * </p>
     */
    private void startHeartbeat() {
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor(r -> {
            return new Thread(r, "gaea-leaf-uid-heartbeat");
        });
        executor.scheduleWithFixedDelay(this::updateHeartbeat, 20L, 20L, TimeUnit.MINUTES);
    }

    /**
     * 更新心跳时间戳
     * <p>
     * 将当前时间写入数据库的 last_heartbeat 字段，表示此 worker 仍在使用中。
     * 包含时钟回拨检测：若当前时间小于上次更新时间则跳过本次更新。
     * </p>
     */
    private void updateHeartbeat() {
        try {
            if (System.currentTimeMillis() < lastUpdateTime) {
                return;
            }

            long now = System.currentTimeMillis();
            leafHolderMapper.updateLastHeartbeat(workerID,now);
            lastUpdateTime = now;
        } catch (Exception e) {
            LOGGER.error("update heartbeat error", e);
        }
    }

    /**
     * 将 workerId 缓存到本地文件系统
     * <p>
     * 写入路径为 {@value #PROP_PATH}，内容格式为 {@code workerID=N}。
     * 当数据库不可用时（如网络故障、数据库宕机），可从本地文件恢复 workerId，
     * 保证服务仍能正常启动。
     * </p>
     *
     * @param workerID 需要缓存的 workerId
     */
    private void updateLocalWorkerID(int workerID) {
        File leafConfFile = new File(PROP_PATH);
        boolean exists = leafConfFile.exists();
        LOGGER.info("file exists status is {}", exists);
        if (exists) {
            try {
                FileUtils.writeStringToFile(leafConfFile, "workerID=" + workerID, StandardCharsets.UTF_8);
                LOGGER.info("update file cache workerID is {}", workerID);
            } catch (IOException e) {
                LOGGER.error("update file cache error ", e);
            }
        } else {
            // 文件不存在，需创建父目录和文件
            try {
                boolean mkdirs = leafConfFile.getParentFile().mkdirs();
                LOGGER.info("init local file cache create parent dis status is {}, worker id is {}", mkdirs, workerID);
                if (mkdirs) {
                    if (leafConfFile.createNewFile()) {
                        FileUtils.writeStringToFile(leafConfFile, "workerID=" + workerID,StandardCharsets.UTF_8);
                        LOGGER.info("local file cache workerID is {}", workerID);
                    }
                } else {
                    LOGGER.warn("create parent dir error===");
                }
            } catch (IOException e) {
                LOGGER.warn("create workerID conf file error", e);
            }
        }
    }
}
