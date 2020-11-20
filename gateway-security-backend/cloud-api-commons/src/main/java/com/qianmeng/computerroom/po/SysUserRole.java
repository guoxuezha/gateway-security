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
@TableName(value = "sys_user_role")
public class SysUserRole implements Serializable {

    @TableId
    private Integer userRoleId;

    private Integer userId;

    private Integer roleId;

    private Integer isDeleted;

    private String createTime;

    private Integer createPerson;

    private String modifiedTime;

    private Integer modifiedPerson;

    private String deletedTime;

    private Integer deletedPerson;

    private String userRoleRemark;

}