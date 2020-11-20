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
@TableName(value = "sys_backend_api")
public class SysBackendApi implements Serializable {

    @TableId
    private Integer apiId;

    private Integer apiPid;

    private String apiName;

    private String apiUrl;

    private String apiMethod;

    private Integer apiSort;

    private String apiDescription;

    private Integer isDeleted;

    private String createTime;

    private Integer createPerson;

    private String modifiedTime;

    private Integer modifiedPerson;

    private String deletedTime;

    private Integer deletedPerson;

    private String apiRemark;

}