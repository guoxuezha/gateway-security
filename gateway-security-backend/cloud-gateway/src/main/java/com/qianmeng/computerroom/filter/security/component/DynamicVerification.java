package com.qianmeng.computerroom.filter.security.component;

import com.qianmeng.computerroom.filter.security.constant.AuthorizeStatus;
import com.qianmeng.computerroom.po.SysBackendApi;
import com.qianmeng.computerroom.service.SysBackendApiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.RequestPath;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author 郭超
 * Date:2020-09-29 10:45
 * Description: 用户鉴权处理类
 */
@Slf4j
@Component
public class DynamicVerification {

    @Resource
    private SysBackendApiService apiService;

    /**
     * 动态鉴权,根据用户名查询可访问API集合,然后与当前访问API进行匹配
     *
     * @param userName 当前登陆用户名
     * @param request  当前请求
     * @return 鉴权结果
     */
    public int check(String userName, ServerHttpRequest request) {
        // 通过账号获取资源鉴权
        List<SysBackendApi> apiUrls = apiService.getApiUrlByUserName(userName);
        if (apiUrls != null) {
            AntPathMatcher antPathMatcher = new AntPathMatcher();

            // 当前访问路径
            RequestPath requestUri = request.getPath();
            log.info("动态权限校验 => 当前访问路径 = " + requestUri);

            // 提交类型
            HttpMethod urlMethod = request.getMethod();

            // 判断当前路径中是否在资源鉴权中
            boolean rs = apiUrls.stream().anyMatch(item -> {
                // 判断URL是否匹配
                boolean hashAntPath = antPathMatcher.match(item.getApiUrl(), String.valueOf(requestUri));

                // 判断请求方式是否和数据库中匹配（数据库存储：GET,POST,PUT,DELETE）
                String dbMethod = item.getApiMethod();

                // 处理null,万一数据库存null值
                dbMethod = (dbMethod == null) ? "" : dbMethod;
                int hasMethod = dbMethod.indexOf(String.valueOf(urlMethod));
                log.info("hashAntPath && hasMethod = " + (hashAntPath && hasMethod != -1));

                // 两者都成立,返回真,否则返回假
                boolean result = hashAntPath && (hasMethod != -1);
                if (result) {
                    log.info("result == " + true + "<==============URL匹配且用户具有访问权限,权限校验通过!============>");
                }
                return result;
            });
            if (rs) {
                return AuthorizeStatus.AUTHORIZE_SUCCESS;
            } else {
                return AuthorizeStatus.AUTHORIZE_INSUFFICIENT;
            }
        } else {
            return AuthorizeStatus.NULL_AUTHORIZATION;
        }
    }

}
