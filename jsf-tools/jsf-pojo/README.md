# jsf-pojo

JSF 公共 POJO 模块，提供跨模块共享的标准响应/传输对象。纯类库，无自动配置、无额外运行时依赖。

## 功能特性

- **统一分页响应体 `PagedResult<T>`** — 封装查询总数与当前页数据列表，泛型适配任意元素类型

## 引入依赖

前置条件：通过 [jsf-bom](../../jsf-bom/README.md) 管理依赖版本。

```xml
<dependency>
    <groupId>io.soil.jsf</groupId>
    <artifactId>jsf-pojo</artifactId>
</dependency>
```

## 使用示例

`PagedResult` 构造器私有，统一通过静态工厂方法创建：

```java
// 正常分页结果
PagedResult<UserVO> result = PagedResult.of(total, list);

// 空结果（总数 0，列表空）
PagedResult<UserVO> empty = PagedResult.empty();

// 空列表但保留总数
PagedResult<UserVO> none = PagedResult.empty(total);
```

### 字段说明

| 字段 | 类型 | 说明 |
|------|------|------|
| `total` | `int` | 查询总数 |
| `items` | `Collection<T>` | 当前页数据列表，`null` 入参自动替换为空列表 |

## 项目结构

```
io.soil.jsf.pojo.dto/
└── PagedResult.java    # 分页结果响应体
```
