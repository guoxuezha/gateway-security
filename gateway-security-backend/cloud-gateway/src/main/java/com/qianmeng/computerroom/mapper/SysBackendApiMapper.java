package com.qianmeng.computerroom.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qianmeng.computerroom.po.SysBackendApi;
import com.qianmeng.computerroom.vo.SysBackendApiVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 郭超
 * Date:2020-09-10 16:38
 * Description:
 */
public interface SysBackendApiMapper extends BaseMapper<SysBackendApi> {

    /**
     * 根据用户名称获得API URL资源鉴权
     *
     * @param username 用户名
     * @return API权限集合
     */
    List<SysBackendApi> getApiUrlByUserName(@Param("username") String username);

    /**
     * 管理员对API进行管理
     *
     * @return API全集和
     */
    List<SysBackendApiVo> getAllApiList();
}
