# jsf-mail-sender

基于 Spring `JavaMailSender` 通过 SMTP 协议发送邮件的通用模块，支持纯文本、HTML 以及带附件的邮件。

## 引入

```xml
<dependency>
    <groupId>io.soil.jsf</groupId>
    <artifactId>jsf-mail-sender</artifactId>
    <version>${jsf.version}</version>
</dependency>
```

模块通过 `META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports`
自动注册 `MailConfig`，在 Spring Boot 应用中无需额外 `@Import`。

## 配置

在 `application.yml` 中配置 SMTP 服务器信息（前缀 `jsf.mail`）：

```yaml
jsf:
  mail:
    host: smtp.example.com      # SMTP 服务器地址
    port: 465                   # 端口, 默认 25
    username: noreply@example.com
    password: your-auth-code    # 邮箱授权码
    protocol: smtp              # 默认 smtp
    from: noreply@example.com   # 默认发件人
    from-name: 系统通知          # 默认发件人显示名称(可选)
    auth: true                  # 是否 SMTP 认证, 默认 true
    starttls-enable: true       # 是否启用 STARTTLS, 默认 true
    connection-timeout: 5000    # 连接超时(ms)
    timeout: 5000               # 读超时(ms)
    write-timeout: 5000         # 写超时(ms)
    debug: false                # JavaMail 调试日志
```

> 只有配置了 `jsf.mail.host` 时，`JavaMailSender` 才会被创建。
> 若容器已存在 `JavaMailSender` Bean（例如使用 Spring Boot 自带的 `spring.mail.*`），本配置会自动跳过。

## 使用

注入 `MailUtils` 即可发送邮件：

```java
@Autowired
private MailUtils mailUtils;

// 发送纯文本邮件
mailUtils.sendText("user@example.com", "验证码", "您的验证码是 123456");

// 发送 HTML 邮件
mailUtils.sendHtml("user@example.com", "周报", "<h1>本周总结</h1>");

// 发送带附件的邮件
Map<String, InputStreamSource> attachments = new HashMap<>();
attachments.put("report.pdf", new ByteArrayResource(bytes));
mailUtils.send("user@example.com", "报表", "请查收附件", false, attachments);
```

可用方法：

| 方法 | 说明 |
| --- | --- |
| `sendText(String to, String subject, String content)` | 发送纯文本邮件（单收件人） |
| `sendText(String[] to, String subject, String content)` | 发送纯文本邮件（多收件人） |
| `sendHtml(String to, String subject, String html)` | 发送 HTML 邮件 |
| `send(String to, String subject, String content, boolean html)` | 发送文本或 HTML 邮件 |
| `send(String to, String subject, String content, boolean html, Map<String, InputStreamSource> attachments)` | 发送带附件邮件 |
| `send(String from, String[] to, String[] cc, String[] bcc, String subject, String content, boolean html, Map<String, InputStreamSource> attachments)` | 完整参数发送 |

发送失败（地址解析失败、SMTP 连接/发送异常等）时会抛出模块内的 `MailException`，可通过 `code()` 获取错误码：

| 错误码 | 含义 |
| --- | --- |
| `MAIL-RECIPIENT-EMPTY` | 收件人为空 |
| `MAIL-FROM-NOT-CONFIGURED` | 未配置默认发件人（或发件人地址非法） |
| `MAIL-SEND-FAILED` | 邮件发送失败（含原始异常原因） |
