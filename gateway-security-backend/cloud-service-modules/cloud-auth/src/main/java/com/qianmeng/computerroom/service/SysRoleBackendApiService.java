package com.qianmeng.computerroom.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qianmeng.computerroom.po.SysRoleBackendApi;
import com.qianmeng.computerroom.vo.SysRoleAndPermissionVo;

/**
 * (SysRoleBackendApiTable)表服务接口
 * @author qianmeng
 * @since 2020-08-12 14:31:50
 */
public interface SysRoleBackendApiService extends IService<SysRoleBackendApi> {

    /**
     * 批量保存角色-API表
     * 该方法内有两个操作,先删除、后批量新增,因此存在事务控制
     * @param roleId
     * @param sysRoleAndPermissionVos
     * @return
     */
    boolean saveRoleAip(String roleId, SysRoleAndPermissionVo... sysRoleAndPermissionVos);
}