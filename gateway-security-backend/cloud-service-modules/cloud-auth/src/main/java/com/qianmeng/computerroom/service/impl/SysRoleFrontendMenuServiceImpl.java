package com.qianmeng.computerroom.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qianmeng.computerroom.mapper.SysRoleFrontendMenuMapper;
import com.qianmeng.computerroom.po.SysRoleFrontendMenu;
import com.qianmeng.computerroom.service.SysRoleFrontendMenuService;
import com.qianmeng.computerroom.vo.SysRoleAndPermissionVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

/**
 * (SysRoleFrontendMenuTable)表服务实现类
 *
 * @author qianmeng
 * @since 2020-08-12 14:31:50
 */
@Service("sysRoleFrontendMenuService")
@Slf4j
@CacheConfig(cacheNames = "auth_roleMenu")
public class SysRoleFrontendMenuServiceImpl extends ServiceImpl<SysRoleFrontendMenuMapper, SysRoleFrontendMenu> implements SysRoleFrontendMenuService {

    /**
     * 批量保存角色-菜单表
     * 该方法内有两个操作,先删除、后批量新增,因此存在事务控制
     *
     * @param roleId
     * @param sysRoleAndPermissionVos
     * @return
     */
    @Override
    @Transactional(rollbackForClassName = "RuntimeException")
    public boolean saveRoleMenu(String roleId, SysRoleAndPermissionVo... sysRoleAndPermissionVos) {

        log.info("roleId = " + roleId);
        //先删除数据
        this.delRoleId(roleId);
        //
        if (sysRoleAndPermissionVos != null) {
            Set<SysRoleFrontendMenu> set = new HashSet<>();
            SysRoleFrontendMenu roleFrontendMenu = null;
            for (SysRoleAndPermissionVo roleVo : sysRoleAndPermissionVos) {
                roleFrontendMenu = new SysRoleFrontendMenu();
                //存储roleID和FrontendMenuId到多对对的中间表
                roleFrontendMenu.setRoleId(roleVo.getRoleId());
                roleFrontendMenu.setMenuId(roleVo.getId());
                set.add(roleFrontendMenu);
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
        LambdaQueryWrapper<SysRoleFrontendMenu> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(SysRoleFrontendMenu::getRoleId, roleId);
        return this.remove(lambdaQueryWrapper);
    }
}