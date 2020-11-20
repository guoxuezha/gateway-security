package com.qianmeng.computerroom.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qianmeng.computerroom.po.SysRole;

import java.util.List;

/**
 * @author 郭超
 * Date:2020-09-10 14:32
 * Description:
 */
public interface SysRoleService extends IService<SysRole> {

    /**
     * 根据用户名称查询角色
     *
     * @param userName 用户名
     * @return 该用户的角色集合
     */
    List<String> getRolesByUserName(String userName);

}
