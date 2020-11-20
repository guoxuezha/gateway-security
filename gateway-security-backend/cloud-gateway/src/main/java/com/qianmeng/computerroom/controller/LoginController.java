package com.qianmeng.computerroom.controller;

import com.qianmeng.computerroom.components.JwtTokenUtil;
import com.qianmeng.computerroom.po.SysUser;
import com.qianmeng.computerroom.redis.RedisUtil;
import com.qianmeng.computerroom.service.SysUserService;
import com.qianmeng.computerroom.util.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author 郭超
 * Date:2020-09-22 17:30
 * Description:
 */
@RestController
@Slf4j
public class LoginController {

    @Resource
    private RedisUtil redisUtil;

    @Resource
    private JwtTokenUtil jwtTokenUtil;

    @Resource
    private SysUserService userService;

    /**
     * 通过userName获取userId
     *
     * @param userName 用户名
     * @return 用户ID
     */
    @GetMapping("/getUserId")
    public Mono<Integer> getUserName(String userName) {
        Integer userId = (Integer) redisUtil.get(userName + "ID");
        log.info("userId = " + userId);
        return Mono.create(monoSink -> monoSink.success(userId));
    }

    /**
     * 根据token获取user信息
     *
     * @param token token
     * @return userMap
     */
    @GetMapping("/getUserInfoByToken")
    public Mono<ResponseResult> getUserInfoByToken(String token) {
        Map<String, Object> infoMap = userService.getUserInfo(token);
        log.info("根据token获取到的用户信息 = " + infoMap.toString());
        return Mono.just(ResponseResult.success(infoMap));
    }

    /**
     * 根据Token清空userName在Redis中的缓存
     *
     * @param token 用户Token
     * @return 执行结果
     */
    @GetMapping("/logout")
    public Mono<ResponseResult> logout(String token) {
        log.info("要清除的token = " + token);
        String usernameFromToken = jwtTokenUtil.getUsernameFromToken(token);
        redisUtil.del(usernameFromToken);
        return Mono.create(monoSink -> monoSink.success(ResponseResult.success()));
    }

    @GetMapping("/testRedisCache")
    public Mono<ResponseResult> testRedisCache(Integer userId) {
        SysUser sysUser = userService.getById(userId);
        log.info("sysUser === " + sysUser);
        return Mono.just(ResponseResult.success(sysUser));
    }

}
