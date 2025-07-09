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
            case "forget" -> createMessage("你的密码重置邮件",
                    "你好，你正在进行重置密码操作，验证码为: " + codeOrToken + "。有效时间为3分钟，如非您本人操作，请忽视本邮件!", email);
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
