# BaseMongoDoc<U>

> MongoDB 文档抽象基类，封装 ID 生成与审计公共字段。

- **包**: `io.soil.jsf.mongodb.doc`
- **修饰**: `abstract`，泛型 `U` 为审计人字段类型（默认 `Long`）
- **Lombok**: `@Getter` `@Setter` `@NoArgsConstructor` `@AllArgsConstructor`

子类继承后自动获得 `id`（Snowflake）、审计字段与 JSON 序列化能力（Long ID 转 String 防精度丢失）。`version`（乐观锁）与 `deleted`（逻辑删除）不在此基类，由子类按需添加。

## 字段

| 字段 | 类型 | 说明 |
|------|------|------|
| `id` | Long | 主键，Snowflake 算法生成（`MongoDocIdCallback` 保存前回填），JSON 序列化为 String |
| `createdAt` | OffsetDateTime | 创建时间（`@CreatedDate` 自动填充），映射 `createdAt` |
| `updatedAt` | OffsetDateTime | 更新时间（`@LastModifiedDate` 自动填充），映射 `updatedAt` |
| `createdBy` | U | 创建人（`@CreatedBy`，由 `AuditorAware<U>` 填充），映射 `createdBy` |
| `updatedBy` | U | 更新人（`@LastModifiedBy`，由 `AuditorAware<U>` 填充），映射 `updatedBy` |

> 所有字段均有 getter/setter [Lombok 生成]。

## 示例

```java
// 审计人为 Long（默认）
@Document(collection = "users")
public class UserDoc extends BaseMongoDoc<Long> {
    @Field("name")
    private String name;
}

// 审计人为 String
@Document(collection = "orders")
public class OrderDoc extends BaseMongoDoc<String> {
    @Field("orderNo")
    private String orderNo;
}
```

- **相关**: [MongoDocIdCallback](#MongoDocIdCallback)（ID 生成回调）、`io.soil.jsf.leaf.gen.LeafId`
