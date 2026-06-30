package io.soil.jsf.mongodb.config;

import io.soil.jsf.mongodb.doc.BaseMongoDoc;
import io.soil.jsf.mongodb.doc.MongoDocIdCallback;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.core.MongoTemplate;

/**
 * jsf-mongodb 自动配置类
 * <p>
 * 当 classpath 下存在 {@link MongoTemplate} 时自动激活，提供以下能力：
 * <ul>
 *   <li>{@link EnableMongoAuditing} — 激活审计注解（@CreatedDate / @LastModifiedDate / @CreatedBy / @LastModifiedBy）</li>
 *   <li>{@link MongoTransactionManager} — 启用 @Transactional 多文档事务支持</li>
 *   <li>{@link MongoDocIdCallback} — Snowflake ID 自动生成回调</li>
 * </ul>
 * </p>
 * <p>
 * 审计人（createdBy / updatedBy）的自动填充需要外部模块提供 {@code AuditorAware} Bean。
 * {@link BaseMongoDoc} 的泛型参数 {@code <U>} 决定审计人字段类型，
 * {@code AuditorAware<U>} 的泛型必须与之匹配。
 * </p>
 * <p>
 * 前置条件：
 * <ul>
 *   <li>引入 {@code spring-boot-starter-data-mongodb}</li>
 *   <li>引入 {@code jsf-leaf-id}（Snowflake ID 来源）</li>
 *   <li>配置 MongoDB 连接信息（{@code spring.data.mongodb.uri}）</li>
 *   <li>提供 {@code AuditorAware} Bean（由外部模块实现）</li>
 * </ul>
 * </p>
 */
@Configuration
@ConditionalOnClass(MongoTemplate.class)
@EnableMongoAuditing
public class MongoAutoConfig {

    /**
     * MongoDB 事务管理器
     * <p>
     * 启用后可在 Service 层使用 {@code @Transactional} 进行多文档事务。
     * 注意：事务需要 MongoDB 副本集（Replica Set），单机模式不支持。
     * </p>
     *
     * @param mongoDatabaseFactory MongoDB 数据库工厂
     * @return 事务管理器
     */
    @Bean
    @ConditionalOnMissingBean
    public MongoTransactionManager transactionManager(MongoDatabaseFactory mongoDatabaseFactory) {
        return new MongoTransactionManager(mongoDatabaseFactory);
    }

    /**
     * Snowflake ID 自动生成回调
     * <p>
     * 在 Document 保存前自动为 ID 为 null 的 {@link BaseMongoDoc} 子类生成 Snowflake ID。
     * </p>
     *
     * @return Entity Callback 实例
     */
    @Bean
    public MongoDocIdCallback leafIdCallback() {
        return new MongoDocIdCallback();
    }
}
