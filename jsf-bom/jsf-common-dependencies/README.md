# jsf-common-dependencies

JSF 通用模块依赖管理，统一管理 `jsf-common` 下核心模块的版本。

## 功能说明

管理 JSF 框架基础通用模块的依赖版本，这些模块是框架的核心基础设施。

## 管理的依赖

| 依赖 | 说明 |
|------|------|
| jsf-pojo | 基础 POJO 定义，包含通用实体、DTO、VO 基类 |
| jsf-util | 工具类库，提供 JSON、日期、字符串等工具方法 |
| jsf-waf | Web 应用防火墙，提供安全过滤与防护能力 |

## 使用方式

```xml
<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>io.soil</groupId>
            <artifactId>jsf-common-dependencies</artifactId>
            <version>0.0.1</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>
```