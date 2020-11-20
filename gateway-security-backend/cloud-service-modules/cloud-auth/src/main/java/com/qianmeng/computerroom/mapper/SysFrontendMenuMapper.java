package com.qianmeng.computerroom.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.qianmeng.computerroom.po.SysFrontendMenu;
import com.qianmeng.computerroom.vo.SysFrontendVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * (SysFrontendMenu)表数据库访问层
 *
 * @author qianmeng
 * @since 2020-08-12 14:31:50
 */
public interface SysFrontendMenuMapper extends BaseMapper<SysFrontendMenu> {

    /**
     * 全查
     * @return 全部menu
     */
    List<SysFrontendMenu> selectAllMenu();

    /**
     * 直接根据userName查询可访问菜单集合
     *
     * @param menuName 用户名
     * @return 菜单项集合
     */
    int selectCountByMenuName(String menuName);

    /**
     * 分页查询前端菜单（编辑使用）
     *
     * @param page     分页参数
     * @param menuName 模糊查询
     * @return 展示所有菜单项
     */
    List<SysFrontendVo> selectMenuListByPage(IPage page,
                                             @Param("menuName") String menuName);

    /**
     * 根据roleId查询该角色可访问的菜单项
     *
     * @param roleId 角色ID
     * @return 菜单项集合
     */
    List<SysFrontendMenu> selectByRoleId(Integer roleId);

    /**
     * 根据登录账号,获得前端展现的菜单
     * 控制前端菜单的权限
     *
     * @param username 用户名
     * @return 可访问的菜单
     */
    List<SysFrontendMenu> getMenusByUserName(@Param("username") String username);
}