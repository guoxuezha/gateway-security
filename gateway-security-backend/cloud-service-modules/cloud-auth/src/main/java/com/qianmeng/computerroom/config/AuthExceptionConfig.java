package com.qianmeng.computerroom.config;

/**
 * @author 郭超
 * Date:2020-10-19 16:25
 * Description: 统一配置权限模块异常
 */
public class AuthExceptionConfig {

    /**
     * 用户名重复
     *
     * @return 警告信息
     */
    public static String userNameRepeatException() {
        return "该用户名已存在,不可重复录入！";
    }

    /**
     * 角色名重复
     *
     * @return 警告信息
     */
    public static String roleNameRepeatException() {
        return "该角色名已存在,不可重复录入！";
    }

    /**
     * 公司名称重复录入
     *
     * @return 警告信息
     */
    public static String companyNameRepeatException() {
        return "公司名已存在,不可重复录入！";
    }

    /**
     * 部门名称重复录入
     *
     * @return 警告信息
     */
    public static String departmentNameRepeatException() {
        return "该部门已存在,不可重复录入！";
    }

    /**
     * 菜单Code重复录入
     *
     * @return 警告信息
     */
    public static String menuCodeRepeatException() {
        return "菜单Code已存在,不可重复录入！";
    }

}
