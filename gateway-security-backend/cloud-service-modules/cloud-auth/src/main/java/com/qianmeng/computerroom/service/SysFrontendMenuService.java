package com.qianmeng.computerroom.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qianmeng.computerroom.po.SysFrontendMenu;
import com.qianmeng.computerroom.util.resulthelper.ResponseResult;
import com.qianmeng.computerroom.vo.SysFrontendVo;

import java.util.List;

/**
 * (SysFrontendMenu)表服务接口
 *
 * @author qianmeng
 * @since 2020-08-12 14:31:50
 */
public interface SysFrontendMenuService extends IService<SysFrontendMenu> {

    /**
     * 全查menu
     *
     * @return 全部menu
     */
    List<SysFrontendMenu> selectAllMenu();

    /**
     * 分页查询菜单项
     *
     * @param page     当前页数
     * @param limit    每页条数
     * @param menuName 菜单项名,提供模糊查询
     * @return 菜单集合
     */
    ResponseResult selectByPage(Integer page, Integer limit, String menuName);

    /**
     * 根据roleId查询该角色可访问的菜单项
     *
     * @param roleId 角色ID
     * @return 菜单项集合
     */
    List<SysFrontendMenu> selectByRoleId(Integer roleId);

    /**
     * 为角色分配可访问菜单
     *
     * @param roleId  绝嗣ID
     * @param menuIds 菜单项ID
     */
    void distributeMenu(Integer roleId, String menuIds);

    /**
     * 新增菜单项
     *
     * @param menu 前台传来的menu对象
     * @return 处理完的菜单对象
     */
    SysFrontendVo insert(SysFrontendMenu menu) throws Exception;

    /**
     * 修改菜单项
     *
     * @param menu 修改后的内容
     * @return vo
     */
    SysFrontendVo updateByMenuId(SysFrontendMenu menu);

}