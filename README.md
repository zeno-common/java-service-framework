# java-service-framework (JSF)

Java 服务端开发脚手架，提供统一依赖版本管理（BOM）、通用工具库与可插拔的公共组件，用于快速搭建微服务或单体应用。

- **GroupId**：`io.soil.jsf`
- **当前版本**：`jsf.0.2.1`
- **基础环境**：Java 25 / Spring Boot 3.5.x / Spring Cloud 2025.0.x

## 模块总览

项目由 3 个顶层模块聚合，共 16 个 Maven 模块：

| 顶层模块 | 定位 | 子模块 |
|----------|------|--------|
| [jsf-bom](./jsf-bom/README.md) | 依赖版本管理与构建配置 | jsf-dependencies、jsf-parent、jsf-spring-dependencies、jsf-alibaba-dependencies、jsf-tools-dependencies、jsf-common-dependencies、jsf-jdbc-dependencies、jsf-redis-dependencies |
| [jsf-tools](./jsf-tools/README.md) | 通用工具库 | jsf-util、jsf-pojo、jsf-wsf、jsf-leaf-id、jsf-mail-sender、jsf-unit-test |
| [jsf-common](./jsf-common/README.md) | 组件化功能模块 | jsf-mongodb、jsf-mq（jsf-mq-core、jsf-mq-mongodb） |

## 快速开始

### 1. 引入 BOM 管理依赖版本

推荐继承 `jsf-parent`，自动获得全部依赖版本管理、Java 25 配置、多环境 Profile 与 Docker 构建能力：

```xml
<parent>
    <groupId>io.soil.jsf</groupId>
    <artifactId>jsf-parent</artifactId>
    <version>jsf.0.2.1</version>
</parent>
```

非启动项目可改为导入 `jsf-dependencies` BOM，详见 [jsf-bom/README.md](./jsf-bom/README.md)。

### 2. 声明功能依赖（无需写版本号）

```xml
<dependencies>
    <!-- 通用工具：日期/JSON/集合/树/分页/异常基类等 -->
    <dependency>
        <groupId>io.soil.jsf</groupId>
        <artifactId>jsf-util</artifactId>
    </dependency>
    <!-- Web 服务框架：全局异常、Jackson 配置、健康探针，引入即生效 -->
    <dependency>
        <groupId>io.soil.jsf</groupId>
        <artifactId>jsf-wsf</artifactId>
    </dependency>
</dependencies>
```

> 引入 BOM/父 POM 后，声明 JSF 依赖时**必须省略 `<version>`**，版本由 BOM 统一管理。

### 3. 构建

```bash
# 本地打包
mvn clean package -P d-local

# 生产环境 + Docker 镜像构建
mvn clean package -P d-prod,pd-docker
```

多环境 Profile（`d-local` / `d-dev` / `d-staing` / `d-prod`，另加 `pd-docker` 启用镜像构建）与 Docker 配置详见 [jsf-bom/README.md](./jsf-bom/README.md)。

## 目录结构

```
java-service-framework/
├── jsf-bom/       # BOM：依赖版本、父 POM、多环境 Profile
├── jsf-tools/     # 工具库：util / pojo / wsf / leaf-id / mail-sender / unit-test
├── jsf-common/    # 组件：mongodb / mq（core + mongodb）
└── docs/scripts/  # Agent Skills 安装脚本
```

## Agent Skills

本项目为 AI 编码助手（Qoder / TRAE / CodeBuddy 等）配置了 **Agent Skills**。所有 skill 统一安装到 `.agents/skills`，并通过目录联结（junction）映射到各 IDE 的 skills 目录，实现一处安装、多端共享。

### 正在使用的 Skills

以下 skill 均来自 [`zeno-common/soil-coding-skills`](https://github.com/zeno-common/soil-coding-skills)，版本锁定见 [skills-lock.json](./skills-lock.json)：

| Skill | 用途 | 触发场景 |
|-------|------|----------|
| `java-coding-guidelines` | 强制执行 Java 编码规范 | 编写/审查 Java 代码、修复风格问题、提及编码约定时 |
| `java-sdk-doc-skill-creator` | 将 Java 源码的 Javadoc 转换为面向编码助手的 SDK skill | 生成 SDK 文档、创建/更新 doc skill、javadoc-to-skill 转换时 |

### 安装与映射

安装脚本为 [docs/scripts/install-agent-skills.ps1](./docs/scripts/install-agent-skills.ps1)，通过 `npx skills` 拉取指定 skill 并创建 IDE 目录联结。

```powershell
# 安装单个 skill
.\docs\scripts\install-agent-skills.ps1 -Source zeno-common/soil-coding-skills -Skill java-sdk-doc-skill-creator

# 安装仓库全部 skill
.\docs\scripts\install-agent-skills.ps1 -Source zeno-common/soil-coding-skills -All

# 仅补建 Qoder / TRAE / CodeBuddy 目录联结，不安装新 skill
.\docs\scripts\install-agent-skills.ps1 -LinkOnly
```


> **前置条件**：已安装 Node.js 且 `npx` 可用。目录联结（`mklink /J`）在普通权限终端即可创建，无需管理员（与符号链接 `mklink /D` 不同）。
>
> **执行策略**：若直接运行 `.ps1` 被系统拦截，请以管理员身份执行 `Set-ExecutionPolicy -Scope CurrentUser RemoteSigned`，或使用 `powershell -ExecutionPolicy Bypass -File docs\scripts\install-agent-skills.ps1 ...`。
>
> **编码要求**：本 `.ps1` 脚本含中文内容，必须以 **UTF-8 with BOM** 保存，否则在 Windows PowerShell 5.1 下会因按 GBK 解码而解析失败。

## 相关文档

- [jsf-bom](./jsf-bom/README.md) — 依赖版本、核心版本速查、多环境 Profile、Docker 构建
- [jsf-tools](./jsf-tools/README.md) — 各工具库使用说明
- [jsf-common](./jsf-common/README.md) — MongoDB、消息队列组件说明
