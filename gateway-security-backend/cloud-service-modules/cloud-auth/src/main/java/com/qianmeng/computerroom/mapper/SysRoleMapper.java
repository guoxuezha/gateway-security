package com.qianmeng.computerroom.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qianmeng.computerroom.po.SysRole;
import com.qianmeng.computerroom.vo.SysRoleAndPermissionVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * (SysRole)表数据库访问层
 *
 * @author qianmeng
 * @since 2020-08-12 14:31:50
 */
public interface SysRoleMapper extends BaseMapper<SysRole> {

    /**
     * 根据用户名查询拥有的角色ID
     *
     * @param userName 用户名
     * @return 角色ID集合
     */
    List<String> getRolesByUserName(@Param("userName") String userName);

    /**
     * 根据userId查询roleId集合
     *
     * @param userId 用户ID
     * @return 角色ID集合
     */
    List<SysRole> selectByUserId(Integer userId);

    /**
     * 根据roleId找用户以及用户是否被设置在某个角色上,用在显示在用于角色设置处
     *
     * @param roleId 角色ID
     * @return 跟该角色相关联的用户集合
     */
    List<SysRoleAndPermissionVo> getRoleAndUserList(@Param("roleId") String roleId);

    /**
     * 根据roleId找菜单
     *
     * @param roleId 角色ID
     * @return 该角色可访问的菜单集合
     */
    List<SysRoleAndPermissionVo> getRoleAndMenuList(@Param("roleId") String roleId);

    /**
     * 根据roleId找API
     *
     * @param roleId 角色ID
     * @return 该角色可访问的API集合
     */
    List<SysRoleAndPermissionVo> getRoleAndApiList(@Param("roleId") String roleId);
}