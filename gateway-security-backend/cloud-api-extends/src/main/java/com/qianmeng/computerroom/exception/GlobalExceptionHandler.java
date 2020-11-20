package com.qianmeng.computerroom.exception;

import com.qianmeng.computerroom.aop.GetDeveloperName;
import com.qianmeng.computerroom.mail.MailUtil;
import com.qianmeng.computerroom.redis.RedisUtil;
import com.qianmeng.computerroom.util.resulthelper.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.annotation.Resource;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * @author qianmeng
 * className GlobalExceptionHandler
 * description 全局异常处理类
 * date 2020/7/11 11:16
 **/
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @Resource
    private RedisUtil redisUtil;

    @Resource
    private MailUtil mailUtil;

    /**
     * 全局异常捕捉处理,判断错误是否是已定义的已知错误,不是则由未知错误代替,同时记录在log中
     *
     * @param e 异常对象
     * @return 报错信息
     */
    @ExceptionHandler(value = Exception.class)
    public ResponseResult exceptionGet(Exception e) {
        // 从Redis中获取异常发生代码的维护者邮箱
        String email = (String) redisUtil.get(GetDeveloperName.EMAIL);
        // 发送邮件
        if (StringUtils.isNotBlank(email)) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            mailUtil.sendSimpleMail(email, "机房运维管理系统异常", sw.toString());
        }
        log.error("全局异常捕获", e);
        return ResponseResult.fail(e.getMessage());
    }

    /**
     * 应用到所有@RequestMapping注解方法,在其执行之前初始化数据绑定器
     *
     * @param binder
     */
    /*@InitBinder
    public void initBinder(WebDataBinder binder) {

    }*/

}
