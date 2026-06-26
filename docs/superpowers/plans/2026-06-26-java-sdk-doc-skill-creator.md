# java-sdk-doc-skill-creator 实现计划

> **面向 AI 代理的工作者：** 必需子技能：使用 superpowers:subagent-driven-development（推荐）或 superpowers:executing-plans 逐任务实现此计划。步骤使用复选框（`- [ ]`）语法来跟踪进度。

**目标：** 创建一个 skill，基于 Java 源码的 Javadoc 注释生成对 AI coding agent 友好的中文 Markdown SDK 文档

**架构：** 单个 SKILL.md 文件，包含完整的执行流程、提取规则、输出模板、浓缩规则和省略规则。skill 采用两轮遍历策略：第一轮构建类型索引，第二轮基于索引生成文档。

**技术栈：** Skill 框架（SKILL.md + frontmatter）

---

## 文件结构

| 文件 | 职责 |
|------|------|
| 创建：`.trae/skills/java-sdk-doc-skill-creator/SKILL.md` | skill 主文件，包含全部执行指令 |

---

### 任务 1：创建 skill 目录

**文件：**
- 创建：`.trae/skills/java-sdk-doc-skill-creator/`

- [ ] **步骤 1：创建目录**

```powershell
New-Item -ItemType Directory -Path ".trae/skills/java-sdk-doc-skill-creator" -Force
```

- [ ] **步骤 2：确认目录存在**

```powershell
Test-Path ".trae/skills/java-sdk-doc-skill-creator"
```

预期：`True`

---

### 任务 2：编写 SKILL.md — frontmatter 与概述

**文件：**
- 创建：`.trae/skills/java-sdk-doc-skill-creator/SKILL.md`

- [ ] **步骤 1：创建 SKILL.md 文件，写入 frontmatter 和概述部分**

```markdown
---
name: "java-sdk-doc-skill-creator"
description: "基于 Java 源码 Javadoc 注释生成 AI coding 友好的中文 Markdown SDK 文档。当用户要求生成 SDK 文档、javadoc 转 markdown、或提到 Java API 文档生成时调用。"
---

# Java SDK 文档生成器

从 Java 源码的 Javadoc 注释生成对 AI coding agent 友好的中文 Markdown SDK 文档。

## 核心原则

1. **AI 友好优先** — 文档面向 AI coding agent，不是人类阅读，信息密度和可导航性最重要
2. **示例驱动** — 每个关键 API 必须附带可运行的代码示例
3. **深度浓缩** — 去除一切冗余修饰，用最精炼的中文表达
4. **类型可导航** — 项目内类型引用生成 Markdown 链接，AI agent 可跳转查阅

## 输入输出

**输入**：Java 源码路径（文件或目录）

**输出**：中文 Markdown 文件，按 `包名/类名.md` 组织
- 包名作为单层目录名（如 `com.example.service/`），不拆分为多级子目录
- 默认输出到输入路径同级目录下的 `sdk-docs/`，用户可指定输出目录

**提取范围**：仅 `public` 修饰的类、方法、字段、常量
```

- [ ] **步骤 2：确认文件已创建且 frontmatter 格式正确**

```powershell
Get-Content ".trae/skills/java-sdk-doc-skill-creator/SKILL.md" -Head 5
```

预期：第一行是 `---`

---

### 任务 3：编写 SKILL.md — 执行步骤

**文件：**
- 修改：`.trae/skills/java-sdk-doc-skill-creator/SKILL.md`（追加内容）

- [ ] **步骤 1：追加执行步骤部分**

在文件末尾追加以下内容：

```markdown

## 执行步骤

严格按以下顺序执行：

### 步骤 1：确定输入范围

| 输入类型 | 行为 |
|----------|------|
| 单个 `.java` 文件 | 仅处理该文件 |
| 目录 | 递归扫描目录下所有 `.java` 文件 |

忽略：`target/` 目录、测试文件（`src/test/`）、非 `.java` 文件。

### 步骤 2：第一轮遍历 — 构建类型索引

扫描所有目标 Java 文件，构建符号表：

1. **类声明** — 记录每个类/接口/枚举的：
   - 全限定名（如 `com.example.service.UserService`）
   - 类型（class / interface / enum / abstract class）
   - 泛型参数（如 `<T, R>`）
   - 访问修饰符（public / private 等）
   - 父类和实现接口
   - Lombok 注解（类级别和字段级别）

2. **符号表结构** — 建立 `简单类名 → 全限定名` 和 `全限定名 → 文件路径` 的映射

3. **输出索引** — 在内存中保存符号表，供第二轮使用

### 步骤 3：第二轮遍历 — 生成文档

对每个 Java 文件，基于符号表生成 Markdown 文档：

1. 解析类结构（类名、包名、泛型、继承、实现）
2. 提取 public 成员（方法、字段、常量）
3. 识别 Lombok 注解，推断生成的方法
4. 浓缩 Javadoc 描述为中文
5. 生成类型引用链接（项目内类型用相对链接）
6. 生成用法示例
7. 按输出模板格式化

### 步骤 4：输出文件

1. 确定输出目录（默认 `sdk-docs/` 或用户指定路径）
2. 按包名创建单层子目录（如 `com.example.service/`）
3. 每个类输出一个 `类名.md` 文件
4. 覆盖已存在的同名文件
```

- [ ] **步骤 2：确认内容追加成功**

```powershell
Select-String -Path ".trae/skills/java-sdk-doc-skill-creator/SKILL.md" -Pattern "执行步骤" -SimpleMatch
```

预期：匹配到内容

---

### 任务 4：编写 SKILL.md — 提取规则

**文件：**
- 修改：`.trae/skills/java-sdk-doc-skill-creator/SKILL.md`（追加内容）

- [ ] **步骤 1：追加提取规则部分**

```markdown

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

### Lombok 注解识别

| 注解 | 生成说明 |
|------|---------|
| `@Data` | 所有字段 getter/setter、equals/hashCode、toString、RequiredArgsConstructor。文档中用一行概括："所有字段均有 getter/setter"，不逐个列出 |
| `@Getter` | 生成 getter。类级别=所有字段，字段级别=该字段 |
| `@Setter` | 生成 setter。同上规则 |
| `@Builder` | 生成 Builder 模式，独占"Builder 模式"节，输出完整链式调用示例 |
| `@SuperBuilder` | 同 @Builder 但支持父类字段，示例中包含父类字段 |
| `@AllArgsConstructor` | 生成全参构造器，列入构造表 |
| `@NoArgsConstructor` | 生成无参构造器，列入构造表 |
| `@RequiredArgsConstructor` | 生成 final/@NonNull 字段构造器，列入构造表 |
| `@Value` | 同 @Data 但不可变（无 setter），标注"不可变类" |
| `@With` | 生成 withXxx() 方法，返回新实例 |

**处理策略**：
1. 识别类级别和字段级别的 Lombok 注解
2. 基于注解推断生成的方法，在文档中标注 `[Lombok 生成]`
3. 为 `@Builder` / `@SuperBuilder` 生成完整的 builder 链式调用示例
4. `@Data` 不逐个列出 getter/setter，用一行概括

### 类型引用解析

基于第一轮符号表：
- 返回值/参数类型是项目内的类 → 生成相对链接 `[ClassName](./ClassName.md)`
- Java 标准库类型 → 保留简单类名
- 泛型 → 保留泛型参数，递归解析泛型实参中的项目内类型
```

- [ ] **步骤 2：确认内容追加成功**

```powershell
Select-String -Path ".trae/skills/java-sdk-doc-skill-creator/SKILL.md" -Pattern "Lombok 注解识别" -SimpleMatch
```

预期：匹配到内容

---

### 任务 5：编写 SKILL.md — 输出模板

**文件：**
- 修改：`.trae/skills/java-sdk-doc-skill-creator/SKILL.md`（追加内容）

- [ ] **步骤 1：追加输出模板部分**

```markdown

## 输出模板

每个 Java 类生成一个 Markdown 文件，严格遵循以下结构。标注 `(omit)` 的段落，在无对应内容时整段省略。

```markdown-template
# ClassName

> 一句话浓缩中文描述（从 Javadoc 第一句提炼）

- **包**: com.example.service
- **父类**: [ParentClass](./ParentClass.md) (omit)
- **实现**: [InterfaceA](./InterfaceA.md), Serializable (omit)

## 构造 (omit)

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

**返回**: ResultType — 返回值说明 (omit)

**异常**: (omit)
- `SomeException` — 异常说明

**示例**:
```java
ClassName obj = new ClassName();
ResultType result = obj.methodName(param1, param2);
```

---

### 字段 (omit)

| 字段 | 类型 | 说明 |
|------|------|------|
| `name` | String | 名称 [Lombok: getter/setter] |

### Builder 模式 [Lombok 生成] (omit)

```java
ClassName obj = ClassName.builder()
    .name("示例值")
    .age(25)
    .build();
```

### 枚举值 (omit)

| 值 | 说明 |
|----|------|
| `VALUE_A` | 值A说明 |
```

### 格式规则

1. **方法排序** — 常用方法在前，辅助方法在后（基于 Javadoc 描述推断使用频率）
2. **示例必须可运行** — 包含完整的对象创建和方法调用链
3. **类型引用** — 项目内类型 `[ClassName](./ClassName.md)`，外部类型用反引号
4. **Builder 独占一节** — 不混在方法列表中
5. **表格优先** — 构造、字段、枚举值用表格
6. **方法用三级标题** — 每个方法独立一节
7. **空描述成员** — 保留签名，描述标为 `*(无描述)*`
```

- [ ] **步骤 2：确认内容追加成功**

```powershell
Select-String -Path ".trae/skills/java-sdk-doc-skill-creator/SKILL.md" -Pattern "输出模板" -SimpleMatch
```

预期：匹配到内容

---

### 任务 6：编写 SKILL.md — 浓缩规则

**文件：**
- 修改：`.trae/skills/java-sdk-doc-skill-creator/SKILL.md`（追加内容）

- [ ] **步骤 1：追加浓缩规则部分**

```markdown

## 浓缩规则

将 Javadoc 英文描述深度浓缩为中文精炼表达。

### 转换模式

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
2. **去掉元话语** — 删除"请注意"、"需要注意的是"、"这个方法用于"等
3. **合并同义句** — 多句表达同一含义时合并
4. **英→中** — 所有描述翻译为中文，但类名/方法名/参数名保留英文
5. **术语保留英文** — "Builder 模式"、"DTO"、"DAO"、"POJO"等业界通用术语不翻译
6. **代码标识符保留英文** — 类名、方法名、参数名、类型名不做翻译
```

- [ ] **步骤 2：确认内容追加成功**

```powershell
Select-String -Path ".trae/skills/java-sdk-doc-skill-creator/SKILL.md" -Pattern "浓缩规则" -SimpleMatch
```

预期：匹配到内容

---

### 任务 7：编写 SKILL.md — 省略规则

**文件：**
- 修改：`.trae/skills/java-sdk-doc-skill-creator/SKILL.md`（追加内容）

- [ ] **步骤 1：追加省略规则部分**

```markdown

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
| 空描述的 public 成员 | 无 Javadoc 且无继承描述 | 保留签名，描述标为 `*(无描述)*`，不展开描述段 |

### 保留以下内容

| 类别 | 原因 |
|------|------|
| `@throws` / `@exception` | AI 需要知道异常条件来写健壮代码 |
| `@see` 指向项目内其他类 | 转为跨类链接 |
| 显式覆写的 `Object` 方法 | 有自定义行为，需要文档 |
| 空描述成员的签名 | 仍需知道方法存在，只是不展开描述 |
```

- [ ] **步骤 2：确认内容追加成功**

```powershell
Select-String -Path ".trae/skills/java-sdk-doc-skill-creator/SKILL.md" -Pattern "省略规则" -SimpleMatch
```

预期：匹配到内容

---

### 任务 8：验证 SKILL.md 完整性

**文件：**
- 验证：`.trae/skills/java-sdk-doc-skill-creator/SKILL.md`

- [ ] **步骤 1：检查 frontmatter 格式**

```powershell
$content = Get-Content ".trae/skills/java-sdk-doc-skill-creator/SKILL.md" -Raw; $content -match '(?s)^---\s*\nname:.*?\ndescription:.*?\n---'
```

预期：`True`

- [ ] **步骤 2：检查所有必需章节存在**

```powershell
$content = Get-Content ".trae/skills/java-sdk-doc-skill-creator/SKILL.md" -Raw
$sections = @("执行步骤", "提取规则", "输出模板", "浓缩规则", "省略规则")
foreach ($s in $sections) {
    if ($content -notmatch [regex]::Escape($s)) {
        Write-Output "MISSING: $s"
    } else {
        Write-Output "OK: $s"
    }
}
```

预期：所有章节输出 `OK`

- [ ] **步骤 3：检查 Lombok 注解表完整**

```powershell
$content = Get-Content ".trae/skills/java-sdk-doc-skill-creator/SKILL.md" -Raw
$lombokAnnotations = @("@Data", "@Getter", "@Setter", "@Builder", "@SuperBuilder", "@AllArgsConstructor", "@NoArgsConstructor", "@RequiredArgsConstructor", "@Value", "@With")
foreach ($a in $lombokAnnotations) {
    if ($content -notmatch [regex]::Escape($a)) {
        Write-Output "MISSING: $a"
    } else {
        Write-Output "OK: $a"
    }
}
```

预期：所有注解输出 `OK`

- [ ] **步骤 4：检查文件总行数合理**

```powershell
(Get-Content ".trae/skills/java-sdk-doc-skill-creator/SKILL.md").Count
```

预期：100-250 行之间

---

### 任务 9：Commit

**文件：**
- 提交：`.trae/skills/java-sdk-doc-skill-creator/SKILL.md`

- [ ] **步骤 1：暂存文件**

```bash
git add .trae/skills/java-sdk-doc-skill-creator/SKILL.md
```

- [ ] **步骤 2：Commit**

```bash
git commit -m "feat: add java-sdk-doc-skill-creator skill"
```