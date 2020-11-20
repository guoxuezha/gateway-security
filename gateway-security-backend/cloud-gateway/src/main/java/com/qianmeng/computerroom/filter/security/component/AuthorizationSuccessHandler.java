package com.qianmeng.computerroom.filter.security.component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qianmeng.computerroom.components.JwtTokenUtil;
import com.qianmeng.computerroom.po.SysFrontendMenu;
import com.qianmeng.computerroom.redis.RedisUtil;
import com.qianmeng.computerroom.service.SysFrontendMenuService;
import com.qianmeng.computerroom.util.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 郭超
 * Date:2020-09-29 11:00
 * Description: 鉴权成功处理类
 */
@Slf4j
@Component
public class AuthorizationSuccessHandler {

    @Resource
    private SysFrontendMenuService menuService;

    @Resource
    private JwtTokenUtil jwtTokenUtil;

    @Resource
    private RedisUtil redisUtil;

    /**
     * 根据用户名查询出用户详情,并转换为DataBuffer返回
     *
     * @param userName 用户名
     * @param response 响应
     * @return 用户详细信息流
     */
    public DataBuffer onAuthorizationSuccess(String userName, ServerHttpResponse response) {
        String userToken = (String) redisUtil.get(userName);
        if (userToken == null || "".equals(userToken.trim())) {
            // 如果token为空,则通过JWT创建一个新的token
            userToken = jwtTokenUtil.generateToken(userName);

            // 把新的token存储到Redis中
            redisUtil.set(userName, userToken);
            log.info("第一次登陆,token为空,创建并存储到Redis中的Token:" + userToken);
        } else {
            log.info("3.2 Redis中已有该用户, 根据用户名从Redis中取到的token = " + userToken);
        }
        
        // 设置body,封装数据
        List<SysFrontendMenu> menus = menuService.getMenusByUserName(userName);

        Map<String, Object> map = new HashMap<>(8);
        map.put("username", userName);
        map.put("menus", menus);
        map.put("token", userToken);
        log.info("4. 存入Map中返回到前台的数据内容为: " + map);
        ResponseResult result = ResponseResult.success(map);

        byte[] dataBytes;
        ObjectMapper mapper = new ObjectMapper();
        try {
            dataBytes = mapper.writeValueAsBytes(result);
        } catch (JsonProcessingException e) {
            dataBytes = ResponseResult.fail("授权异常").toString().getBytes();
        }

        return response.bufferFactory().wrap(dataBytes);
    }
}
