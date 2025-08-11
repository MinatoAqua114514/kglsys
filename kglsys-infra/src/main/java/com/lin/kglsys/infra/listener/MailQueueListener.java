package com.lin.kglsys.infra.listener;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
@RabbitListener(queues = "${app.rabbitmq.email-queue}")
public class MailQueueListener {

    @Resource
    JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    String username;

    @Value("${app.frontend.base-url}") // 1. 注入前端基础URL
    private String frontendBaseUrl;

    /**
     * 发送邮件
     */
    @RabbitHandler
    public void sendMail(Map<String, Object> data) {
        String email = (String) data.get("email");
        Object codeOrToken = data.get("code"); // 可能是Integer或String
        String type = (String) data.get("type");
        SimpleMailMessage message = switch (type) {
            case "register" -> createMessage("欢迎您注册本网站",
                    "您的邮箱验证码为: " + codeOrToken + "。有效时间为3分钟，为了保障您的安全，请勿向他人泄露验证码信息!", email);
            case "forget" -> {
                // 2. 拼接完整的重置链接
                String resetUrl = frontendBaseUrl + "/reset-password?token=" + codeOrToken;
                // 3. 创建包含链接的邮件内容
                String content = String.format(
                        "您好，\n\n您正在申请重置密码。请点击下面的链接来设置您的新密码，该链接在15分钟内有效：\n%s\n\n如果无法点击，请将以上链接复制到浏览器地址栏中打开。\n\n如非您本人操作，请忽略本邮件。",
                        resetUrl
                );
                yield createMessage("密码重置请求", content, email);
            }
            case "admin_reset" -> {
                String content = String.format(
                        "您好，\n\n您的账户密码已被管理员重置。您的新临时密码是: %s\n\n请立即使用此密码登录并修改您的密码，以确保账户安全。",
                        codeOrToken
                );
                yield createMessage("账户密码重置通知", content, email);
            }
            default -> null;
        };
        if (message!=null) {
            log.info("准备发送邮件，数据: {}", data);
            mailSender.send(message);
        } else {
            log.error("邮件类型错误，数据: {}", data);
        }
    }

    private SimpleMailMessage createMessage(String title, String content, String email) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(username);
        message.setTo(email);
        message.setSubject(title);
        message.setText(content);
        return message;
    }
}
