---
name: "jsf-util-doc"
description: "Use when coding with jsf-util SDK, need API reference for date-time, JSON, collection, tree, IP, paging, Spring context utilities and the exception base hierarchy."
---

# jsf-util SDK 参考

JSF 通用工具集，沉淀与业务无关的基础能力：日期时间、JSON、集合、树模型、IP、分页、Spring 上下文、异常基类等。引入即通过 `JsfCommonAutoConfig` 完成组件扫描。

## BOM 引入示例

推荐先导入 `jsf-dependencies` 聚合 BOM 统一管理版本，再声明本模块依赖（**省略 `<version>`**）：

```xml
<!-- 1. 导入 BOM -->
<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>io.soil.jsf</groupId>
            <artifactId>jsf-dependencies</artifactId>
            <version>jsf.0.2.1</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>

<!-- 2. 声明依赖（版本由 BOM 管理） -->
<dependencies>
    <dependency>
        <groupId>io.soil.jsf</groupId>
        <artifactId>jsf-util</artifactId>
    </dependency>
</dependencies>
```

> 若为启动项目，也可直接继承 `io.soil.jsf:jsf-parent:jsf.0.2.1` 作为 `<parent>`，自动获得全部依赖版本管理与构建配置。

## 依赖引入

java maven 工程的依赖引入参考：'[jsf-bom/README.md](../../jsf-bom/README.md)'

| ArtifactId | 说明 |
|-----------|------|
| `jsf-util` | 通用工具集与异常体系 |

## 类索引

| 类 | 包 | 说明 | 文档 |
|----|-----|------|------|
| `CollectionUtil` | `io.soil.jsf.common.collection` | 集合转 Map / mapList（支持额外参数） | [CollectionUtil](references/io.soil.jsf.common.collection/CollectionUtil.md) |
| `FunctionWithParam` | `io.soil.jsf.common.collection` | 带 1 个额外参数的函数接口 | [FunctionWithParam](references/io.soil.jsf.common.collection/FunctionWithParam.md) |
| `FunctionWith2Param` | `io.soil.jsf.common.collection` | 带 2 个额外参数的函数接口 | [FunctionWith2Param](references/io.soil.jsf.common.collection/FunctionWith2Param.md) |
| `FunctionWith3Param` | `io.soil.jsf.common.collection` | 带 3 个额外参数的函数接口 | [FunctionWith3Param](references/io.soil.jsf.common.collection/FunctionWith3Param.md) |
| `EnumSample` | `io.soil.jsf.common.constant` | 枚举定义样例 | [EnumSample](references/io.soil.jsf.common.constant/EnumSample.md) |
| `DateTimeConst` | `io.soil.jsf.common.date` | 日期时间常量 | [DateTimeConst](references/io.soil.jsf.common.date/DateTimeConst.md) |
| `OffsetDateTimeUtil` | `io.soil.jsf.common.date` | 日期时间格式化与转换 | [OffsetDateTimeUtil](references/io.soil.jsf.common.date/OffsetDateTimeUtil.md) |
| `BaseException` | `io.soil.jsf.common.exception` | 业务异常抽象基类 | [BaseException](references/io.soil.jsf.common.exception/BaseException.md) |
| `BizException` | `io.soil.jsf.common.exception` | 业务异常 | [BizException](references/io.soil.jsf.common.exception/BizException.md) |
| `SysException` | `io.soil.jsf.common.exception` | 系统异常 | [SysException](references/io.soil.jsf.common.exception/SysException.md) |
| `ParamException` | `io.soil.jsf.common.exception` | 参数校验异常 | [ParamException](references/io.soil.jsf.common.exception/ParamException.md) |
| `Error` | `io.soil.jsf.common.exception` | 错误码/描述定义接口 | [Error](references/io.soil.jsf.common.exception/Error.md) |
| `ExceptionType` | `io.soil.jsf.common.exception` | 异常类型枚举 | [ExceptionType](references/io.soil.jsf.common.exception/ExceptionType.md) |
| `UnknownEnumException` | `io.soil.jsf.common.exception` | 未知枚举状态异常 | [UnknownEnumException](references/io.soil.jsf.common.exception/UnknownEnumException.md) |
| `Node` | `io.soil.jsf.common.tree` | 通用树节点 | [Node](references/io.soil.jsf.common.tree/Node.md) |
| `TreeUtils` | `io.soil.jsf.common.tree` | list/tree/map 转换 | [TreeUtils](references/io.soil.jsf.common.tree/TreeUtils.md) |
| `JsfCommonAutoConfig` | `io.soil.jsf.util.config` | 组件扫描自动配置 | [JsfCommonAutoConfig](references/io.soil.jsf.util.config/JsfCommonAutoConfig.md) |
| `IpUtil` | `io.soil.jsf.util.ip` | 本地连接 IP 工具 | [IpUtil](references/io.soil.jsf.util.ip/IpUtil.md) |
| `JsfPagingUtil` | `io.soil.jsf.util.jdbc` | PageHelper URL 参数分页 | [JsfPagingUtil](references/io.soil.jsf.util.jdbc/JsfPagingUtil.md) |
| `JsfUrlParams` | `io.soil.jsf.util.jdbc` | URL 参数提取 | [JsfUrlParams](references/io.soil.jsf.util.jdbc/JsfUrlParams.md) |
| `JsfUrlParamsPage` | `io.soil.jsf.util.jdbc` | MyBatis-Flex URL 分页对象 | [JsfUrlParamsPage](references/io.soil.jsf.util.jdbc/JsfUrlParamsPage.md) |
| `JsfUrlParamConst` | `io.soil.jsf.util.jdbc` | URL 参数名常量 | [JsfUrlParamConst](references/io.soil.jsf.util.jdbc/JsfUrlParamConst.md) |
| `JsonMapper` | `io.soil.jsf.util.json` | Jackson 序列化封装 | [JsonMapper](references/io.soil.jsf.util.json/JsonMapper.md) |
| `ApplicationContextUtil` | `io.soil.jsf.util.spring` | Spring 上下文取 Bean | [ApplicationContextUtil](references/io.soil.jsf.util.spring/ApplicationContextUtil.md) |
