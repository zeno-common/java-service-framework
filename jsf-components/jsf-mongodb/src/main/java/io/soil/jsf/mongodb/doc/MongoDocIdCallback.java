package io.soil.jsf.mongodb.doc;

import io.soil.jsf.leaf.gen.LeafId;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertCallback;

/**
 * Snowflake ID 自动生成回调
 * <p>
 * 在 Document 转换为 BSON 之前（BeforeConvert 阶段）自动为 {@link BaseMongoDoc} 子类生成 Snowflake ID。
 * 替代旧版 Spring Data MongoDB 的 {@code @PrePersist} 注解机制。
 * </p>
 * <p>
 * 仅当 {@code id} 为 {@code null} 时才生成新 ID，避免覆盖已有 ID。
 * </p>
 *
 * @see BaseMongoDoc
 * @see io.soil.jsf.leaf.gen.LeafId
 */
public class MongoDocIdCallback implements BeforeConvertCallback<BaseMongoDoc> {

    @Override
    public BaseMongoDoc onBeforeConvert(BaseMongoDoc entity, String collection) {
        if (entity.getId() == null) {
            entity.setId(LeafId.nextId());
        }
        return entity;
    }
}
