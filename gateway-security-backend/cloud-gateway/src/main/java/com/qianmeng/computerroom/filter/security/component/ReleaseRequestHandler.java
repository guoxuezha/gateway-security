package com.qianmeng.computerroom.filter.security.component;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author 郭超
 * Date:2020-11-03 9:40
 * Description: 处理直接放行的请求
 */
@Slf4j
@Component
public class ReleaseRequestHandler {

    private List<String> releaseRequestPath = new ArrayList<>(Arrays.asList("/agent/"));

    /**
     * 判断是否为代理模块发送的请求
     *
     * @param request request
     * @return boolean
     */
    public boolean isReleaseRequest(ServerHttpRequest request) {
        // 获取request请求的Path
        String path = request.getPath().toString();
        boolean result = path.matches("/agent/");
        log.info("path.equals(agentPath) = " + result);
        return result;
    }

    public boolean isReleaseRequest2(ServerHttpRequest request) {
        // 获取request请求的Path
        String path = request.getPath().toString();
        AntPathMatcher antPathMatcher = new AntPathMatcher();
        boolean result = releaseRequestPath.stream().anyMatch(item -> {
            return antPathMatcher.match(item, path);
        });
        log.info("path.equals(agentPath) = " + result);
        return result;
    }
}
