package com.qianmeng.computerroom.po;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author qianmeng
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "sys_role_frontend_menu")
public class SysRoleFrontendMenu implements Serializable {

    @TableId
    private Integer roleMenuId;

    private Integer roleId;

    private Integer menuId;

    private Integer isDeleted;

    private String createTime;

    private Integer createPerson;

    private String modifiedTime;

    private Integer modifiedPerson;

    private String deletedTime;

    private Integer deletedPerson;

    private String roleMenuRemark;

}