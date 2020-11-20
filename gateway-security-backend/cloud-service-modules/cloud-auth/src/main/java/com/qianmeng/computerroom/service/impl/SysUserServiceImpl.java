package com.qianmeng.computerroom.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qianmeng.computerroom.components.BCryptPasswordEncoderUtil;
import com.qianmeng.computerroom.mapper.CompanyMapper;
import com.qianmeng.computerroom.mapper.SysFrontendMenuMapper;
import com.qianmeng.computerroom.mapper.SysUserMapper;
import com.qianmeng.computerroom.mapper.SysUserRoleMapper;
import com.qianmeng.computerroom.po.Company;
import com.qianmeng.computerroom.po.SysFrontendMenu;
import com.qianmeng.computerroom.po.SysUser;
import com.qianmeng.computerroom.po.SysUserRole;
import com.qianmeng.computerroom.redis.RedisUtil;
import com.qianmeng.computerroom.service.SysUserService;
import com.qianmeng.computerroom.util.resulthelper.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.qianmeng.computerroom.config.AuthExceptionConfig.userNameRepeatException;

/**
 * 用户服务实现类,同样继承了MP的的通用实现类
 *
 * @author qianmeng
 */
@Slf4j
@Service("SysUserService")
@CacheConfig(cacheNames = "auth_user")
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    @Resource
    private BCryptPasswordEncoderUtil bCryptPasswordEncoderUtil;

    @Resource
    private SysUserMapper sysUserMapper;

    @Resource
    private SysUserRoleMapper userRoleMapper;

    @Resource
    private SysFrontendMenuMapper menuMapper;

    @Resource
    private CompanyMapper companyMapper;

    @Resource
    private RedisUtil redisUtil;

    /**
     * 用户激活状态
     */
    private static final int IS_DELETED = 0;

    /**
     * 通过账号查询用户
     *
     * @param username 用户名
     * @return 查到的用户对象
     */
    @Override
    public SysUser getUserByUserName(String username) {
        LambdaQueryWrapper<SysUser> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        // 查询条件：全匹配账号名,和状态为1的账号
        lambdaQueryWrapper
                .eq(SysUser::getUserName, username)
                .eq(SysUser::getIsDeleted, IS_DELETED);

        // 用getOne查询一个对象出来,第二个参数false可以保证查询到多条数据时不报错,只提取列表中的第一条
        return this.getOne(lambdaQueryWrapper, false);
    }

    /**
     * 用户注册
     *
     * @param sysUser 新注册的用户信息
     * @return 注册是否成功
     * @throws Exception 注册失败的提示信息
     */
    @Override
    @Transactional(rollbackForClassName = "RuntimeException")
    public SysUser register(SysUser sysUser) throws Exception {
        if (sysUser != null) {
            SysUser f = this.getUserByUserName(sysUser.getUserName());
            if (f != null) {
                throw new Exception(userNameRepeatException());
            }
            // 先把信息存入sysUser表
            sysUser.setPassword(bCryptPasswordEncoderUtil.encode(sysUser.getPassword()));
            sysUser.setIsDeleted(IS_DELETED);
            String roleIdsStr = sysUser.getUserRemark();
            sysUser.setUserRemark(null);
            sysUserMapper.insert(sysUser);
            // 再建立user_role关联关系
            if (roleIdsStr != null) {
                String[] roleIds = roleIdsStr.split(",");
                SysUserRole userRole = new SysUserRole();
                userRole.setUserId(sysUser.getUserId());
                for (String id : roleIds) {
                    Integer roleId = Integer.parseInt(id);
                    userRole.setRoleId(roleId);
                    userRole.setIsDeleted(0);
                    userRoleMapper.insert(userRole);
                }
            }
            return sysUser;
        } else {
            throw new Exception("错误消息：用户对象为空！");
        }
    }

    @Override
    public Map<String, Object> getUserInfo(String token) {
        // 获取用户名
        String userName = (String) redisUtil.get(token);
        // 获取菜单
        List<SysFrontendMenu> menuList = menuMapper.getMenusByUserName(userName);
        // 获取公司信息
        Company company = companyMapper.selectByUserName(userName);
        Map<String, Object> map = new HashMap<>(2);
        map.put("userName", userName);
        map.put("menus", menuList);
        map.put("company", company);
        return map;
    }

    @Override
    @Transactional(rollbackForClassName = "RuntimeException")
    public void delete(Integer userId) {
        // 先删除用户
        LambdaQueryWrapper<SysUser> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(SysUser::getUserId, userId);
        sysUserMapper.delete(queryWrapper);

        // 再删除用户-角色关联关系
        LambdaQueryWrapper<SysUserRole> queryWrapper2 = Wrappers.lambdaQuery();
        queryWrapper2.eq(SysUserRole::getUserId, userId);
        userRoleMapper.delete(queryWrapper2);
    }

    /**
     * MybatisPlus牛逼啊,给自己写的sql接口里加个IPage参数就可以实现分页
     * 而且非侵入式的设计理念并不妨碍使用Xml写sql语句实现业务需求
     *
     * @param page   当前页
     * @param limit  每页条数
     * @param name   用户名
     * @param roleId 角色ID
     * @return 用户集合
     */
    @Override
    public ResponseResult selectByPage(Integer page, Integer limit, String name, Integer roleId, Integer companyId) {
        Page pages = new Page(page, limit);
        name = "%" + name + "%";
        List<SysUser> userList = sysUserMapper.selectUserAndRolesByPage(pages, name, roleId, companyId);
        int total = (int) pages.getTotal();
        return ResponseResult.success(userList, total);
    }

}
