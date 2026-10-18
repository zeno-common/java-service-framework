# MongoDocIdCallback

> Snowflake ID 自动生成回调，在 Document 转换前（BeforeConvert 阶段）填充 ID。

- **包**: `io.soil.jsf.mongodb.doc`
- **实现**: `BeforeConvertCallback<BaseMongoDoc>`

替代旧版 `@PrePersist` 机制。仅当 `id` 为 `null` 时生成新 ID，避免覆盖已有 ID。

## 方法

### onBeforeConvert

`BaseMongoDoc onBeforeConvert(BaseMongoDoc entity, String collection)`

> 若 `entity.getId() == null`，调用 `LeafId.nextId()` 生成并回填。

**参数**:
- `entity` (BaseMongoDoc) — 待转换的文档实体
- `collection` (String) — 目标集合名

**返回**: BaseMongoDoc — 回填 ID 后的实体

- **相关**: [BaseMongoDoc](#BaseMongoDoc)、`io.soil.jsf.leaf.gen.LeafId`
