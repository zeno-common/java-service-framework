# `BaseException`

`package io.soil.common.exception`

extends `RuntimeException`

`@Slf4j`

异常基类。

主要以下用途：
1. 用于制定通用的异常信息，给派生类的构建函数定义进行参考。
2. 有助于构异异常类的构建，并提供多种场景下异常类构建函数的定义参考，方便快速定义异常类和易于实例化异常类对象。

- **Author:** zeno.w

---

## Methods

### `abstract String module()`

→ **String** — 模块名

获取异常模块名称

---

### `String getStackTraceString()`

→ **String** — 异常栈字符串

获取异常栈字符串

---

### `static String getStackTraceString(Throwable throwable)`

→ **String** — 异常栈字符串

获取异常栈字符串

| Param | Type | Description |
|-------|------|-------------|
| `throwable` | `Throwable` | 异常对象 |