package com.qianmeng.computerroom.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 角色和鉴权资源的VO
 * 如：角色和用户VO
 * 角色和API
 * 角色和前端菜单
 * 他们都可以使用该VO来转换并展现给前端
 *
 * @author qianmeng
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SysRoleAndPermissionVo {

    private Integer id;

    private String name;

    private Integer roleId;

    private Integer pid;

}
