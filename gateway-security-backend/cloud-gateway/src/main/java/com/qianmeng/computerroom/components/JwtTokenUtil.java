package com.qianmeng.computerroom.components;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT生成令牌、验证令牌、获取令牌
 *
 * @author qianmeng
 */
@Component
@Slf4j
public class JwtTokenUtil {
    /**
     * 私钥
     */
    private static final String SECRET_KEY = "coding-study";

    /**
     * 过期时间,单位毫秒,设置默认1周的时间过期
     */
    private static final long EXPIRATION_TIME = 3600000L * 24 * 7;

    /**
     * 根据用户的基本信息生成令牌
     *
     * @param userName 用户名
     * @return 令牌
     */
    public String generateToken(String userName) {
        Map<String, Object> claims = new HashMap<>(2);
        claims.put(Claims.SUBJECT, userName);
        claims.put(Claims.ISSUED_AT, new Date());
        return generateToken(claims);
    }

    /**
     * 从令牌中获取用户名
     *
     * @param token 令牌
     * @return 用户名
     */
    public String getUsernameFromToken(String token) {
        String username = null;
        log.info("token === " + token);
        try {
            Claims claims = getClaimsFromToken(token);
            username = claims.getSubject();
        } catch (Exception e) {
            log.info("Exception = " + e.getMessage());
        }
        return username;
    }

    /**
     * 判断令牌是否过期
     *
     * @param token 令牌
     * @return 是否过期, 已经过期为True, 未过期为False
     */
    public Boolean isTokenExpired(String token) {
        Claims claims;
        try {
            claims = getClaimsFromToken(token);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        Date expiration = claims.getExpiration();
        return expiration.before(new Date());
    }

    /**
     * 刷新令牌
     *
     * @param token 原令牌
     * @return 新令牌
     */
    public String refreshToken(String token) {
        String refreshedToken;
        try {
            Claims claims = getClaimsFromToken(token);
            claims.put(Claims.ISSUED_AT, new Date());
            refreshedToken = generateToken(claims);
        } catch (Exception e) {
            refreshedToken = null;
        }
        return refreshedToken;
    }

    /**
     * 验证令牌
     *
     * @param token    令牌
     * @param userName 用户名
     * @return 是否有效
     */
    public Boolean validateToken(String token, String userName) {
        String userDetailsName = userName;
        String username = getUsernameFromToken(token);
        return (username.equals(userDetailsName) && !isTokenExpired(token));
    }

    /**
     * 从数据声明生成令牌
     *
     * @param claims 数据声明
     * @return 令牌
     */
    private String generateToken(Map<String, Object> claims) {
        Date expirationDate = new Date(System.currentTimeMillis() + EXPIRATION_TIME);
        return Jwts.builder().setClaims(claims).setExpiration(expirationDate).signWith(SignatureAlgorithm.HS512, SECRET_KEY).compact();
    }

    /**
     * 从令牌中获取数据声明
     *
     * @param token 令牌
     * @return 数据声明
     */
    private static Claims getClaimsFromToken(String token) {
        try {
            // jwtToken过期会发生异常,但ExpiredJwtException对象还是返回了过期的Claims对象
            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
            if (claimsJws != null) {
                return claimsJws.getBody();
            } else {
                return null;
            }
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }
}