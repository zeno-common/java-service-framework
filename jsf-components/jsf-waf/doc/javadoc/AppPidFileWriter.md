# AppPidFileWriter

`io.soil.waf.AppPidFileWriter`

应用 PID 文件写入器，将应用进程 ID 写入 `./app.pid` 文件。继承自 Spring Boot 的 `ApplicationPidFileWriter`。

## 类签名

```java
public class AppPidFileWriter extends ApplicationPidFileWriter
```

## 构造方法

### AppPidFileWriter()

```java
public AppPidFileWriter()
```

构造 PID 写入器，PID 文件路径为 `./app.pid`。

## 使用方式

### 方式一：SpringApplication 注册

```java
@SpringBootApplication
public class MyApp {
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(MyApp.class);
        app.addListeners(new AppPidFileWriter());
        app.run(args);
    }
}
```

### 方式二：application.yml 配置

```yaml
spring:
  pid:
    file: ./app.pid
```

## PID 文件用途

- 便于脚本获取应用进程 ID
- 配合监控工具检测应用存活状态
- 支持优雅停机脚本（如 `kill $(cat app.pid)`）