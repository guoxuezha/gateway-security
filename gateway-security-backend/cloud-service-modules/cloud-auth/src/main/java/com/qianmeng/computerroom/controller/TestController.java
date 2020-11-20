package com.qianmeng.computerroom.controller;

import com.qianmeng.computerroom.mail.DeveloperEmail;
import com.qianmeng.computerroom.redis.RedisUtil;
import com.qianmeng.computerroom.util.resulthelper.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author 郭超
 * Date:2020-11-12 16:14
 * Description:
 */
@Slf4j
@RestController
@RequestMapping("/test")
@DeveloperEmail(name = "郭超")
public class TestController {

    @Resource
    private RedisUtil redisUtil;

    /**
     * 测试从注解中获取开发者邮箱
     *
     * @return msg
     */
    @GetMapping("/getDeveloperName")
    public ResponseResult getDeveloperName() {
        Object email = redisUtil.get("email");
        log.info("email = " + email);
        return ResponseResult.success();
    }

    /**
     * 测试报错时发送异常信息邮件
     *
     * @return msg
     */
    @GetMapping("/testExceptionSendMail")
    public ResponseResult testExceptionSendMail() {
        int i = 10 / 0;
        return ResponseResult.success();
    }

}
