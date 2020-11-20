package com.qianmeng.computerroom.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.qianmeng.computerroom.po.SysUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author qianmeng
 */
public interface SysUserMapper extends BaseMapper<SysUser> {

    /**
     * 根据角色ID分页联查用户表
     *
     * @param page      分页参数
     * @param userName  用户名
     * @param roleId    角色ID
     * @param companyId 公司ID
     * @return 查询到的用户集合
     */
    List<SysUser> selectUserAndRolesByPage(IPage page,
                                           @Param("userName") String userName,
                                           @Param("roleId") Integer roleId,
                                           @Param("companyId") Integer companyId);
}
