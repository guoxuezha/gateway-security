package com.qianmeng.computerroom.filter.security.exception;

import com.qianmeng.computerroom.filter.security.constant.AuthorizeStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;

/**
 * @author 郭超
 * Date:2020-09-29 11:06
 * Description: 鉴权失败异常处理类
 */
@Slf4j
@Component
public class AuthorizationExceptionHandler {

    /**
     * 根据鉴权时返回的状态值不同,返回不同的警告信息
     *
     * @param authorizeStatus 鉴权异常状态
     * @param response        响应
     * @return 警告信息Buffer
     */
    public DataBuffer getWarningInfo(int authorizeStatus, ServerHttpResponse response) {
        String exceptionMessage;
        switch (authorizeStatus) {
            case AuthorizeStatus.AUTHORIZE_INSUFFICIENT:
                exceptionMessage = "访问失败,权限不足！";
                break;
            case AuthorizeStatus.NULL_AUTHORIZATION:
                exceptionMessage = "访问失败,权限为空！";
                break;
            default:
                exceptionMessage = "";
                break;
        }
        return response.bufferFactory().wrap(exceptionMessage.getBytes());
    }
}
