# jsf-mongodb 模块设计规格

## 目标

在 `jsf-components` 下创建 `jsf-mongodb` 模块，基于 `spring-mongodb-conventions` skill 规范，提供 Spring Data MongoDB 的通用公共类，使业务项目引入后即可获得：Snowflake ID 主键、审计字段自动填充、事务管理、默认审计人提供。

## 核心类

### BaseDoc（`io.soil.jsf.mongodb.doc.BaseMongoDoc`）

所有 Document 类的抽象基类，封装 ID 生成和审计字段。

| 字段 | 类型 | 注解 | 说明 |
|------|------|------|------|
| `id` | `Long` | `@Id` + `@JsonSerialize(using = ToStringSerializer.class)` | Snowflake ID，JSON 序列化为 String 防止 JS 精度丢失 |
| `createdAt` | `LocalDateTime` | `@CreatedDate` + `@Field("createdAt")` | 创建时间 |
| `updatedAt` | `LocalDateTime` | `@LastModifiedDate` + `@Field("updatedAt")` | 更新时间 |
| `createdBy` | `String` | `@CreatedBy` + `@Field("createdBy")` | 创建人 |
| `updatedBy` | `String` | `@LastModifiedBy` + `@Field("updatedBy")` | 更新人 |

`@PrePersist` 逻辑：`if (this.id == null) { this.id = LeafId.nextId(); }`

- 使用 `LeafId.nextId()` 而非 `SpringContextHolder.getBean()`，因为项目已有 LeafId 静态门面，更简洁
- `version` 和 `deleted` 不放入 BaseDoc，由子类按需添加

### MongoAutoConfig（`io.soil.jsf.mongodb.config.MongoAutoConfig`）

Spring Boot 自动配置类，注册以下 Bean：

1. `@EnableMongoAuditing` — 激活审计注解
2. `MongoTransactionManager` — 启用 `@Transactional`
3. `DefaultAuditorAware` — 默认审计人提供者

条件：`@ConditionalOnClass({MongoTemplate.class, MongoTransactionManager.class})`

### DefaultAuditorAware（`io.soil.jsf.mongodb.config.DefaultAuditorAware`）

实现 `AuditorAware<String>`：

1. 尝试从 `SecurityContextHolder` 获取当前认证用户名
2. 无认证时返回可配置的默认值（默认 `"system"`）
3. 默认值通过 `MongoProperties.auditorSystemFallback` 配置

### MongoProperties（`io.soil.jsf.mongodb.config.MongoProperties`）

配置属性绑定，前缀 `jsf.mongodb`：

| 属性 | 默认值 | 说明 |
|------|--------|------|
| `auditor-system-fallback` | `system` | 无认证时的默认审计人 |

## 依赖

```xml
<dependency>
  <groupId>io.soil.jsf</groupId>
  <artifactId>jsf-leaf-id</artifactId>
</dependency>
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-data-mongodb</artifactId>
  <scope>provided</scope>
</dependency>
<dependency>
  <groupId>com.fasterxml.jackson.core</groupId>
  <artifactId>jackson-databind</artifactId>
  <scope>provided</scope>
</dependency>
```

- `spring-boot-starter-data-mongodb` 设为 `provided`，由使用方引入具体驱动
- `jsf-leaf-id` 作为 Snowflake ID 来源

## 包结构

```
io.soil.jsf.mongodb
├── config
│   ├── MongoAutoConfig.java
│   ├── DefaultAuditorAware.java
│   └── MongoProperties.java
└── doc
    └── BaseDoc.java
```

## 不包含（YAGNI）

- ❌ `SoftDeleteRepository` — 无明确需求
- ❌ 自定义 Converter 注册 — 无通用场景
- ❌ Jackson 全局 Long→String 配置 — 由使用方按需配置
- ❌ 索引迁移脚本工具 — 由使用方选择 Mongock 等方案

## 使用方式

### 引入依赖

```xml
<dependency>
  <groupId>io.soil.jsf</groupId>
  <artifactId>jsf-mongodb</artifactId>
</dependency>
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-data-mongodb</artifactId>
</dependency>
```

### 定义 Document

```java
@Document(collection = "users")
public class UserDoc extends BaseDoc {
    @Field("name")
    private String name;

    @Field("deleted")
    private Boolean deleted = false;

    @PrePersist
    public void initDefaults() {
        super.prePersist();
        if (this.deleted == null) {
            this.deleted = false;
        }
    }
}
```

### 配置（可选）

```yaml
jsf:
  mongodb:
    auditor-system-fallback: anonymous
```

## BOM 注册

在 `jsf-components-dependencies/pom.xml` 中添加 `jsf-mongodb` 版本管理。
在 `jsf-components/pom.xml` 中添加 `jsf-mongodb` 模块。