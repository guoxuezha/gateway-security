package com.qianmeng.computerroom.filter.security;

import com.qianmeng.computerroom.filter.security.component.*;
import com.qianmeng.computerroom.filter.security.constant.AuthenticatedStatus;
import com.qianmeng.computerroom.filter.security.constant.AuthorizeStatus;
import com.qianmeng.computerroom.filter.security.exception.AuthenticationExceptionHandler;
import com.qianmeng.computerroom.filter.security.exception.AuthorizationExceptionHandler;
import com.qianmeng.computerroom.filter.security.po.Authentication;
import com.qianmeng.computerroom.util.SecurityHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;

/**
 * @author 郭超
 * Date:2020-09-29 9:50
 * Description: 认证授权主配置类,使用过滤器链需要中间存储对象
 */
@Slf4j
@Component
@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE + 2)
public class SecurityFilter implements WebFilter {

    /**
     * 处理直接放行的请求
     */
    @Resource
    private ReleaseRequestHandler releaseRequestHandler;

    /**
     * 登陆请求处理类
     */
    @Resource
    private UserPassAuthenticationHandler userPassAuthenticationHandler;

    /**
     * 根据Token完成认证的处理类
     */
    @Resource
    private TokenAuthenticationHandler tokenAuthenticationHandler;

    /**
     * 用户鉴权处理类
     */
    @Resource
    private DynamicVerification dynamicPermission;

    /**
     * 鉴权成功处理类
     */
    @Resource
    private AuthorizationSuccessHandler authorizationSuccessHandler;

    /**
     * 认证失败异常处理类
     */
    @Resource
    private AuthenticationExceptionHandler authenticationExceptionHandler;

    /**
     * 鉴权失败异常处理类
     */
    @Resource
    private AuthorizationExceptionHandler authorizationExceptionHandler;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        // 0. 判断是否为直接放行的请求
        boolean isReleaseRequest = releaseRequestHandler.isReleaseRequest(request);
        if (isReleaseRequest) {
            // 如果是则直接放行
            return chain.filter(exchange);
        }
        // 1. 为response装填JSONHeader
        ServerHttpResponse response = SecurityHelper.setResponseHeader(exchange.getResponse());

        // 2. 拦截请求,判断是否为第一次登陆
        boolean isLoginPath = userPassAuthenticationHandler.isLoginPath(request);
        boolean isStaticResourcePath = tokenAuthenticationHandler.isResourceRequest(request);
        Authentication authentication;
        if (isLoginPath) {
            // 2.1 如果是,则进入通过账号密码认证的逻辑
            authentication = userPassAuthenticationHandler.authenticate(exchange);
        } else {
            // 2.2 再判断是否访问静态资源
            if (isStaticResourcePath) {
                // 2.2.1 如果是,则直接放行
                return chain.filter(exchange);
            } else {
                // 2.2.2 如果都不是,则进入通过token认证的逻辑
                authentication = tokenAuthenticationHandler.authenticate(request);
            }
        }

        int authenticatedStatus = authentication.getAuthenticatedStatus();
        String userName = authentication.getUserName();

        // 3 根据认证结果,判断是执行鉴权还是回写警告
        DataBuffer dataBuffer;
        if (authenticatedStatus == AuthenticatedStatus.AUTHENTICATION_SUCCESS ||
                authenticatedStatus == AuthenticatedStatus.FIRST_LOGIN_SUCCESS) {
            // 3.1 认证成功,进入鉴权逻辑
            int authorizeStatus = dynamicPermission.check(userName, request);

            if (authorizeStatus == AuthorizeStatus.AUTHORIZE_SUCCESS) {
                if (authenticatedStatus == AuthenticatedStatus.AUTHENTICATION_SUCCESS) {
                    // 3.1.1 鉴权成功,且为Token,则直接放行
                    return chain.filter(exchange);
                }
                dataBuffer = authorizationSuccessHandler.onAuthorizationSuccess(userName, response);
            } else {
                // 3.1.2 鉴权失败,返回警告信息
                dataBuffer = authorizationExceptionHandler.getWarningInfo(authorizeStatus, response);
            }
        } else {
            // 3.2 认证失败,则根据返回状态,返回警告信息
            dataBuffer = authenticationExceptionHandler.getWarningInfo(authenticatedStatus, response);
        }
        return response.writeWith(Mono.just(dataBuffer));
    }

}
