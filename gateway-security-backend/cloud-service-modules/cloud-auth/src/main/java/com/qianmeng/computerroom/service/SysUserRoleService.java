package com.qianmeng.computerroom.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qianmeng.computerroom.po.SysUserRole;
import com.qianmeng.computerroom.vo.SysRoleAndPermissionVo;

/**
 * (SysRoleUserTable)表服务接口
 *
 * @author qianmeng
 * @since 2020-08-12 14:31:50
 */
public interface SysUserRoleService extends IService<SysUserRole> {

    /**
     * 批量保存角色-用户表
     * 该方法内有两个操作,先删除、后批量新增,因此存在事务控制
     *
     * @param roleId                  给用户绑定的角色ID集合
     * @param sysRoleAndPermissionVos 将获得该角色的的用户集合
     * @return 执行结果
     */
    boolean saveRoleUser(String roleId, SysRoleAndPermissionVo... sysRoleAndPermissionVos);

}