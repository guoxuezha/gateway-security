package com.qianmeng.computerroom.util;

import com.alibaba.fastjson.JSONObject;
import org.springframework.http.server.reactive.ServerHttpRequest;

/**
 * @author 郭超
 * Date:2020-08-13 18:10
 * Description: Json帮助类
 */
public class GetRequestJsonUtils {

    public static JSONObject getRequestJsonObject(ServerHttpRequest request) {
        String json = getRequestJsonString(request);
        return JSONObject.parseObject(json);
    }

    /***
     * 获取 request 中 json 字符串的内容
     *
     * @param request ServerHttpRequest
     * @return : <code>byte[]</code>
     */
    private static String getRequestJsonString(ServerHttpRequest request) {
        String submitMethod = request.getMethod().name();
        return request.getQueryParams().getFirst("username") + request.getQueryParams().getFirst("password");
    }

}
