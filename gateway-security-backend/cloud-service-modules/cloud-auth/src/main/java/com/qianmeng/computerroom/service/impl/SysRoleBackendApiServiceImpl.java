package com.qianmeng.computerroom.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qianmeng.computerroom.mapper.SysRoleBackendApiMapper;
import com.qianmeng.computerroom.po.SysRoleBackendApi;
import com.qianmeng.computerroom.service.SysRoleBackendApiService;
import com.qianmeng.computerroom.vo.SysRoleAndPermissionVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

/**
 * (SysRoleBackendApiTable)表服务实现类
 *
 * @author qianmeng
 * @since 2020-08-12 14:31:50
 */
@Service("sysRoleBackendApiService")
@Slf4j
@CacheConfig(cacheNames = "auth_backendApi")
public class SysRoleBackendApiServiceImpl extends ServiceImpl<SysRoleBackendApiMapper, SysRoleBackendApi> implements SysRoleBackendApiService {

    /**
     * 批量保存角色-API表
     * 该方法内有两个操作,先删除、后批量新增,因此存在事务控制
     *
     * @param roleId
     * @param sysRoleAndPermissionVos
     * @return
     */
    @Override
    @Transactional(rollbackForClassName = "RuntimeException")
    public boolean saveRoleAip(String roleId, SysRoleAndPermissionVo... sysRoleAndPermissionVos) {

        log.info("roleId = " + roleId);

        //先删除数据
        this.delRoleId(roleId);
        //
        if (sysRoleAndPermissionVos != null) {
            Set<SysRoleBackendApi> set = new HashSet<>();
            SysRoleBackendApi roleBackendApi = null;
            for (SysRoleAndPermissionVo roleVo : sysRoleAndPermissionVos) {
                roleBackendApi = new SysRoleBackendApi();
                //存储roleID和BackendApiId到多对对的中间表
                roleBackendApi.setRoleId(roleVo.getRoleId());
                roleBackendApi.setApiId(roleVo.getId());
                set.add(roleBackendApi);
            }
            log.info("set = " + set);
            //再批量保存
            return this.saveBatch(set);
        }
        return false;
    }

    /**
     * 根据RoleId删除
     *
     * @param roleId
     * @return
     */
    private boolean delRoleId(String roleId) {
        LambdaQueryWrapper<SysRoleBackendApi> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(SysRoleBackendApi::getRoleId, roleId);
        return this.remove(lambdaQueryWrapper);
    }
}