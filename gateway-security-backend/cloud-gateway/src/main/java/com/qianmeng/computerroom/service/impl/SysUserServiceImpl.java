package com.qianmeng.computerroom.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qianmeng.computerroom.components.BCryptPasswordEncoderUtil;
import com.qianmeng.computerroom.components.JwtTokenUtil;
import com.qianmeng.computerroom.mapper.CompanyMapper;
import com.qianmeng.computerroom.mapper.SysFrontendMenuMapper;
import com.qianmeng.computerroom.mapper.SysUserMapper;
import com.qianmeng.computerroom.po.Company;
import com.qianmeng.computerroom.po.SysFrontendMenu;
import com.qianmeng.computerroom.po.SysUser;
import com.qianmeng.computerroom.service.SysUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 郭超
 * Date:2020-09-10 14:08
 * Description: 用户服务实现类,同样继承了MP的的通用实现类
 */
@Service
@Slf4j
@CacheConfig(cacheNames = "gateway_sysUser")
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    @Resource
    private BCryptPasswordEncoderUtil bCryptPasswordEncoderUtil;

    @Resource
    private SysUserMapper userMapper;

    @Resource
    private SysFrontendMenuMapper menuMapper;

    @Resource
    private CompanyMapper companyMapper;

    @Resource
    private JwtTokenUtil jwtTokenUtil;

    /**
     * 通过账号查询用户
     *
     * @param username 用户名
     * @return 查到的用户对象
     */
    @Override
    @Cacheable(key = "#username")
    public SysUser getUserByUserName(String username) {
        LambdaQueryWrapper<SysUser> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        // 查询条件：全匹配账号名,和状态为1的账号
        lambdaQueryWrapper
                .eq(SysUser::getUserName, username)
                .eq(SysUser::getIsDeleted, 0);

        // 用getOne查询一个对象出来,第二个参数false可以保证查询到多条数据时不报错,只提取列表中的第一条
        return userMapper.selectOne(lambdaQueryWrapper);
    }

    /**
     * 个性化验证登录
     *
     * @param username    账号
     * @param rawPassword 原始密码
     * @return 返回验证结果
     */
    @Override
    public boolean checkLogin(String username, String rawPassword) throws Exception {
        SysUser user = this.getUserByUserName(username);
        log.info("MyUsernamePasswordAuthenticationFilter => checkLogin + user = " + user);
        if (user == null) {
            log.info("MyUsernamePasswordAuthenticationFilter => checkLogin--------->账号不存在,请重新尝试！");
            // 设置友好提示
            throw new Exception("账号不存在,请重新尝试！");
        } else {
            // 获取加密的密码
            String encodedPassword = user.getPassword();
            // 和加密后的密码进行比配
            if (!bCryptPasswordEncoderUtil.matches(rawPassword, encodedPassword)) {
                log.info("checkLogin--------->密码不正确！");
                // 设置友好提示
                throw new Exception("密码不正确！");
            } else {
                return true;
            }
        }
    }

    @Override
    public Map<String, Object> getUserInfo(String token) {
        // 获取用户名
        String userName = jwtTokenUtil.getUsernameFromToken(token);
        log.info("从jwtTokenUtil中取出的用户名为：" + userName);
        // 获取用户全部信息
        LambdaQueryWrapper<SysUser> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(SysUser::getUserName, userName);
        SysUser user = userMapper.selectOne(queryWrapper);
        // 获取菜单
        List<SysFrontendMenu> menuList = menuMapper.getMenusByUserName(userName);
        // 获取公司信息
        Company company = companyMapper.selectByUserName(userName);
        Map<String, Object> map = new HashMap<>(2);
        map.put("user", user);
        map.put("menus", menuList);
        map.put("company", company);
        return map;
    }

}
