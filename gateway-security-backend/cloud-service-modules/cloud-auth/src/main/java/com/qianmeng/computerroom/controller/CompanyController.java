package com.qianmeng.computerroom.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qianmeng.computerroom.po.Company;
import com.qianmeng.computerroom.service.CompanyService;
import com.qianmeng.computerroom.util.resulthelper.ResponseResult;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author 郭超
 * Date:2020-08-25 13:49
 * Description:
 */
@Slf4j
@RestController
@RequestMapping("/auth/company")
public class CompanyController {

    @Resource
    private CompanyService companyService;

    /**
     * 查询所有未被删除的公司,
     * 如果companyId有值且不为0,则只查询对应的公司信息
     *
     * @param companyId 公司ID
     * @return 公司列表
     */
    @GetMapping("/selectNotDeleted")
    @ApiOperation(value = "查询公司集合", notes = "查询未被逻辑删除的公司列表")
    @ApiImplicitParam(name = "page", value = "当前页数", required = true, dataType = "Integer", paramType = "path")
    public Mono<ResponseResult> selectNotDeleted(Integer companyId) {
        List<Company> companyList = companyService.selectNotDeleted(companyId);
        companyService.selectNotDeleted(companyId);
        return Mono.just(ResponseResult.success(companyList));
    }

    @GetMapping("/selectByPage")
    @ApiOperation(value = "分页查询公司列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "当前页数", required = true, dataType = "Integer", paramType = "path"),
            @ApiImplicitParam(name = "limit", value = "每页条数", required = true, dataType = "Integer", paramType = "path"),
            @ApiImplicitParam(name = "name", value = "公司名关键字", required = true, dataType = "String", paramType = "path"),
    })
    public Mono<ResponseResult> selectByPage(Integer page, Integer limit, String name) {
        Page<Company> pages = new Page<>(page, limit);
        LambdaQueryWrapper<Company> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(Company::getIsDeleted, 0)
                .like(Company::getCompanyName, name);
        IPage<Company> page1 = companyService.page(pages, queryWrapper);
        List<Company> companyList = page1.getRecords();
        int total = (int) page1.getTotal();
        return Mono.just(ResponseResult.success(companyList, total));
    }

    @PostMapping("/insert")
    @ApiOperation(value = "新增公司信息")
    @ApiImplicitParam(name = "company", value = "公司对象", required = true, dataType = "Company", paramType = "path")
    public Mono<ResponseResult> insert(Company company) {
        try {
            companyService.insert(company);
        } catch (Exception e) {
            e.printStackTrace();
            log.info("==========" + e.getMessage());
            return Mono.just(ResponseResult.fail(e.getMessage()));
        }
        return Mono.just(ResponseResult.success(company));
    }

    @PutMapping("/update")
    @ApiOperation(value = "修改公司信息")
    @ApiImplicitParam(name = "company", value = "公司对象", required = true, dataType = "Company", paramType = "path")
    public Mono<ResponseResult> update(Company company) {
        boolean result = companyService.updateById(company);
        return Mono.just(ResponseResult.success(result));
    }

    @PutMapping("/delete")
    @ApiOperation(value = "物理删除公司信息")
    @ApiImplicitParam(name = "company", value = "公司ID", required = true, dataType = "Integer", paramType = "path")
    public Mono<ResponseResult> delete(Company company) {
        log.info("company = " + company.toString());
        boolean result = companyService.updateById(company);
        return Mono.just(ResponseResult.success(result));
    }



}
