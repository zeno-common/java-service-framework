package io.soil.jsf.mail.sender.utils;

import io.soil.jsf.mail.sender.config.properties.MailProperties;
import io.soil.jsf.mail.sender.exception.MailException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * 邮件发送工具类, 基于 Spring {@link JavaMailSender} 通过 SMTP 发送邮件.
 * <p>
 * 支持发送纯文本邮件、HTML 邮件以及带附件的邮件, 收件人/抄送人可为多个.
 * 发送失败时会包装为 {@link MailException} 抛出.
 * <p>
 * 使用前需在配置文件中指定 SMTP 服务器信息(见 {@link MailProperties}),
 * 未指定 {@code from} 时必须通过方法参数显式传入发件人.
 *
 * @author zeno
 */
@Component
public class MailUtils {

    private final JavaMailSender mailSender;
    private final MailProperties properties;

    public MailUtils(JavaMailSender mailSender, MailProperties properties) {
        this.mailSender = mailSender;
        this.properties = properties;
    }

    /**
     * 发送纯文本邮件
     *
     * @param to      收件人
     * @param subject 主题
     * @param content 文本内容
     */
    public void sendText(String to, String subject, String content) {
        send(resolveFrom(null), new String[]{to}, null, null, subject, content, false, null);
    }

    /**
     * 发送纯文本邮件(多收件人)
     *
     * @param to      收件人数组
     * @param subject 主题
     * @param content 文本内容
     */
    public void sendText(String[] to, String subject, String content) {
        send(resolveFrom(null), to, null, null, subject, content, false, null);
    }

    /**
     * 发送 HTML 邮件
     *
     * @param to      收件人
     * @param subject 主题
     * @param html    HTML 内容
     */
    public void sendHtml(String to, String subject, String html) {
        send(resolveFrom(null), new String[]{to}, null, null, subject, html, true, null);
    }

    /**
     * 发送邮件(可指定文本或 HTML)
     *
     * @param to      收件人
     * @param subject 主题
     * @param content 内容
     * @param html    是否为 HTML 内容
     */
    public void send(String to, String subject, String content, boolean html) {
        send(resolveFrom(null), new String[]{to}, null, null, subject, content, html, null);
    }

    /**
     * 发送带附件的邮件
     *
     * @param to          收件人
     * @param subject     主题
     * @param content     内容
     * @param html        是否为 HTML 内容
     * @param attachments 附件集合, key 为附件名称(含扩展名), value 为附件输入流源
     */
    public void send(String to, String subject, String content, boolean html,
                     Map<String, InputStreamSource> attachments) {
        send(resolveFrom(null), new String[]{to}, null, null, subject, content, html, attachments);
    }

    /**
     * 发送完整邮件(显式指定发件人/收件人/抄送人及附件)
     *
     * @param from        发件人(为空时使用配置中的默认发件人)
     * @param to          收件人数组
     * @param cc          抄送人数组(可为 null)
     * @param bcc         密送人数组(可为 null)
     * @param subject     主题
     * @param content     内容
     * @param html        是否为 HTML 内容
     * @param attachments 附件集合(可为 null)
     */
    public void send(String from, String[] to, String[] cc, String[] bcc,
                     String subject, String content, boolean html,
                     Map<String, InputStreamSource> attachments) {
        if (to == null || to.length == 0) {
            throw new MailException(MailException.RECIPIENT_EMPTY, "收件人不能为空");
        }
        send(resolveFrom(from), to, cc, bcc, subject, content, html, attachments);
    }

    private void send(InternetAddress from, String[] to, String[] cc, String[] bcc,
                      String subject, String content, boolean html,
                      Map<String, InputStreamSource> attachments) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            // 第二个参数 true 表示支持 multipart(附件)
            MimeMessageHelper helper = new MimeMessageHelper(message, true, StandardCharsets.UTF_8.name());
            helper.setFrom(from);
            helper.setTo(to);
            if (cc != null && cc.length > 0) {
                helper.setCc(cc);
            }
            if (bcc != null && bcc.length > 0) {
                helper.setBcc(bcc);
            }
            helper.setSubject(subject);
            helper.setText(content, html);
            if (attachments != null) {
                for (Map.Entry<String, InputStreamSource> entry : attachments.entrySet()) {
                    helper.addAttachment(entry.getKey(), entry.getValue());
                }
            }
            mailSender.send(message);
        } catch (org.springframework.mail.MailException | jakarta.mail.MessagingException e) {
            throw new MailException(MailException.SEND_FAILED, "邮件发送失败: " + subject, e);
        }
    }

    /**
     * 解析发件人地址; 当显式 from 为空时使用配置中的默认发件人.
     * 若配置了发件人显示名称, 则返回 {@code 名称 <地址>} 形式.
     *
     * @param from 显式指定的发件人(可为 null 或空)
     * @return 发件人 InternetAddress
     */
    private InternetAddress resolveFrom(String from) {
        String address = StringUtils.hasText(from) ? from : properties.getFrom();
        if (!StringUtils.hasText(address)) {
            throw new MailException(MailException.FROM_NOT_CONFIGURED, "未配置默认发件人(jsf.mail.from)");
        }
        String name = properties.getFromName();
        try {
            if (StringUtils.hasText(name)) {
                return new InternetAddress(address, name, StandardCharsets.UTF_8.name());
            }
            return new InternetAddress(address);
        } catch (UnsupportedEncodingException | jakarta.mail.internet.AddressException e) {
            throw new MailException(MailException.FROM_NOT_CONFIGURED, "发件人地址配置错误: " + address, e);
        }
    }
}
