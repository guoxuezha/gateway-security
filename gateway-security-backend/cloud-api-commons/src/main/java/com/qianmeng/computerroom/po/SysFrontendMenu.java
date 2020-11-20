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
@TableName(value = "sys_frontend_menu")
public class SysFrontendMenu implements Serializable {

    @TableId
    private Integer menuId;

    private Integer menuPid;

    private String menuName;

    private String menuCodeName;

    private String menuUrl;

    private Integer menuSort;

    private String menuDescription;

    private Integer isDeleted;

    private String createTime;

    private Integer createPerson;

    private String modifiedTime;

    private Integer modifiedPerson;

    private String deletedTime;

    private Integer deletedPerson;

    private String menuRemark;
}