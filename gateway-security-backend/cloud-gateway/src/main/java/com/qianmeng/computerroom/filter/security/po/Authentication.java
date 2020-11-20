package com.qianmeng.computerroom.filter.security.po;

import lombok.Data;

/**
 * @author 郭超
 * Date:2020-09-29 11:13
 * Description: 自定义认证对象
 */
@Data
public class Authentication {

    /**
     * 认证状态
     */
    private int authenticatedStatus;

    /**
     * 正在认证的用户名
     */
    private String userName;
}
