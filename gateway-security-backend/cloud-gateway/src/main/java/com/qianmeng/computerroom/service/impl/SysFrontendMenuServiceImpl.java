package com.qianmeng.computerroom.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qianmeng.computerroom.mapper.SysFrontendMenuMapper;
import com.qianmeng.computerroom.po.SysFrontendMenu;
import com.qianmeng.computerroom.service.SysFrontendMenuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 郭超
 * Date:2020-09-29 16:50
 * Description:
 */
@Service("sysFrontendMenuService")
@Slf4j
@CacheConfig(cacheNames = "gateway_frontMenu")
public class SysFrontendMenuServiceImpl extends ServiceImpl<SysFrontendMenuMapper, SysFrontendMenu> implements SysFrontendMenuService {

    /**
     * 根据登录账号,获得前端展现的菜单
     * 控制前端菜单的权限
     *
     * @param username 登陆账号
     * @return 可访问菜单集合
     */
    @Override
    @Cacheable(key = "#username")
    public List<SysFrontendMenu> getMenusByUserName(String username) {
        return this.baseMapper.getMenusByUserName(username);
    }
}
