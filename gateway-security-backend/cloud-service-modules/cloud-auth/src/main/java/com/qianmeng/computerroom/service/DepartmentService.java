package com.qianmeng.computerroom.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qianmeng.computerroom.po.Department;

/**
 * @author 郭超
 * Date:2020-09-05 11:41
 * Description:
 */
public interface DepartmentService extends IService<Department> {

    /**
     * 新增部门
     *
     * @param department 部门对象
     * @return 新增结果
     */
    int insert(Department department) throws Exception;
}
