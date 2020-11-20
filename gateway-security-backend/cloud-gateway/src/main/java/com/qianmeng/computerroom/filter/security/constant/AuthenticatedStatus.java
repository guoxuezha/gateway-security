package com.qianmeng.computerroom.filter.security.constant;

/**
 * @author 郭超
 * Date:2020-09-29 11:33
 * Description: 认证结果状态码
 */
public interface AuthenticatedStatus {

    /**
     * 第一次登陆成功,特用于成功后存放用户详细信息
     */
    int FIRST_LOGIN_SUCCESS = 0;

    /**
     * 认证成功
     */
    int AUTHENTICATION_SUCCESS = 1;

    /**
     * 用户名不存在
     */
    int USERNAME_NOT_EXSIT = 2;

    /**
     * 密码不正确
     */
    int PASSWORD_NOT_MATCH = 3;

    /**
     * Login请求类型不为JSON
     */
    int LOGIN_CONTENT_TYPE_NOT_JSON = 4;

    /**
     * 未能从requestBody中获取用户名
     */
    int NULL_USERNAME = 5;

    /**
     * 未知认证异常
     */
    int UNKNOWN_EXCEPTION = 6;

    //========================以下为Token认证的异常==========================

    /**
     * 未能从header中获取Token
     */
    int NULL_TOKEN = 7;

    /**
     * 无效TOKEN,无法从中获取Token
     */
    int INVALID_TOKEN = 8;

    /**
     * Token已过期
     */
    int TOKEN_EXPIRED = 9;

}
