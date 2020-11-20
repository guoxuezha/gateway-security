package com.qianmeng.computerroom.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qianmeng.computerroom.mapper.DepartmentMapper;
import com.qianmeng.computerroom.po.Department;
import com.qianmeng.computerroom.service.DepartmentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import static com.qianmeng.computerroom.config.AuthExceptionConfig.departmentNameRepeatException;

/**
 * @author 郭超
 * Date:2020-09-05 11:41
 * Description:
 */
@Service("departmentService")
@Slf4j
@CacheConfig(cacheNames = "auth_department")
public class DepartmentServiceImpl extends ServiceImpl<DepartmentMapper, Department> implements DepartmentService {

    @Resource
    private DepartmentMapper departmentMapper;

    @Override
    @CachePut(key = "#department")
    public int insert(Department department) throws Exception {
        LambdaQueryWrapper<Department> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(Department::getDepartmentName, department.getDepartmentName());
        Department existDepartment = departmentMapper.selectOne(queryWrapper);
        if (existDepartment != null){
            throw new Exception(departmentNameRepeatException());
        }
        departmentMapper.insert(department);
        return 0;
    }

}
