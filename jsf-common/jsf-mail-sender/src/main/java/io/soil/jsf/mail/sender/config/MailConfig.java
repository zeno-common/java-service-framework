package io.soil.jsf.mail.sender.config;

import io.soil.jsf.mail.sender.config.properties.MailProperties;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

/**
 * 邮件发送自动配置, 当配置了 {@code jsf.mail.host} 时生效,
 * 基于 {@link MailProperties} 构建并注册一个 {@link JavaMailSender} Bean.
 * <p>
 * 若容器中已存在 {@link JavaMailSender} 类型的 Bean(例如 Spring Boot 自带的
 * {@code spring.mail.*} 自动配置), 本配置将跳过, 避免重复创建.
 *
 * @author zeno
 */
@AutoConfiguration
@EnableConfigurationProperties(MailProperties.class)
@ConditionalOnProperty(prefix = MailProperties.PREFIX, name = "host")
public class MailConfig {

    private final MailProperties properties;

    public MailConfig(MailProperties properties) {
        this.properties = properties;
    }

    @Bean
    @ConditionalOnMissingBean(JavaMailSender.class)
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl sender = new JavaMailSenderImpl();
        sender.setHost(properties.getHost());
        sender.setPort(properties.getPort());
        sender.setUsername(properties.getUsername());
        sender.setPassword(properties.getPassword());
        sender.setProtocol(properties.getProtocol());
        sender.setDefaultEncoding("UTF-8");

        Properties props = sender.getJavaMailProperties();
        props.put("mail.smtp.auth", String.valueOf(properties.isAuth()));
        props.put("mail.smtp.starttls.enable", String.valueOf(properties.isStarttlsEnable()));
        props.put("mail.smtp.connectiontimeout", properties.getConnectionTimeout());
        props.put("mail.smtp.timeout", properties.getTimeout());
        props.put("mail.smtp.writetimeout", properties.getWriteTimeout());
        props.put("mail.debug", String.valueOf(properties.isDebug()));
        return sender;
    }
}
