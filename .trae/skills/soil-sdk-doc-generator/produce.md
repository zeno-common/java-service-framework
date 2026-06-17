# 产出阶段（步骤 4-6）

## 4. 生成 Markdown 文档

格式严格遵循 [template.md](template.md) 中的模板。

核心要求：
- 使用结构化表格，字段标签明确，便于 AI 程序化解析
- 无 Javadoc 的成员标注 `*(No description provided)*`
- 同项目内的类型引用生成相对 Markdown 链接
- 所有文件 UTF-8 编码

### 省略规则

| 字段/段落 | 省略条件 |
|-----------|----------|
| `extends` / `implements` | 无继承或仅继承 `Object`，无实现接口 |
| 注解行 | 类/方法无注解 |
| `@Deprecated` | 未被 `@Deprecated` 标记 |
| `Constants` 段落 | 无 `public static final` 字段 |
| `Enum Values` 段落 | 非枚举类型 |
| `throws` | 方法无 `@throws` 声明 |
| 参数表 | 方法无参数 |

## 5. 输出文件

目录结构：

```
docs/sdk/
├── README.md
├── <module-name>/
│   └── <package-path>/
│       └── <ClassName>.md
```

增量更新策略：

| 文件变更 | 操作 |
|----------|------|
| 新增/修改 | 覆盖对应 `.md` |
| 删除 | 删除对应 `.md`（若存在） |

每次生成后更新 `docs/sdk/README.md` 索引：

```markdown
# SDK Documentation Index

| Module | Package | Classes | Last Updated |
|--------|---------|---------|--------------|
| `ruoyi-common-core` | `com.ruoyi.common.core` | 15 | 2026-06-17 |
```

## 6. 关键规则

1. **仅公开 API** — 聚焦 `public` 类/方法/字段，跳过 `private`/package-private
2. **注解感知** — 捕获 Spring MVC、Security、Transaction、Validation、Lombok、Deprecated 注解
3. **废弃标记** — `@Deprecated` 需标注 `since`、`forRemoval`、替代方案
4. **无重复写入** — 先检查文件是否存在，增量模式仅覆盖变更文件
5. **空 Javadoc 保留结构** — 仍输出签名和注解，描述标为 `*(No description provided)*`

## 完成汇总

| 指标 | 数量 |
|------|------|
| 处理的模块 | N |
| 已文档化的类 | N |
| 已文档化的方法 | N |
| 新建文件 | N |
| 更新文件 | N |
| 删除文件 | N |