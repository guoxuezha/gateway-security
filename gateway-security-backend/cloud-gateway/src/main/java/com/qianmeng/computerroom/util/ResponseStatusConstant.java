package com.qianmeng.computerroom.util;

/**
 * Date: 7/19
 * Description: <描述>
 * @author qianmeng
 */
public interface ResponseStatusConstant {

    /**
     * 响应状态码,1表示成功
     */
    int RESPONSE_STATUS_SUCCESS = 1;

    /**
     * 响应状态码,2表示失败
     */
    int RESPONSE_STATUS_FAIL = 2;

    /**
     * 响应状态码,3表示没有权限
     */
    int RESPONSE_STATUS_NO_PERMISSION = 3;

    int SUCCESS_CODE = 20000;

    int ERR_CODE = 30000;

    int DEFAULT_TOTAL = 1;

}
