# jsf-unit-test

JSF 单元测试支撑模块，提供测试场景下的辅助工具类。以 `test` 作用域引入，不进入生产打包。

## 功能特性

- **`YamlPropertySourceFactory`** — 实现 Spring `PropertySourceFactory`，支持通过 `@PropertySource` 加载 YAML 配置文件（Spring 原生仅支持 properties）

## 引入依赖

前置条件：通过 [jsf-bom](../../jsf-bom/README.md) 管理依赖版本。

```xml
<dependency>
    <groupId>io.soil.jsf</groupId>
    <artifactId>jsf-unit-test</artifactId>
    <scope>test</scope>
</dependency>
```

## 使用示例

在测试类中加载独立的 YAML 配置文件：

```java
@SpringBootTest
@PropertySource(value = "classpath:test-config.yml", factory = YamlPropertySourceFactory.class)
class MyServiceTest {

    @Value("${my.biz.threshold}")
    private int threshold;

    // ...
}
```

> `YamlPropertySourceFactory` 内部使用 `YamlPropertiesFactoryBean` 将 YAML 解析为 `Properties`，再以资源文件名作为 `PropertySource` 名称注册到环境。

## 项目结构

```
io.soil.jsf.test/
└── YamlPropertySourceFactory.java   # YAML 属性源工厂
```
