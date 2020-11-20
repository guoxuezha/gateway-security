package com.qianmeng.computerroom.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 简单的用户注册帮助类,没有太多无用信息
 *
 * @author qianmeng
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SysUserVo {

    private Integer id;

    private String username;

    private String password;

    private String description;

}
