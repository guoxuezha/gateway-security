package com.qianmeng.computerroom.mail;

import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;

/**
 * @author 郭超
 * Date:2020-11-17 18:36
 * Description:
 */
@Component
public class MyJavaMailSender {

    @Bean
    public JavaMailSender GetJavaMailSender() {
        return new JavaMailSenderImpl();
    }
}
