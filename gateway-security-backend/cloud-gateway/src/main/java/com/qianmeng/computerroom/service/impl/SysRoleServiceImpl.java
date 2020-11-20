package com.qianmeng.computerroom.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qianmeng.computerroom.mapper.SysRoleMapper;
import com.qianmeng.computerroom.mapper.SysUserMapper;
import com.qianmeng.computerroom.po.SysRole;
import com.qianmeng.computerroom.po.SysUser;
import com.qianmeng.computerroom.service.SysRoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author 郭超
 * Date:2020-09-10 14:33
 * Description:
 */
@Service
@Slf4j
@CacheConfig(cacheNames = "gateway_sysRole")
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {

    @Resource
    private SysUserMapper userMapper;

    /**
     * 根据用户名称查询角色和userId
     *
     * @param userName 用户名
     * @return 拥有的角色名
     */
    @Override
    @Cacheable(key = "#userName")
    public List<String> getRolesByUserName(String userName) {
        // 先把userId查出来放到tokenCache里先
        LambdaQueryWrapper<SysUser> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(SysUser::getUserName, userName);
        SysUser user = userMapper.selectOne(queryWrapper);
        // 把查到的用户存入Redis
        return this.baseMapper.getRolesByUserName(userName);
    }
}
