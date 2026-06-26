# jsf-parent

> JSF 框架父 POM，为业务项目提供统一构建配置和多环境 Profile 管理

- **GAV**: `io.soil:jsf-parent:0.0.1`
- **Packaging**: pom
- **继承链**: 业务项目 → `jsf-parent` → `jsf-dependencies`（聚合 BOM）

## 引入方式

```xml
<parent>
  <groupId>io.soil</groupId>
  <artifactId>jsf-parent</artifactId>
  <version>0.0.1</version>
</parent>
```

继承后**无需**再导入 `jsf-dependencies` BOM，也**无需**重复声明 Java 版本与编码。

## 多环境 Profile

通过 `-P <profileId>` 激活对应环境，设置 `env` 属性供资源过滤与 Docker 镜像 Tag 使用。

| Profile ID | env 值 | docker.push.registry | 用途 |
|------------|--------|---------------------|------|
| `d-local` | local | — | 本地开发 |
| `d-backend` | backend | — | 后端开发环境 |
| `d-dev` | dev | — | 开发环境 |
| `d-uat` | uat | `crpi-gq7wp4xitzhccl7a.cn-guangzhou.personal.cr.aliyuncs.com` | 用户验收测试 |
| `d-prod` | prod | `crpi-gq7wp4xitzhccl7a.cn-guangzhou.personal.cr.aliyuncs.com` | 生产环境 |

### 构建命令示例

```bash
# 本地开发构建
mvn clean package -P d-local

# 开发环境 + Docker 镜像构建
mvn clean package -P d-dev,pd-docker

# 生产环境打包
mvn clean package -P d-prod
```

## Docker 构建 Profile（pd-docker）

激活 `pd-docker` 后启用 `io.fabric8:docker-maven-plugin` 进行镜像构建。

| 配置项 | 值 |
|--------|-----|
| 基础镜像 (`docker.from`) | `eclipse-temurin:21.0.4_7-jre-alpine` |
| 镜像名 | `soil/${project.artifactId}` |
| Tag 规则 | `${env}-${project.version}` 与 `${env}-latest` |
| 工作目录 | `/programs` |
| 入口命令 | `java -Dspring.profiles.active=${env} -jar ${project.build.finalName}.jar` |
| 镜像拉取策略 | `IfNotPresent` |
| 重启策略 | `on-failure` |
| 网络模式 | `bridge` |

### 镜像 Tag 示例

当 `-P d-dev,pd-docker` 且版本为 `0.0.1` 时：
- `dev-0.0.1`
- `dev-latest`

## Agent 使用要点

1. **环境 Profile 必须激活**：构建时通过 `-P` 指定环境，否则 `${env}` 无值会导致资源过滤异常
2. **Docker 构建需组合激活**：`pd-docker` 需与环境 Profile 同时使用，如 `-P d-dev,pd-docker`
3. **`docker.push.registry` 仅 uat/prod/live 配置**：local/backend/dev 不推送镜像
4. **继承后不要重复配置**：Java 21、UTF-8、Lombok、源码打包均已在父 POM 链中配置
5. **Spring Profile 联动**：Docker 入口通过 `-Dspring.profiles.active=${env}` 将 Maven 环境传递给 Spring，确保 `application-${env}.yml` 配置文件被加载

## 完整启动项目 POM 模板

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
  <modelVersion>4.0.0</modelVersion>

  <parent>
    <groupId>io.soil</groupId>
    <artifactId>jsf-parent</artifactId>
    <version>0.0.1</version>
  </parent>

  <artifactId>my-service</artifactId>
  <packaging>jar</packaging>

  <dependencies>
    <dependency>
      <groupId>io.soil</groupId>
      <artifactId>jsf-util</artifactId>
    </dependency>
    <dependency>
      <groupId>io.soil</groupId>
      <artifactId>jsf-waf</artifactId>
    </dependency>
  </dependencies>
</project>
```