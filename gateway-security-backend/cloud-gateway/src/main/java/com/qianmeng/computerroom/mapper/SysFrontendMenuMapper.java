package com.qianmeng.computerroom.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qianmeng.computerroom.po.SysFrontendMenu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 郭超
 * Date:2020-09-29 16:51
 * Description:
 */
@Mapper
public interface SysFrontendMenuMapper extends BaseMapper<SysFrontendMenu> {

    /**
     * 根据登录账号,获得前端展现的菜单
     * 控制前端菜单的权限
     *
     * @param username 用户名
     * @return 可访问的菜单
     */
    List<SysFrontendMenu> getMenusByUserName(@Param("username") String username);

}
