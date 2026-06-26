# java-sdk-doc-skill-creator 设计规格

## 概览

| 项 | 决策 |
|----|------|
| 名称 | `java-sdk-doc-skill-creator` |
| 触发条件 | 用户要求从 Java 源码生成 SDK 文档，或提到"生成文档"、"javadoc 转 markdown"等 |
| 输入 | Java 源码路径（文件或目录） |
| 输出 | 中文 Markdown，按 `包名/类名.md` 组织（包名为单层目录，不拆多级子目录）。默认输出到输入路径同级目录下的 `sdk-docs/`，用户可指定输出目录 |
| 提取范围 | 仅 public API + Lombok 生成方法 |
| 浓缩策略 | 深度浓缩，英→中，最大化信息密度 |
| 处理方式 | 两轮遍历（第一轮建索引，第二轮生成文档） |

## 执行流程

```
输入路径
  │
  ├─ 是文件？→ 直接处理单个文件
  │
  └─ 是目录？→ 递归扫描 .java 文件
        │
        ▼
  第一轮：构建类型索引
  - 扫描所有类/接口/枚举声明
  - 记录全限定名、访问修饰符、泛型参数
  - 记录 Lombok 注解
  - 建立符号表（类名 → 文件路径映射）
        │
        ▼
  第二轮：生成文档
  - 基于索引解析类型引用
  - 提取 public API（含 Lombok 生成的方法）
  - 深度浓缩 Javadoc 描述为中文
  - 生成用法示例
  - 输出 Markdown 文件
        │
        ▼
  输出目录
  └─ com.example.sdk.service/
       ├─ UserService.md
       └─ OrderService.md
```

## 提取规则

### 提取范围

仅提取 `public` 修饰的：

| 元素 | 提取内容 |
|------|---------|
| 类/接口/枚举 | 类名、泛型参数、父类、实现接口、Javadoc 描述 |
| 方法 | 方法签名（含泛型）、返回值类型、参数名+类型、异常、Javadoc |
| 字段 | 字段名、类型、Javadoc |
| 常量 | 常量名、类型、值 |

忽略：`private`、`package-private`、`protected` 成员，以及 `@Deprecated` 标记的 API。

### Lombok 注解识别与生成说明

| Lombok 注解 | 生成的调用说明 |
|-------------|--------------|
| `@Data` | 生成 getter/setter（所有字段）、equals/hashCode、toString、RequiredArgsConstructor |
| `@Getter` | 生成 getter（标注在类上=所有字段，标注在字段上=该字段） |
| `@Setter` | 生成 setter（同上规则） |
| `@Builder` | 生成 Builder 模式：`ClassName.builder().field(value).build()` |
| `@AllArgsConstructor` | 生成全参构造器 |
| `@NoArgsConstructor` | 生成无参构造器 |
| `@RequiredArgsConstructor` | 生成 final 字段/@NonNull 字段构造器 |
| `@Value` | 同 @Data 但不可变（全字段 private final，无 setter） |
| `@With` | 生成 withXxx() 方法，返回新实例 |
| `@SuperBuilder` | 同 @Builder 但支持父类字段 |

**处理策略**：
1. 识别类级别和字段级别的 Lombok 注解
2. 基于注解推断生成的方法，在文档中标注 `[Lombok 生成]`
3. 为 `@Builder` / `@SuperBuilder` 生成完整的 builder 链式调用示例
4. `@Data` 不逐个列出 getter/setter，而是用一行概括："所有字段均有 getter/setter"

### 类型引用解析

利用第一轮构建的符号表：
- 返回值/参数类型如果是项目内的类 → 生成相对链接 `[ClassName](./ClassName.md)`
- 如果是 Java 标准库类型 → 保留简单类名
- 如果是泛型 → 保留泛型参数，递归解析泛型实参

## 输出模板

### 类文档模板

```markdown
# ClassName

> 一句话浓缩描述（从 Javadoc 提炼）

- **包**: com.example.sdk.service
- **父类**: [ParentClass](./ParentClass.md)
- **实现**: [InterfaceA](./InterfaceA.md), Serializable

## 构造

| 签名 | 说明 |
|------|------|
| `ClassName()` | 无参构造 [Lombok 生成] |
| `ClassName(String name, int age)` | 全参构造 [Lombok 生成] |

## 方法

### methodName

```java
ResultType methodName(ParamType1 param1, ParamType2 param2)
```

> 浓缩后的中文描述

**参数**:
- `param1` (ParamType1) — 参数说明
- `param2` (ParamType2) — 参数说明

**返回**: ResultType — 返回值说明

**异常**:
- `SomeException` — 异常说明

**示例**:
```java
ClassName obj = new ClassName();
ResultType result = obj.methodName(param1, param2);
```

---

### 字段

| 字段 | 类型 | 说明 |
|------|------|------|
| `name` | String | 名称 |
| `age` | int | 年龄 [Lombok: getter/setter] |

### Builder 模式 [Lombok 生成]

```java
ClassName obj = ClassName.builder()
    .name("张三")
    .age(25)
    .build();
```

### 枚举值（仅枚举类）

| 值 | 说明 |
|----|------|
| `VALUE_A` | 值A说明 |
| `VALUE_B` | 值B说明 |
```

### 关键格式规则

1. **方法按使用频率排序** — 常用方法在前，辅助方法在后（基于 Javadoc 中的描述推断）
2. **示例代码必须可运行** — 包含完整的对象创建和方法调用链
3. **类型引用用链接** — 项目内类型 `[ClassName](./ClassName.md)`，外部类型用反引号
4. **Builder 示例独占一节** — 不混在方法列表中，因为 AI agent 最常需要
5. **表格优先** — 构造、字段、枚举值用表格，信息密度高
6. **方法用三级标题** — 每个方法独立一节，便于 AI agent 定位

## 浓缩规则

### Javadoc → 中文精炼表达

| 原始 Javadoc 模式 | 浓缩后 | 策略 |
|---|---|---|
| "This method is used to get the user name" | "获取用户名" | 去除"This method is used to"，动词直出 |
| "Returns the ..." | "返回..." | 保留核心动作 |
| "Sets the value of ..." | "设置..." | 去除模板句式 |
| "This class represents a ..." | "表示..." | 去除"This class represents" |
| "@param name the name of the user" | "`name` — 用户名" | 去除"the name of the" |
| "@return the user object" | "用户对象" | 去除"the ... object" |
| "@throws IOException if an I/O error occurs" | "IO 异常" | 去除"if ... occurs" |
| 多句描述同一件事 | 合并为一句 | 去重复语义 |
| "Please note that ..." | 直接写注意事项内容 | 去除"Please note that" |
| "It is important to note that ..." | 同上 | 同上 |

### 核心原则

1. **动词开头** — 描述以动作开头，不用名词性从句
2. **去掉元话语** — 删除所有"请注意"、"需要注意的是"、"这个方法用于"等
3. **合并同义句** — 多句表达同一含义时合并
4. **英→中** — 所有描述翻译为中文，但类名/方法名/参数名保留英文
5. **术语保留英文** — 如"Builder 模式"、"DTO"、"DAO"等业界通用术语不翻译

## 省略规则

### 省略以下内容

| 类别 | 示例 | 原因 |
|------|------|------|
| `@author` | `@author John Doe` | AI agent 不关心作者 |
| `@since` | `@since 1.0` | 版本信息无调用价值 |
| `@version` | `@version 2.1` | 同上 |
| `@see` 引用 | `@see #otherMethod` | 已通过类型链接覆盖 |
| `@deprecated` 标记的 API | 整个成员 | 已废弃，不应引导 AI 使用 |
| `Object` 继承方法 | `equals`, `hashCode`, `toString`, `getClass` | 除非被显式覆写且有 Javadoc |
| 重复的 `@param` 与描述 | Javadoc 说"设置名称"，`@param name` 又说"the name" | 合并，不重复 |
| 内部实现细节 | "Uses HashMap internally" | AI 只需知道 API 契约 |
| 空描述的 public 成员 | 无 Javadoc 且无继承描述的成员 | 保留签名但不生成描述段 |

### 保留以下内容

| 类别 | 原因 |
|------|------|
| `@throws` / `@exception` | AI 需要知道异常条件来写健壮代码 |
| `@see` 指向项目内其他类 | 转为跨类链接 |
| 显式覆写的 `Object` 方法 | 有自定义行为，需要文档 |
| 空描述成员的签名 | 仍需知道方法存在，只是不展开描述 |

## 文件结构

```
.trae/skills/java-sdk-doc-skill-creator/
  └─ SKILL.md    # 包含执行步骤、提取规则、输出模板、浓缩规则、省略规则
```