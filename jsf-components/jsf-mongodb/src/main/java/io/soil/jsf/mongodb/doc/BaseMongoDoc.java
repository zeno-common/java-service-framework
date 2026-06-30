package io.soil.jsf.mongodb.doc;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.soil.jsf.leaf.gen.LeafId;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.OffsetDateTime;

/**
 * MongoDB Document 抽象基类
 * <p>
 * 封装所有 Document 共有的 ID 生成和审计字段，确保一致性并减少样板代码。
 * </p>
 * <p>
 * 泛型参数 {@code <U>} 定义审计人（createdBy / updatedBy）的字段类型，默认使用 {@link Long}。
 * 子类可按需指定其他类型（如 {@link String}），以适配不同的 AuditorAware 实现。
 * </p>
 * <p>
 * 生成的 64 位 ID 结构（与 jsf-leaf-id 一致）：
 * <pre>
 * | 1 bit  |       42 bit        |   5 bit    |   8 bit    |   8 bit   |
 * | 符号位  |  时间戳（相对 EPOCH） |  数据中心ID  |  工作节点ID  |  毫秒内序号  |
 * </pre>
 * </p>
 * <p>
 * 关键设计：
 * <ul>
 *   <li>ID 使用 Snowflake 算法（通过 {@link LeafId#nextId()}），存储为 Long (int64)</li>
 *   <li>JSON 序列化时 ID 自动转为 String，防止 JavaScript 精度丢失</li>
 *   <li>审计字段由 Spring Data MongoDB Auditing 自动填充</li>
 *   <li>ID 生成通过 {@link MongoDocIdCallback}（Entity Callback）在保存前自动完成</li>
 *   <li>审计人类型通过泛型参数 {@code <U>} 指定，默认 {@link Long}，子类可覆盖为 {@link String} 等</li>
 *   <li>{@code version}（乐观锁）和 {@code deleted}（逻辑删除）不在此基类中，由子类按需添加</li>
 * </ul>
 * </p>
 * <p>
 * 使用示例：
 * <pre>{@code
 * // 审计人为 Long 类型（默认）
 * @Document(collection = "users")
 * public class UserDoc extends BaseDoc<Long> {
 *     @Field("name")
 *     private String name;
 * }
 *
 * // 审计人为 String 类型
 * @Document(collection = "orders")
 * public class OrderDoc extends BaseDoc<String> {
 *     @Field("orderNo")
 *     private String orderNo;
 * }
 * }</pre>
 * </p>
 *
 * @param <U> 审计人字段类型，默认 Long
 * @see LeafId
 * @see MongoDocIdCallback
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public abstract class BaseMongoDoc<U> {

    /**
     * 主键 ID，使用 Snowflake 算法生成
     * <p>
     * 通过 {@link MongoDocIdCallback} 在保存前自动生成。
     * </p>
     */
    @Id
    private Long id;

    @CreatedDate
    @Field("createdAt")
    private OffsetDateTime createdAt;

    @LastModifiedDate
    @Field("updatedAt")
    private OffsetDateTime updatedAt;

    /**
     * 创建人
     * <p>
     * 类型由泛型参数 {@code <U>} 决定，默认为 {@link Long}。
     * 由 Spring Data MongoDB Auditing 通过 {@code AuditorAware<U>} Bean 自动填充。
     * </p>
     */
    @CreatedBy
    @Field("createdBy")
    private U createdBy;

    /**
     * 更新人
     * <p>
     * 类型由泛型参数 {@code <U>} 决定，默认为 {@link Long}。
     * 由 Spring Data MongoDB Auditing 通过 {@code AuditorAware<U>} Bean 自动填充。
     * </p>
     */
    @LastModifiedBy
    @Field("updatedBy")
    private U updatedBy;
}
