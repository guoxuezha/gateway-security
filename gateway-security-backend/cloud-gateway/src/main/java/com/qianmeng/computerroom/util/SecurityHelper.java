package com.qianmeng.computerroom.util;

import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpResponse;

/**
 * @author: 郭超
 * Date:2020-09-29 10:49
 * Description:
 */
public class SecurityHelper {

    /**
     * 为Response装填请求头基本信息
     *
     * @param response HttpServletResponse
     * @return 携带可回传JSON数据请求头的Response
     */
    public static ServerHttpResponse setResponseHeader(ServerHttpResponse response) {
        HttpHeaders httpHeaders = response.getHeaders();
        httpHeaders.add("Content-Type", "application/json; charset=UTF-8");
        httpHeaders.add("Cache-Control", "no-store, no-cache, must-revalidate, max-age=0");
        return response;
    }
}
