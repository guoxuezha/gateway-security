package com.qianmeng.computerroom.filter.security.component;

import com.qianmeng.computerroom.components.JwtTokenUtil;
import com.qianmeng.computerroom.filter.security.constant.AuthenticatedStatus;
import com.qianmeng.computerroom.filter.security.po.Authentication;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author 郭超
 * Date:2020-09-29 10:42
 * Description: 根据Token完成认证的处理类
 */
@Slf4j
@Component
public class TokenAuthenticationHandler {

    @Resource
    private JwtTokenUtil jwtTokenUtil;

    /**
     * 判断请求是否为访问静态资源的请求
     *
     * @param request request
     * @return 判断结果
     */
    public boolean isResourceRequest(ServerHttpRequest request) {
        String path = request.getPath().toString();
        log.info("path =========== " + request.getPath());
        String staticResourcePath = ".*/images/.*";
        boolean result = Pattern.matches(staticResourcePath, path);
        log.info("判断当前路径是否为静态资源的结果为： " + result);
        return result;
    }

    /**
     * 根据token执行认证
     *
     * @param request ServerHttpRequest
     * @return 认证结果
     */
    public Authentication authenticate(ServerHttpRequest request) {
        List<String> auth = request.getHeaders().get(HttpHeaders.AUTHORIZATION);
        Authentication authentication = new Authentication();
        if (auth != null && auth.size() > 0 && !StringUtils.isEmpty(auth.get(0))) {
            log.info("获取到的 headerToken = " + auth.get(0));
            String headerToken = auth.get(0);

            // postMan测试时,自动加入的前缀,要去掉。
            String token = headerToken.replace("Bearer", "").trim();

            // 先判断令牌是否过期
            boolean isExpired = jwtTokenUtil.isTokenExpired(token);
            if (isExpired) {
                // 过期刷新
                jwtTokenUtil.refreshToken(token);
            }
            // 通过令牌获取用户名称
            String username = jwtTokenUtil.getUsernameFromToken(token);
            log.info("从token令牌中获取到的username = " + username);
            if (username != null) {
                // 没过期则刷新令牌,重置有效期,然后放行
                jwtTokenUtil.refreshToken(token);
                authentication.setUserName(username);
                authentication.setAuthenticatedStatus(AuthenticatedStatus.AUTHENTICATION_SUCCESS);
                log.info("认证成功:Token认证成功！");
            } else {
                authentication.setAuthenticatedStatus(AuthenticatedStatus.INVALID_TOKEN);
                log.info("认证失败:无效Token,无法从中获取Token！");
            }
        } else {
            authentication.setAuthenticatedStatus(AuthenticatedStatus.NULL_TOKEN);
            log.info("认证失败:当前请求头没有Token信息！");
        }
        return authentication;
    }

}
