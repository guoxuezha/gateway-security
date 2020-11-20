package com.qianmeng.computerroom.filter.security.component;

import com.qianmeng.computerroom.filter.security.constant.AuthenticatedStatus;
import com.qianmeng.computerroom.filter.security.po.Authentication;
import com.qianmeng.computerroom.po.SysUser;
import com.qianmeng.computerroom.service.SysUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author 郭超
 * Date:2020-09-29 10:35
 * Description: 登陆请求处理类
 */
@Slf4j
@Component
public class UserPassAuthenticationHandler {

    @Resource
    private SysUserService userService;

    /**
     * 根据请求路径判断是否为第一次登陆
     * 需满足以下两个条件才可认为是第一次登陆
     * 1.请求路径为"/login"
     * 2.ContentType为JSON
     *
     * @param request 当前请求
     * @return result
     */
    public boolean isLoginPath(ServerHttpRequest request) {
        // 获取request请求的Path和ContentType
        String path = request.getPath().toString();
        HttpHeaders headers = request.getHeaders();
        MediaType contentType = headers.getContentType();
        log.info("path 0 =========== " + request.getPath());
        /*if (contentType != null) {
            log.info("contentType ============ " + contentType);
            if (contentType.toString().equals(MediaType.APPLICATION_JSON_VALUE)
                    || contentType.toString().equals(MediaType.APPLICATION_JSON_UTF8_VALUE)) {*/
        String loginPath = "/login";
        boolean result = path.equals(loginPath);
        log.info("path.equals(loginPath) = " + result);
        return result;
            /*}
        }
        return false;*/
    }

    /**
     * 账号密码登陆认证
     *
     * @param exchange ServerWebExchange
     * @return 认证结果：是否存在该用户
     */
    public Authentication authenticate(ServerWebExchange exchange) {
        Authentication authentication = new Authentication();
        MultiValueMap<String, String> params = exchange.getRequest().getQueryParams();
        log.info("userMap.toString()" + params.toString());
        String userName = params.get("username").toString();
        if (userName != null && !"".equals(userName)) {
            userName = userName.replaceAll("[\\[\\]]", "");
            authentication.setUserName(userName);
            String password = params.get("password").toString().replaceAll("[\\[\\]]", "");
            log.info("<===========用户:" + userName);
            // 先判断用户是否存在
            SysUser user = userService.getUserByUserName(userName);
            if (user != null) {
                // 执行认证
                try {
                    if (userService.checkLogin(userName, password)) {
                        log.info("<===========用户:" + userName + "存在,身份验证通过!===========>");
                        authentication.setAuthenticatedStatus(AuthenticatedStatus.FIRST_LOGIN_SUCCESS);
                    } else {
                        authentication.setAuthenticatedStatus(AuthenticatedStatus.PASSWORD_NOT_MATCH);
                    }
                } catch (Exception e) {
                    authentication.setAuthenticatedStatus(AuthenticatedStatus.UNKNOWN_EXCEPTION);
                    e.printStackTrace();
                }
            } else {
                authentication.setAuthenticatedStatus(AuthenticatedStatus.USERNAME_NOT_EXSIT);
            }
        } else {
            authentication.setAuthenticatedStatus(AuthenticatedStatus.NULL_USERNAME);
        }
        log.info("认证后得到的Authentication对象 = " + authentication.toString());
        return authentication;
    }

    private Map<String, Object> decodeBody(String body) {
        return Arrays.stream(body.split("&"))
                .map(s -> s.split("="))
                .collect(Collectors.toMap(arr -> arr[0], arr -> arr[1]));
    }

    private String encodeBody(Map<String, Object> map) {
        return map.entrySet().stream().map(e -> e.getKey() + "=" + e.getValue()).collect(Collectors.joining("&"));
    }

}
