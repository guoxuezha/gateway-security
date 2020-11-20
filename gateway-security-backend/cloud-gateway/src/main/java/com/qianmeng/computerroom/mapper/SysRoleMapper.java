package com.qianmeng.computerroom.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qianmeng.computerroom.po.SysRole;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 郭超
 * Date:2020-09-10 14:34
 * Description:
 */
public interface SysRoleMapper extends BaseMapper<SysRole> {

    /**
     * 根据用户名查询拥有的角色ID
     *
     * @param userName 用户名
     * @return 角色ID集合
     */
    List<String> getRolesByUserName(@Param("userName") String userName);
}
