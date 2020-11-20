package com.qianmeng.computerroom.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.qianmeng.computerroom.po.SysRole;
import com.qianmeng.computerroom.util.resulthelper.ResponseResult;
import com.qianmeng.computerroom.vo.SysRoleAndPermissionVo;

import java.util.List;

/**
 * (SysRole)表服务接口
 *
 * @author qianmeng
 * @since 2020-08-12 14:31:50
 */
public interface SysRoleService extends IService<SysRole> {

    /**
     * 角色表分页查询
     *
     * @param page     page对象
     * @param roleName 角色名
     * @return 角色列表
     */
    ResponseResult selectByPage(Page page, String roleName);

    /**
     * 根据userId查询roleId集合
     *
     * @param userId 用户ID
     * @return 角色ID集合
     */
    List<SysRole> selectByUserId(Integer userId);

    /**
     * 新增角色
     *
     * @param sysRole 角色信息
     * @return 新增结果
     */
    SysRole insert(SysRole sysRole) throws Exception ;

    /**
     * 为用户分配角色
     *
     * @param userId  用户ID
     * @param roleIds 角色ID集合
     */
    void distributeRoles(Integer userId, String roleIds);

    /**
     * 根据roleId找用户以及用户是否被设置在某个角色上,用在显示在用于角色设置处
     *
     * @param roleId 角色ID
     * @return 拥有该角色的用户集合
     */
    List<SysRoleAndPermissionVo> getRoleAndUserList(String roleId);

    /**
     * 根据roleId找菜单
     *
     * @param roleId 角色ID
     * @return 可访问菜单集合
     */
    List<SysRoleAndPermissionVo> getRoleAndMenuList(String roleId);

    /**
     * 根据roleId找API
     *
     * @param roleId 角色ID
     * @return 可访问API集合
     */
    List<SysRoleAndPermissionVo> getRoleAndApiList(String roleId);
}