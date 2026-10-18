# jsf-tools

JSF 通用工具库聚合，提供与具体业务无关的基础能力：常用工具类、统一 DTO、Web 服务框架、分布式 ID、邮件发送、单元测试支撑等。按需独立引入，互不耦合。

## 模块结构

| 模块 | 说明 |
|------|------|
| [jsf-util](./jsf-util/README.md) | 通用工具集：日期时间、JSON、集合、树、IP、分页、Spring 上下文、异常基类 |
| [jsf-pojo](./jsf-pojo/README.md) | 公共 POJO：统一分页响应体 `PagedResult` |
| [jsf-wsf](./jsf-wsf/README.md) | Web 服务框架：全局异常处理、Jackson 序列化配置、健康探针、客户端 IP，引入即生效 |
| [jsf-leaf-id](./jsf-leaf-id/README.md) | 基于 Leaf 的分布式主键生成（Snowflake） |
| [jsf-mail-sender](./jsf-mail-sender/README.md) | 邮件发送封装 |
| [jsf-unit-test](./jsf-unit-test/README.md) | 单元测试支撑：YAML 属性源工厂等 |

## 依赖版本管理

前置条件：通过 [jsf-bom](../jsf-bom/README.md) 管理依赖版本（继承 `jsf-parent` 或导入 `jsf-dependencies` BOM）。JSF 工具库的版本统一由 [jsf-tools-dependencies](../jsf-bom/jsf-tools-dependencies/README.md) 声明，声明依赖时省略 `<version>`。

```xml
<dependency>
    <groupId>io.soil.jsf</groupId>
    <artifactId>jsf-util</artifactId>
</dependency>
```

## 自动配置约定

`jsf-util`、`jsf-wsf`、`jsf-leaf-id`、`jsf-mail-sender` 通过 `AutoConfiguration.imports` 注册自动配置类，引入依赖即完成组件扫描与 Bean 装配；`jsf-pojo`、`jsf-unit-test` 为纯类库，无自动配置。
