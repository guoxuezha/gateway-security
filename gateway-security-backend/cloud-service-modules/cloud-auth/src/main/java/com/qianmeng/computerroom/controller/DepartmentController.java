package com.qianmeng.computerroom.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qianmeng.computerroom.po.Department;
import com.qianmeng.computerroom.service.DepartmentService;
import com.qianmeng.computerroom.util.resulthelper.ResponseResult;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author 郭超
 * Date:2020-09-05 11:34
 * Description:
 */
@RestController
@RequestMapping("/auth/department")
public class DepartmentController {

    @Resource
    private DepartmentService departmentService;

    @GetMapping("/selectAll")
    @ApiOperation(value = "查询全部部门", notes = "查询全部部门")
    public Mono<ResponseResult> selectAll() {
        List<Department> departmentList = departmentService.list();
        return Mono.just(ResponseResult.success(departmentList));
    }

    @GetMapping("/selectNotDeleted")
    @ApiOperation(value = "查询所有未被删除的部门")
    public Mono<ResponseResult> selectNotDeleted() {
        LambdaQueryWrapper<Department> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(Department::getIsDeleted, 0);
        List<Department> departmentList = departmentService.list(queryWrapper);
        return Mono.just(ResponseResult.success(departmentList));
    }

    @GetMapping("/selectByPage")
    @ApiOperation(value = "分页查询部门列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "当前页数", required = true, dataType = "Integer", paramType = "path"),
            @ApiImplicitParam(name = "limit", value = "每页条数", required = true, dataType = "Integer", paramType = "path")
    })
    public Mono<ResponseResult> selectByPage(Integer page, Integer limit) {
        Page<Department> pages = new Page<>(page, limit);
        LambdaQueryWrapper<Department> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(Department::getIsDeleted, 0);
        IPage<Department> page1 = departmentService.page(pages, queryWrapper);
        List<Department> departmentList = page1.getRecords();
        int total = (int) page1.getTotal();
        return Mono.just(ResponseResult.success(departmentList, total));
    }

    @PostMapping("/insert")
    @ApiOperation(value = "新增部门信息")
    @ApiImplicitParam(name = "Department", value = "部门对象", required = true, dataType = "Department", paramType = "path")
    public Mono<ResponseResult> insert(Department department) {
        try {
            departmentService.insert(department);
        } catch (Exception e) {
            e.printStackTrace();
            return Mono.just(ResponseResult.fail(e.getMessage()));
        }
        return Mono.just(ResponseResult.success(department));
    }

    @PutMapping("/update")
    @ApiOperation(value = "修改部门信息")
    @ApiImplicitParam(name = "Department", value = "部门对象", required = true, dataType = "Department", paramType = "path")
    public Mono<ResponseResult> update(Department department) {
        departmentService.updateById(department);
        return Mono.just(ResponseResult.success(department));
    }

    @PutMapping("/delete")
    @ApiOperation(value = "逻辑删除部门信息")
    @ApiImplicitParam(name = "Department", value = "部门ID", required = true, dataType = "Integer", paramType = "path")
    public Mono<ResponseResult> delete(Department department) {
        boolean result = departmentService.updateById(department);
        return Mono.just(ResponseResult.success(result));
    }
}
