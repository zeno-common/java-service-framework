# jsf-leaf-id

基于 Twitter Snowflake 算法改进的分布式唯一 ID 生成组件，支持静态配置和 JDBC 数据库两种工作节点协调方式。引入即生效，零代码侵入。

## 功能特性

- **Snowflake ID 生成** — 64 位唯一 ID，单节点每毫秒可生成 256 个
- **静态 workerId** — 单机或固定节点场景，配置即用
- **JDBC 协调** — 多节点自动分配 workerId，乐观锁抢占 + 心跳续约
- **时钟回拨容忍** — 5ms 以内自动补偿，超过则抛异常
- **本地文件降级** — 数据库不可用时从本地缓存恢复 workerId

## ID 结构

```
| 1 bit  |       42 bit        |   5 bit    |   8 bit    |   8 bit   |
| 符号位  |  时间戳（相对 EPOCH） |  数据中心ID  |  工作节点ID  |  毫秒内序号  |
|   0    |  约 139 年可用        |  0 ~ 31    |  0 ~ 255   |  0 ~ 255  |
```

- **EPOCH**：2026-01-01 00:00:00 UTC
- **单节点 QPS**：约 25.6 万/秒

## 引入依赖

前置条件：通过 [jsf-bom](../../jsf-bom/README.md) 管理依赖版本。

```xml
<dependency>
    <groupId>io.soil.jsf</groupId>
    <artifactId>jsf-leaf-id</artifactId>
</dependency>
```

JDBC 模式需额外引入数据库驱动和连接池：

```xml
<dependency>
    <groupId>org.mybatis.spring.boot</groupId>
    <artifactId>mybatis-spring-boot-starter</artifactId>
</dependency>
<dependency>
    <groupId>com.mysql</groupId>
    <artifactId>mysql-connector-j</artifactId>
</dependency>
```

## 使用方式

### 生成 ID

```java
long id = LeafId.nextId();
```

### 静态 workerId 模式（默认）

无需任何配置，默认 dataCenterId = 0、workerId = 0。适用于单机部署。

```yaml
jsf:
  leaf:
    holder-type: default    # 可省略，默认值
    data-center-id: 1       # 可选，默认 0，范围 0 ~ 31
    worker-id: 1            # 可选，默认 0，范围 0 ~ 255
```

### JDBC 协调模式

多节点部署时，通过数据库自动分配和回收 workerId。

**1. 初始化数据库表**

执行 [init.sql](script/mysql/init.sql)，创建 `jsf_leaf_holder` 表并预置 32 个 worker 槽位（id 0~31）。

> 如需支持更多节点，在表中追加记录即可，上限 256（8 bit）。

**2. 配置**

```yaml
jsf:
  leaf:
    data-center-id: 1
    holder-type: jdbc    # 可选，默认 0，范围 0 ~ 31
    ip-coordinator:
      ip: 192.168.1.20      # 远程目标 IP，用于探测本机出口 IP
      port: 3306             # 远程目标端口
```

**3. 启动流程**

- 根据 `IP:Port` 查询是否已有分配记录，有则复用
- 无记录则抢占一个 12 小时内未心跳的空闲 worker
- 每 20 分钟上报心跳，保持 worker 活跃
- workerId 缓存到本地文件 `./jsf-leaf/workerID.properties`，数据库不可用时降级恢复

## 配置项

| 配置项 | 默认值 | 说明 |
|--------|--------|------|
| `jsf.leaf.holder-type` | `default` | 协调器类型：`default`（静态）/ `jdbc`（数据库） |
| `jsf.leaf.data-center-id` | `0` | 数据中心 ID，范围 0 ~ 31（5 bit） |
| `jsf.leaf.worker-id` | `0` | 静态模式的 workerId，范围 0 ~ 255，仅 `default` 模式生效 |
| `jsf.leaf.ip-coordinator.ip` | - | JDBC 模式：远程目标 IP，用于探测本机出口 IP |
| `jsf.leaf.ip-coordinator.port` | - | JDBC 模式：远程目标端口 |

## 异常处理

| 异常场景 | 异常类 | 说明 |
|----------|--------|------|
| 时钟回拨 > 5ms | `LeafIdException` | 需排查 NTP 同步问题 |
| workerId / dataCenterId 超出范围 | `LeafIdException` | 检查配置或数据库预置数据 |
| 无可用 worker | `LeafIdException` | 所有 worker 均被占用，需扩容 |
| 乐观锁抢占失败 | `LeafIdException` | 并发冲突，重试即可 |

## 项目结构

```
io.soil.jsf.leaf/
├── IDGen.java                          # ID 生成器接口
├── LeafAutoConfig.java                 # Spring Boot 自动配置
├── config/
│   └── LeafProperties.java             # 配置属性绑定
├── exception/
│   └── LeafIdException.java            # Leaf 异常
├── gen/
│   ├── ILeafGenerator.java             # 工作节点协调器接口
│   ├── LeafDefaultGenerator.java       # 静态 workerId 协调器
│   ├── LeafId.java                     # 静态工具门面
│   ├── LeafIdGenImpl.java              # Snowflake ID 生成器核心
│   └── LeafJdbcGenerator.java          # JDBC 数据库协调器
└── mapper/
    ├── LeafHolderMapper.java           # 工作节点 Mapper
    └── po/
        └── LeafHolderPO.java           # 工作节点 PO
```