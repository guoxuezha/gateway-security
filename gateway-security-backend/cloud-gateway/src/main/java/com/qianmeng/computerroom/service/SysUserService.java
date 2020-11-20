package com.qianmeng.computerroom.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qianmeng.computerroom.po.SysUser;

import java.util.Map;

/**
 * @author: 郭超
 * Date:2020-09-10 14:06
 * Description:
 */
public interface SysUserService extends IService<SysUser> {

    /**
     * 根据userName查询用户信息
     *
     * @param userName 用户名
     * @return 用户信息
     */
    SysUser getUserByUserName(String userName);

    /**
     * 个性化验证登录
     *
     * @param username    账号
     * @param rawPassword 原始密码
     * @return 是否存在
     * @throws Exception 自定义各种验证失败后的异常提醒信息
     */
    boolean checkLogin(String username, String rawPassword) throws Exception;

    /**
     * 根据token获取user信息
     *
     * @param token token
     * @return userMap
     */
    Map<String, Object> getUserInfo(String token);


}
