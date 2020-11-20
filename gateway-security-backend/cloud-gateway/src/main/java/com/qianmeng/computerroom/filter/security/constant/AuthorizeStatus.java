package com.qianmeng.computerroom.filter.security.constant;

/**
 * @author 郭超
 * Date:2020-09-29 11:25
 * Description: 授权状态码
 */
public interface AuthorizeStatus {

    /**
     * 授权成功
     */
    int AUTHORIZE_SUCCESS = 1;

    /**
     * 权限不足
     */
    int AUTHORIZE_INSUFFICIENT = 2;

    /**
     * 空权限
     */
    int NULL_AUTHORIZATION = 3;
}
