# jsf-bom 构建 Profile 与插件

> 取自 `jsf-bom/jsf-parent/pom.xml` 与 `jsf-dependencies/pom.xml`（权威来源），版本 `jsf.0.1.2`。

## 多环境 Profile

通过 `-P <profileId>` 激活，设置 `env` 属性供 Spring `spring.profiles.active` 与镜像 tag 使用。

| Profile ID | env 值 | 用途 |
|-----------|--------|------|
| `d-local` | local | 本地开发 |
| `d-dev` | dev | 开发环境 |
| `d-staing` | staing | 预发/仿真 |
| `d-prod` | prod | 生产环境 |
| `pd-docker` | — | 启用 Docker 镜像构建，需与上面某个环境 Profile 组合 |

> 注：`jsf-bom/README.md` 中的 `d-backend`、`d-uat` 未在当前 `jsf-parent/pom.xml` 定义，以 pom 为准。

## Docker 构建（pd-docker）

```bash
# 开发环境 + Docker
mvn clean package -P d-dev,pd-docker

# 仅生产打包（不构建镜像）
mvn clean package -P d-prod
```

io.fabric8 docker-maven-plugin 关键配置：

| 配置项 | 值 |
|--------|-----|
| 基础镜像 | `eclipse-temurin:25-jre-alpine` |
| 镜像名 | `soil/${project.artifactId}` |
| Tag | `${project.version}-${env}` |
| workdir | `/programs` |
| 打包产物 | executable jar（assembly artifact → `/programs`） |
| 入口命令 | `java -Dfile.encoding=UTF-8 -Djdk.virtualThreadScheduler.maxPoolSize=256 -Xss512k -XX:+UseG1GC ... -jar ${project.build.finalName}.jar` |
| 容器 env | `TZ=Asia/Shanghai`、`LANG/LC_ALL=C.UTF-8` |
| 运行 | bridge 网络，restart=on-failure |
| docker.host | `http://workbench.soil:3000`（jsf-parent properties） |

> 入口含 GC 日志（`-Xlog:gc*:file=/programs/logs/gc.log`）、OOM heap dump（`/programs/heapdump.hprof`）、`--enable-native-access=ALL-UNNAMED`、`--sun-misc-unsafe-memory-access=allow` 等 JVM 参数。

## 构建插件版本（pluginManagement）

| 插件 | 版本 |
|------|------|
| maven-compiler-plugin | 3.15.0（含 Lombok 注解处理器） |
| maven-resources-plugin | 3.5.0（propertiesEncoding=UTF-8） |
| maven-jar-plugin | 3.5.0 |
| maven-source-plugin | 3.4.0（package 阶段产出源码 jar） |
| maven-shade-plugin | 3.3.0 |
| maven-dependency-plugin | 3.11.0 |
| maven-surefire-plugin | 2.22.2 |
| maven-checkstyle-plugin | 3.2.0 |
| spotbugs-maven-plugin | 4.8.6.2 |
| sonar-maven-plugin | 3.9.1.2184 |
| dokka-maven-plugin | 2.2.0（Java/Kotlin 文档） |
| smart-doc-maven-plugin | 3.1.2（API 文档） |
| docker-maven-plugin (io.fabric8) | 0.49.0 |

## 编译与编码约定

| 项 | 值 |
|----|-----|
| Java 版本 | 21（source/target/release） |
| 源码编码 | UTF-8 |
| 报告输出编码 | UTF-8 |
