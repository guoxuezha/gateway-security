package com.qianmeng.computerroom.po;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author qianmeng
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "company")
public class Company {

    @TableId
    private Integer companyId;

    private String companyName;

    private String contacts;

    private String tel;

    private String email;

    private String address;

    private String communityName;

    private Integer isDeleted;

    private String createTime;

    private Integer createPerson;

    private String modifiedTime;

    private Integer modifiedPerson;

    private String deletedTime;

    private Integer deletedPerson;

}