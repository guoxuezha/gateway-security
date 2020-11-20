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
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "sys_user")
public class SysUser implements Serializable {

    @TableId
    private Integer userId;

    private String userName;

    private String password;

    private Integer companyId;

    private Integer departmentId;

    private String jobNumber;

    private String realName;

    private String userTel;

    private String userDescription;

    private Integer isDeleted;

    private String createTime;

    private Integer createPerson;

    private String modifiedTime;

    private Integer modifiedPerson;

    private String deletedTime;

    private Integer deletedPerson;

    private String userRemark;

}