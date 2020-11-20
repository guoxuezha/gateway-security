package com.qianmeng.computerroom.util;

import lombok.Data;

/**
 * @author qianmeng
 */
@Data
public class ResponseResult {

    /**
     * 状态码
     */
    private int status;

    /**
     * 消息
     */
    private String message;

    /**
     * 返回数据
     */
    private Object data;

    /**
     * 另一个状态码
     */
    private int code;

    /**
     * 返回分页查询时查到的总数据条数
     */
    private int total;

    private ResponseResult(int status, String message, Object data, Integer code, int total) {
        this.status = status;
        this.message = message;
        this.data = data;
        this.code = code;
        this.total = total;
    }

    /**
     * 成功
     *
     * @param data 封装好的回调对象
     * @return date
     */
    public static ResponseResult success(Object data) {
        return new ResponseResult(ResponseStatusConstant.RESPONSE_STATUS_SUCCESS, "success", data,
                ResponseStatusConstant.SUCCESS_CODE, ResponseStatusConstant.DEFAULT_TOTAL);
    }

    /**
     * 分页查询专用Ajax返回方法
     * @param data list集合
     * @param total 返回的数据总条数
     * @return
     */
    public static ResponseResult success(Object data, int total) {
        return new ResponseResult(ResponseStatusConstant.RESPONSE_STATUS_SUCCESS, "success", data, ResponseStatusConstant.SUCCESS_CODE, total);
    }

    public static ResponseResult success(String message) {
        return new ResponseResult(ResponseStatusConstant.RESPONSE_STATUS_SUCCESS, message, null, ResponseStatusConstant.SUCCESS_CODE, ResponseStatusConstant.DEFAULT_TOTAL);
    }

    public static ResponseResult success() {
        return new ResponseResult(ResponseStatusConstant.RESPONSE_STATUS_SUCCESS, "success", null, ResponseStatusConstant.SUCCESS_CODE, ResponseStatusConstant.DEFAULT_TOTAL);
    }

    /**
     * 失败
     *
     * @return 封装好的执行失败消息对象
     */
    public static ResponseResult fail() {
        return new ResponseResult(ResponseStatusConstant.RESPONSE_STATUS_FAIL, "fail", null, ResponseStatusConstant.ERR_CODE, ResponseStatusConstant.DEFAULT_TOTAL);
    }

    public static ResponseResult fail(Object data) {
        return new ResponseResult(ResponseStatusConstant.RESPONSE_STATUS_FAIL, "fail", data, ResponseStatusConstant.ERR_CODE, ResponseStatusConstant.DEFAULT_TOTAL);
    }

    public static ResponseResult fail(String message) {
        return new ResponseResult(ResponseStatusConstant.RESPONSE_STATUS_FAIL, message, null, ResponseStatusConstant.ERR_CODE, ResponseStatusConstant.DEFAULT_TOTAL);
    }

    /**
     * 根据result自动返回success或fail
     *
     * @param result 方法执行结果
     * @return 成功/失败的json格式数据
     */
    public static ResponseResult returnStatus(int result) {
        if (result != 0) {
            return ResponseResult.success(result);
        } else {
            return ResponseResult.fail();
        }
    }

}
