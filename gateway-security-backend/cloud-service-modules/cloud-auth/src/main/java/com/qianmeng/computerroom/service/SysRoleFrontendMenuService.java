package com.qianmeng.computerroom.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qianmeng.computerroom.po.SysRoleFrontendMenu;
import com.qianmeng.computerroom.vo.SysRoleAndPermissionVo;

/**
 * (SysRoleFrontendMenuTable)表服务接口
 * @author qianmeng
 * @since 2020-08-12 14:31:50
 */
public interface SysRoleFrontendMenuService extends IService<SysRoleFrontendMenu> {

    /**
     * 批量保存角色-菜单表
     * 该方法内有两个操作,先删除、后批量新增,因此存在事务控制
     * @param roleId
     * @param sysRoleAndPermissionVos
     * @return
     */
    boolean saveRoleMenu(String roleId, SysRoleAndPermissionVo... sysRoleAndPermissionVos);
}