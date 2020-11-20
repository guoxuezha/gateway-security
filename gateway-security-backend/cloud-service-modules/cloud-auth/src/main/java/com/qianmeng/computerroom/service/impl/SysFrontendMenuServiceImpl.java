package com.qianmeng.computerroom.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qianmeng.computerroom.mapper.SysFrontendMenuMapper;
import com.qianmeng.computerroom.mapper.SysRoleFrontendMenuMapper;
import com.qianmeng.computerroom.po.SysFrontendMenu;
import com.qianmeng.computerroom.po.SysRoleFrontendMenu;
import com.qianmeng.computerroom.service.SysFrontendMenuService;
import com.qianmeng.computerroom.util.resulthelper.ResponseResult;
import com.qianmeng.computerroom.vo.SysFrontendVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

import static com.qianmeng.computerroom.config.AuthExceptionConfig.menuCodeRepeatException;

/**
 * (SysFrontendMenuTable)表服务实现类
 *
 * @author qianmeng
 * @since 2020-08-12 14:31:50
 */
@Service("sysFrontendMenuService")
@Slf4j
@CacheConfig(cacheNames = "auth_frontMenu")
public class SysFrontendMenuServiceImpl extends ServiceImpl<SysFrontendMenuMapper, SysFrontendMenu> implements SysFrontendMenuService {

    @Resource
    private SysFrontendMenuMapper menuMapper;

    @Resource
    private SysRoleFrontendMenuMapper roleMenuMapper;

    @Override
    public List<SysFrontendMenu> selectAllMenu() {
        return menuMapper.selectAllMenu();
    }

    @Override
    public ResponseResult selectByPage(Integer page, Integer limit, String menuName) {
        Page pages = new Page(page, limit);
        menuName = "%" + menuName + "%";
        List<SysFrontendVo> menuList = menuMapper.selectMenuListByPage(pages, menuName);
        int total = (int) pages.getTotal();
        return ResponseResult.success(menuList, total);
    }

    @Override
    @Cacheable(key = "#roleId")
    public List<SysFrontendMenu> selectByRoleId(Integer roleId) {
        return menuMapper.selectByRoleId(roleId);
    }

    @Override
    @CacheEvict(key = "#roleId")
    @Transactional(rollbackForClassName = "RuntimeException")
    public void distributeMenu(Integer roleId, String menuIds) {
        // 先删除原有的菜单集合
        LambdaQueryWrapper<SysRoleFrontendMenu> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(SysRoleFrontendMenu::getRoleId, roleId);
        roleMenuMapper.delete(queryWrapper);

        // 再添加新选的菜单集合
        SysRoleFrontendMenu roleMenu = new SysRoleFrontendMenu();
        roleMenu.setRoleId(roleId);
        String[] menuIdsStr = menuIds.split(",");
        if (menuIdsStr.length > 0) {
            for (String menuIdStr : menuIdsStr) {
                if (menuIdStr != null && !"".equals(menuIdStr.trim())) {
                    roleMenu.setMenuId(Integer.parseInt(menuIdStr));
                    roleMenuMapper.insert(roleMenu);
                }
            }
        }
    }

    @Override
    @Transactional(rollbackForClassName = "RuntimeException")
    public SysFrontendVo insert(SysFrontendMenu menu) throws Exception {
        LambdaQueryWrapper<SysFrontendMenu> queryWrapper1 = Wrappers.lambdaQuery();
        queryWrapper1.eq(SysFrontendMenu::getMenuCodeName, menu.getMenuCodeName());
        SysFrontendMenu existFrontendMenu = menuMapper.selectOne(queryWrapper1);
        if (existFrontendMenu != null) {
            throw new Exception(menuCodeRepeatException());
        }
        // isDeleted = 0
        menu.setIsDeleted(0);
        // 判断sort
        String menuParent;
        Integer menuPid = menu.getMenuPid();
        if (menuPid == 0) {
            menu.setMenuSort(1);
            menuParent = "";
        } else {
            LambdaQueryWrapper<SysFrontendMenu> queryWrapper = Wrappers.lambdaQuery();
            queryWrapper.eq(SysFrontendMenu::getMenuId, menuPid);
            SysFrontendMenu parentMenu = menuMapper.selectOne(queryWrapper);
            Integer parentSort = parentMenu.getMenuSort();
            menu.setMenuSort(parentSort + 1);
            menuParent = parentMenu.getMenuName();
        }
        menuMapper.insert(menu);
        return poToVo(menu, menuParent);
    }

    @Override
    @Transactional(rollbackForClassName = "RuntimeException")
    public SysFrontendVo updateByMenuId(SysFrontendMenu menu) {
        menuMapper.updateById(menu);
        // 获取menuParent
        Integer menuPid = menu.getMenuPid();
        String parentMenuName;
        if (menuPid == 0) {
            parentMenuName = "";
        } else {
            LambdaQueryWrapper<SysFrontendMenu> queryWrapper = Wrappers.lambdaQuery();
            queryWrapper.eq(SysFrontendMenu::getMenuId, menuPid);
            SysFrontendMenu parentMenu = menuMapper.selectOne(queryWrapper);
            parentMenuName = parentMenu.getMenuName();
        }

        return poToVo(menu, parentMenuName);
    }

    /**
     * 将修改/新增后的menuPo类转为menuVo类,并未其parentName字段赋值
     *
     * @param menu       菜单
     * @param menuParent 父节点名称
     * @return vo
     */
    private static SysFrontendVo poToVo(SysFrontendMenu menu, String menuParent) {
        SysFrontendVo menuVo = new SysFrontendVo();
        BeanUtil.copyProperties(menu, menuVo);
        menuVo.setParentName(menuParent);
        log.info("menuVo = " + menuVo);
        return menuVo;
    }
}