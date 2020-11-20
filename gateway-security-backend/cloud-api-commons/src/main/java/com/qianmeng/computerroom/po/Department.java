package com.qianmeng.computerroom.po;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author 郭超
 * Date:2020-09-04 17:44
 * Description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Department implements Serializable {

    @TableId
    private Integer departmentId;

    private String departmentName;

    private Integer isDeleted;

    private String createTime;

    private Integer createPerson;

    private String modifiedTime;

    private Integer modifiedPerson;

    private String deletedTime;

    private Integer deletedPerson;

    private String departmentRemark;

    }
