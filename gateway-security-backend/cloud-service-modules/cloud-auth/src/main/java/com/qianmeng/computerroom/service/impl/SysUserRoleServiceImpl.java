package com.qianmeng.computerroom.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qianmeng.computerroom.mapper.SysUserRoleMapper;
import com.qianmeng.computerroom.po.SysUserRole;
import com.qianmeng.computerroom.service.SysUserRoleService;
import com.qianmeng.computerroom.vo.SysRoleAndPermissionVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

/**
 * (SysUserRole)表服务实现类
 *
 * @author qianmeng
 * @since 2020-08-12 14:31:50
 */
@Slf4j
@Service("sysUserRoleService")
@CacheConfig(cacheNames = "auth_userRole")
public class SysUserRoleServiceImpl extends ServiceImpl<SysUserRoleMapper, SysUserRole> implements SysUserRoleService {

    /**
     * 批量保存角色-用户表
     * 该方法内有两个操作,先删除、后批量新增,因此存在事务控制
     *
     * @param roleId                  角色ID
     * @param sysRoleAndPermissionVos 将获得该角色的的用户集合
     * @return 执行结果
     */
    @Override
    @Transactional(rollbackForClassName = "RuntimeException")
    public boolean saveRoleUser(String roleId, SysRoleAndPermissionVo... sysRoleAndPermissionVos) {

        log.info("roleId = " + roleId);
        // 先删除数据
        this.delRoleId(roleId);
        if (sysRoleAndPermissionVos != null) {
            Set<SysUserRole> set = new HashSet<>();
            SysUserRole roleUser;
            for (SysRoleAndPermissionVo roleVo : sysRoleAndPermissionVos) {
                roleUser = new SysUserRole();
                // 存储roleID和userID到多对对的中间表
                roleUser.setRoleId(roleVo.getRoleId());
                roleUser.setUserId(roleVo.getId());
                set.add(roleUser);
            }
            log.info("set = " + set);
            // 再批量保存
            return this.saveBatch(set);
        }
        return false;
    }

    /**
     * 根据RoleId删除
     *
     * @param roleId 要删除的
     * @return 删除结果
     */
    private boolean delRoleId(String roleId) {
        LambdaQueryWrapper<SysUserRole> lambdaQueryWrapper = Wrappers.lambdaQuery();
        lambdaQueryWrapper.eq(SysUserRole::getRoleId, roleId);
        return this.remove(lambdaQueryWrapper);
    }
}