package com.qianmeng.computerroom.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qianmeng.computerroom.mapper.SysBackendApiMapper;
import com.qianmeng.computerroom.po.SysBackendApi;
import com.qianmeng.computerroom.service.SysBackendApiService;
import com.qianmeng.computerroom.vo.SysBackendApiVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 郭超
 * Date:2020-09-10 16:38
 * Description:
 */
@Service
@Slf4j
@CacheConfig(cacheNames = "gateway_backendApi")
public class SysBackendApiServiceImpl extends ServiceImpl<SysBackendApiMapper, SysBackendApi> implements SysBackendApiService {

    /**
     * 根据用户名称查询API接口URL
     *
     * @param username 用户名
     * @return 权限API集合
     */
    @Override
    @Cacheable(key = "#username")
    public List<SysBackendApi> getApiUrlByUserName(String username) {
        log.info("getApiUrlByUserName.................");
        return this.baseMapper.getApiUrlByUserName(username);
    }

    /**
     * 查所有（编辑使用）
     */
    @Override
    @Cacheable()
    public List<SysBackendApiVo> getAllApiList() {
        return this.baseMapper.getAllApiList();
    }
}
