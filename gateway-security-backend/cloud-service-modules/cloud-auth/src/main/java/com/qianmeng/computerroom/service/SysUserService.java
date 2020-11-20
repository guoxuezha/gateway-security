package com.qianmeng.computerroom.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qianmeng.computerroom.po.SysUser;
import com.qianmeng.computerroom.util.resulthelper.ResponseResult;

import java.util.Map;

/**
 * MP配置了通用接口IService的SysUser业务接口
 *
 * @author qianmeng
 */
public interface SysUserService extends IService<SysUser> {

    /**
     * 通过账号查询用户
     *
     * @param username 账号
     * @return 用户信息
     */
    SysUser getUserByUserName(String username);

    /**
     * 注册
     *
     * @param sysUser 用户注册类
     * @return 注册结果
     * @throws Exception 自定义注册失败时的异常提醒
     */
    SysUser register(SysUser sysUser) throws Exception;

    /**
     * 根据token获取user信息
     *
     * @param token token
     * @return userMap
     */
    Map<String, Object> getUserInfo(String token);

    /**
     * 根据userId删除用户信息
     *
     * @param userId 用户ID
     */
    void delete(Integer userId);

    /**
     * 分页查询用户列表
     *
     * @param page      当前页
     * @param limit     每页条数
     * @param name      用户名
     * @param roleId    角色ID
     * @param companyId 公司ID
     * @return 用户集合
     */
    ResponseResult selectByPage(Integer page, Integer limit, String name, Integer roleId, Integer companyId);

}
