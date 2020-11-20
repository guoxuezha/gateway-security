package com.qianmeng.computerroom.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qianmeng.computerroom.po.SysFrontendMenu;

import java.util.List;

/**
 * @author: 郭超
 * Date:2020-09-29 16:49
 * Description:
 */
public interface SysFrontendMenuService extends IService<SysFrontendMenu> {

    /**
     * 根据登录账号,获得前端可以展现的菜单,控制前端菜单的权限
     *
     * @param username 用户名
     * @return 可展现菜单集合
     */
    List<SysFrontendMenu> getMenusByUserName(String username);

}
