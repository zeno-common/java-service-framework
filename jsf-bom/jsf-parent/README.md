# jsf-parent

JSF 框架的父 POM 模块，为业务项目提供统一的构建配置和多环境 Profile 管理。

## 功能说明

本模块继承自 `jsf-dependencies`，在依赖版本管理的基础上，提供以下能力：

- 多环境 Profile 配置（local / backend / dev / uat / prod / live）
- Docker 构建配置（通过 `pd-docker` Profile 激活）
- 统一的 Maven 仓库分发配置

## 多环境 Profile

| Profile ID | 环境 | 说明 |
|------------|------|------|
| `d-local` | local | 本地开发环境 |
| `d-backend` | backend | 后端开发环境 |
| `d-dev` | dev | 开发环境 |
| `d-uat` | uat | 用户验收测试环境 |
| `d-prod` | prod | 生产环境 |
| `d-live` | live | 线上环境 |

## Docker 构建

通过激活 `pd-docker` Profile 启用 Docker 镜像构建：

```bash
mvn package -P d-dev,pd-docker
```

Docker 构建配置：

- **基础镜像**: `eclipse-temurin:21.0.4_7-jre-alpine`
- **镜像命名**: `soil/${project.artifactId}`
- **Tag 规则**: `${env}-${project.version}` 和 `${env}-latest`
- **入口命令**: `java -Dspring.profiles.active=${env} -jar ${project.build.finalName}.jar`

## 使用方式

```xml
<parent>
    <groupId>io.soil</groupId>
    <artifactId>jsf-parent</artifactId>
    <version>0.0.1</version>
</parent>
```