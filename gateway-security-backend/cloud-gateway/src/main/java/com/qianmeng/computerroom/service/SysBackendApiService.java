package com.qianmeng.computerroom.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qianmeng.computerroom.po.SysBackendApi;
import com.qianmeng.computerroom.vo.SysBackendApiVo;

import java.util.List;

/**
 * @author 郭超
 * Date:2020-09-10 16:37
 * Description:
 */
public interface SysBackendApiService extends IService<SysBackendApi> {

    /**
     * 根据用户名称查询API接口URL
     *
     * @param username 用户名
     * @return API集合
     */
    List<SysBackendApi> getApiUrlByUserName(String username);

    /**
     * 查所有（编辑使用）
     *
     * @return API全集合
     */
    List<SysBackendApiVo> getAllApiList();
}
