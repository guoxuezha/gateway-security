package com.qianmeng.computerroom.filter.security.exception;

import com.qianmeng.computerroom.filter.security.constant.AuthenticatedStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;

/**
 * @author 郭超
 * Date:2020-09-29 11:09
 * Description: 异常定义类
 */
@Slf4j
@Component
public class AuthenticationExceptionHandler {

    /**
     * 根据认证时返回的状态值不同,返回不同的警告信息
     *
     * @param authenticatedStatus 认证异常状态
     * @param response            响应
     * @return 警告信息Buffer
     */
    public DataBuffer getWarningInfo(int authenticatedStatus, ServerHttpResponse response) {
        String exceptionMessage;
        switch (authenticatedStatus) {
            case AuthenticatedStatus.USERNAME_NOT_EXSIT:
                exceptionMessage = "认证失败: 用户不存在！";
                break;
            case AuthenticatedStatus.PASSWORD_NOT_MATCH:
                exceptionMessage = "认证失败: 密码不正确！";
                break;
            case AuthenticatedStatus.LOGIN_CONTENT_TYPE_NOT_JSON:
                exceptionMessage = "认证失败: 登陆请求必须为JSON方式！";
                break;
            case AuthenticatedStatus.NULL_USERNAME:
                exceptionMessage = "认证失败: 未能从JSON中获取用户名！";
                break;
            case AuthenticatedStatus.UNKNOWN_EXCEPTION:
                exceptionMessage = "认证失败: 登陆发生未知异常！";
                break;
            case AuthenticatedStatus.NULL_TOKEN:
                exceptionMessage = "认证失败: Token为空,请重新登陆！";
                break;
            case AuthenticatedStatus.INVALID_TOKEN:
                exceptionMessage = "认证失败: 无效Token,请尝试重新登陆！";
                break;
            case AuthenticatedStatus.TOKEN_EXPIRED:
                exceptionMessage = "认证失败: Token已过期,请重新登陆！";
                break;
            default:
                exceptionMessage = "";
                break;
        }
        return response.bufferFactory().wrap(exceptionMessage.getBytes());
    }
}
