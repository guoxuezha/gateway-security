package com.qianmeng.computerroom.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qianmeng.computerroom.mapper.SysRoleBackendApiMapper;
import com.qianmeng.computerroom.mapper.SysRoleMapper;
import com.qianmeng.computerroom.mapper.SysUserRoleMapper;
import com.qianmeng.computerroom.po.SysRole;
import com.qianmeng.computerroom.po.SysRoleBackendApi;
import com.qianmeng.computerroom.po.SysUserRole;
import com.qianmeng.computerroom.service.SysRoleService;
import com.qianmeng.computerroom.util.resulthelper.ResponseResult;
import com.qianmeng.computerroom.vo.SysRoleAndPermissionVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

import static com.qianmeng.computerroom.config.AuthExceptionConfig.roleNameRepeatException;

/**
 * (SysRoleTable)表服务实现类
 *
 * @author qianmeng
 * @since 2020-08-12 14:31:50
 */
@Slf4j
@Service("sysRoleService")
@CacheConfig(cacheNames = "auth_role")
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {

    @Resource
    private SysRoleMapper roleMapper;

    @Resource
    private SysUserRoleMapper userRoleMapper;

    @Resource
    private SysRoleBackendApiMapper roleApiMapper;

    @Override
    public ResponseResult selectByPage(Page page, String roleName) {
        LambdaQueryWrapper<SysRole> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(SysRole::getIsDeleted, 0)
                .like(SysRole::getRoleName, roleName);
        IPage thisPage = roleMapper.selectPage(page, queryWrapper);
        long total = thisPage.getTotal();
        List roleList = thisPage.getRecords();
        return ResponseResult.success(roleList, (int) total);
    }

    @Override
    public List<SysRole> selectByUserId(Integer userId) {
        return roleMapper.selectByUserId(userId);
    }

    @Override
    @Transactional(rollbackForClassName = "RuntimeException")
    public SysRole insert(SysRole sysRole) throws Exception {
        LambdaQueryWrapper<SysRole> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(SysRole::getRoleName, sysRole.getRoleName());
        SysRole existSysRole = roleMapper.selectOne(queryWrapper);
        if (existSysRole != null) {
            throw new Exception(roleNameRepeatException());
        }
        // 角色信息直接新增即可
        roleMapper.insert(sysRole);

        // 再自动生成一条与全API关联的记录,以保证拥有该角色的用户可登陆
        SysRoleBackendApi roleApi = new SysRoleBackendApi();
        roleApi.setApiId(1);
        roleApi.setRoleId(sysRole.getRoleId());
        roleApiMapper.insert(roleApi);
        return sysRole;
    }

    @Override
    @Transactional(rollbackForClassName = "RuntimeException")
    public void distributeRoles(Integer userId, String roleIds) {
        // 先删除原有的user_role集合
        LambdaQueryWrapper<SysUserRole> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(SysUserRole::getUserId, userId);
        userRoleMapper.delete(queryWrapper);

        // 再插入新绑定的user_role关系
        SysUserRole sysUserRole = new SysUserRole();
        sysUserRole.setUserId(userId);

        String[] roleIdsStr = roleIds.split(",");
        for (String s : roleIdsStr) {
            if (s != null && !"".equals(s.trim())) {
                Integer roleId = Integer.parseInt(s);
                sysUserRole.setRoleId(roleId);
                userRoleMapper.insert(sysUserRole);
            }
        }
    }

    /**
     * 根据roleId找用户以及用户是否被设置在某个角色上,用在显示在用于角色设置处
     *
     * @param roleId 角色ID
     * @return 拥有该角色的用户集合
     */
    @Override
    public List<SysRoleAndPermissionVo> getRoleAndUserList(String roleId) {
        return this.baseMapper.getRoleAndUserList(roleId);
    }

    /**
     * 根据roleId找菜单
     */
    @Override
    public List<SysRoleAndPermissionVo> getRoleAndMenuList(String roleId) {
        return this.baseMapper.getRoleAndMenuList(roleId);
    }

    /**
     * 根据roleId找API
     */
    @Override
    public List<SysRoleAndPermissionVo> getRoleAndApiList(String roleId) {
        return this.baseMapper.getRoleAndApiList(roleId);
    }

}