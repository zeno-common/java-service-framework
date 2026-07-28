package io.soil.jsf.mail.sender.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 邮件发送配置属性, 对应配置文件前缀 {@code jsf.mail}.
 * <p>
 * 示例 (application.yml):
 * <pre>{@code
 * jsf:
 *   mail:
 *     host: smtp.example.com
 *     port: 465
 *     username: noreply@example.com
 *     password: your-auth-code
 *     protocol: smtp
 *     from: noreply@example.com
 *     from-name: 系统通知
 *     auth: true
 *     starttls-enable: true
 * }</pre>
 *
 * @author zeno
 */
@Data
@ConfigurationProperties(prefix = MailProperties.PREFIX)
public class MailProperties {

    /** 配置前缀 */
    public static final String PREFIX = "jsf.mail";

    /** SMTP 服务器主机 */
    private String host;

    /** SMTP 服务器端口, 默认 25 */
    private int port = 25;

    /** 登录用户名(通常为邮箱账号) */
    private String username;

    /** 登录密码或授权码 */
    private String password;

    /** 邮件传输协议, 默认 smtp */
    private String protocol = "smtp";

    /** 默认发件人邮箱地址 */
    private String from;

    /** 默认发件人显示名称 */
    private String fromName;

    /** 是否要求进行 SMTP 认证, 默认 true */
    private boolean auth = true;

    /** 是否启用 STARTTLS 加密, 默认 true */
    private boolean starttlsEnable = true;

    /** SMTP 连接超时(毫秒) */
    private String connectionTimeout = "10000";

    /** SMTP 读超时(毫秒) */
    private String timeout = "10000";

    /** SMTP 写超时(毫秒) */
    private String writeTimeout = "10000";

    /** 是否开启 JavaMail 调试日志 */
    private boolean debug = false;
}
