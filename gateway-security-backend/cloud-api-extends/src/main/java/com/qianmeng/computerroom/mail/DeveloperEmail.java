package com.qianmeng.computerroom.mail;

import java.lang.annotation.*;

/**
 * @author 郭超
 * Date:2020-07-11 11:18
 * Description: 自定义注解,异常信息发送邮件给相关开发者
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DeveloperEmail {
    String name() default  "";

    String email() default "";
}
