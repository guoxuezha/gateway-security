package com.qianmeng.computerroom.aop;

import com.qianmeng.computerroom.mail.Developer;
import com.qianmeng.computerroom.mail.DeveloperEmail;
import com.qianmeng.computerroom.mail.GetEmailHelper;
import com.qianmeng.computerroom.redis.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author 郭超
 * Date:2020-11-17 15:58
 * Description:
 */
@Slf4j
@Aspect
@Component
public class GetDeveloperName {

    public static String DEVELOPER = "developerName";
    public static String EMAIL = "email";

    @Resource
    private RedisUtil redisUtil;

    @Resource
    private Developer developer;

    /**
     * 在所有controller方法执行时添加增强功能
     */
    @Pointcut("execution(public * com.qianmeng.computerroom.controller..*.*(..))")
    public void getDeveloperNamePointCut() {
    }

    @Around(value = "getDeveloperNamePointCut()")
    public Object insertDataProcessing(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("AOP——Controller异常处理！来自Extends模块");
        Object[] objs = joinPoint.getArgs();

        //  通过方法所在的类的注解获取开发者姓名
        DeveloperEmail obj = ((MethodSignature) joinPoint.getSignature()).getMethod().getDeclaringClass().getAnnotation(DeveloperEmail.class);
        log.info(" =====DeveloperEmail obj==== = " + obj);
        if (obj != null) {
            String developerName = obj.name();
            String existDeveloperName = (String) redisUtil.get(DEVELOPER);
            String email = (String) redisUtil.get(EMAIL);
            log.info("developerName = " + developerName + "   /   existDeveloperName = " + existDeveloperName + "   /   existEmail = " + email);
            if (existDeveloperName != null && existDeveloperName.equals(developerName) && email != null) {
                // 如果Redis中开发者没变,则沿用
                return joinPoint.proceed(objs);
            } else {
                // 否则根据开发者姓名获取邮箱
                String newEmail = GetEmailHelper.getEmail(developerName, developer);
                log.info("email = " + email);
                if (email != null && !"".equals(email.trim())) {
                    email = newEmail;
                } else {
                    log.error("未能找到开发者" + developerName + "的邮箱信息");
                }
            }
            redisUtil.set(DEVELOPER, developerName);
            redisUtil.set(EMAIL, email);
        } else {
            log.error("未能通过注解获取到开发者邮箱!");
        }
        // 返回数据
        return joinPoint.proceed(objs);
    }

}
