/*
package com.qianmeng.computerroom.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.api.ApiController;
import com.qianmeng.computerroom.po.SysFrontendMenu;
import com.qianmeng.computerroom.service.SysBackendApiService;
import com.qianmeng.computerroom.util.resulthelper.ResponseResult;
import com.qianmeng.computerroom.vo.SysFrontendVo;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;

*/
/**
 * (SysBackendApi)表控制层,负责API单表的CRUD
 *
 * @author qianmeng
 * @since 2020-08-12 14:31:50
 *//*

@RestController
@RequestMapping("/auth/api")
public class SysBackendApiController extends ApiController {

    @Resource
    private SysBackendApiService sysBackendApiTableService;

    */
/**
     * 查询所有菜单项
     *
     * @return 所有菜单项
     *//*

    @GetMapping("/selectAll")
    @ApiOperation(value = "菜单项全查", notes = "查询全部菜单信息")
    public Mono<ResponseResult> selectAll() {
        return Mono.just(ResponseResult.success(sysBackendApiTableService.selectAllMenu()));
    }

    */
/**
     * 菜单项分页查询
     *
     * @param page  当前页
     * @param limit 每页条数
     * @param name  菜单项名称
     * @return 菜单项集合
     *//*

    @GetMapping("/selectByPage")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "当前页数", required = true, dataType = "Integer", paramType = "path"),
            @ApiImplicitParam(name = "limit", value = "每页条数", required = true, dataType = "Integer", paramType = "path"),
            @ApiImplicitParam(name = "name", value = "菜单名", required = true, dataType = "String", paramType = "path"),
    })
    public Mono<ResponseResult>  selectByPage(Integer page, Integer limit, String name) {
        return Mono.just(sysFrontendMenuService.selectByPage(page, limit, name));
    }

    */
/**
     * 通过主键查询单条数据
     *
     * @param menuId 菜单ID
     * @return 单条数据
     *//*

    @GetMapping("/selectOne/{menuId}")
    @ApiOperation(value = "查询单条菜单项信息", notes = "根据menuId查询单条菜单项信息")
    @ApiImplicitParam(name = "menuId", value = "菜单项ID", required = true, dataType = "Integer", paramType = "path")
    public Mono<ResponseResult>  selectOne(@PathVariable("menuId") Serializable menuId) {
        return Mono.just(ResponseResult.success(this.sysFrontendMenuService.getById(menuId)));
    }

    */
/**
     * 根据roleId查询该角色可访问的菜单项
     *
     * @param roleId 角色ID
     * @return 菜单项集合
     *//*

    @GetMapping("/selectByRoleId")
    @ApiOperation(value = "查询角色的菜单权限", notes = "根据roleId查询该角色可访问的菜单项")
    @ApiImplicitParam(name = "roleId", value = "角色ID", required = true, dataType = "Integer", paramType = "path")
    public Mono<ResponseResult>  selectByRoleId(Integer roleId) {
        log.info("roleId ===============================" + roleId);
        List<SysFrontendMenu> menuList = sysFrontendMenuService.selectByRoleId(roleId);
        return Mono.just(ResponseResult.success(menuList));
    }

    */
/**
     * 为角色分配可访问菜单
     *
     * @param roleId  绝嗣ID
     * @param menuIds 菜单项ID
     * @return 分配结果
     *//*

    @GetMapping("/distributeMenus")
    @ApiOperation(value = "查询角色的菜单权限", notes = "根据roleId查询该角色可访问的菜单项")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleId", value = "角色ID", required = true, dataType = "Integer", paramType = "path"),
            @ApiImplicitParam(name = "menuIds", value = "菜单项ID字符串,ID之间用逗号分隔", required = true, dataType = "String", paramType = "path")
    })
    public Mono<ResponseResult>  distributeMenus(Integer roleId, String menuIds) {
        log.info("roleId = " + roleId + " menuIds == " + menuIds);
        sysFrontendMenuService.distributeMenu(roleId, menuIds);
        return Mono.just(ResponseResult.success());
    }

    */
/**
     * 新增数据
     *
     * @param sysFrontendMenu 实体对象
     * @return 新增结果
     *//*

    @PostMapping("/insert")
    @ApiOperation(value = "新增菜单项", notes = "新增菜单项")
    @ApiImplicitParam(name = "sysFrontendMenu", value = "菜单项内容", required = true, dataType = "SysFrontendMenu", paramType = "path")
    public Mono<ResponseResult>  insert(SysFrontendMenu sysFrontendMenu) {
        log.info("sysFrontendMenu = " + sysFrontendMenu.toString());
        SysFrontendVo menuVo = sysFrontendMenuService.insert(sysFrontendMenu);
        return Mono.just(ResponseResult.success(menuVo));
    }

    */
/**
     * 修改数据
     *
     * @param sysFrontendMenu 实体对象
     * @return 修改结果
     *//*

    @PutMapping("/update")
    @ApiOperation(value = "修改菜单项", notes = "修改菜单项")
    @ApiImplicitParam(name = "sysFrontendMenu", value = "修改后的菜单项内容", required = true, dataType = "SysFrontendMenu", paramType = "path")
    public Mono<ResponseResult>  update(SysFrontendMenu sysFrontendMenu) {
        SysFrontendVo menuVo = sysFrontendMenuService.updateByMenuId(sysFrontendMenu);
        log.info("menuVo = " + menuVo);
        return Mono.just(ResponseResult.success(menuVo));
    }

    */
/**
     * 删除数据
     *
     * @param menuId 主键结合
     * @return 删除结果
     *//*

    @DeleteMapping("/delete/{menuId}")
    @ApiOperation(value = "修改菜单项", notes = "修改菜单项")
    @ApiImplicitParam(name = "idList", value = "要删除的菜单项ID集合", required = true, dataType = "List<String>", paramType = "path")
    public Mono<ResponseResult>  delete(@PathVariable("menuId") Integer menuId) {
        LambdaQueryWrapper<SysFrontendMenu> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(SysFrontendMenu::getMenuId, menuId);
        return Mono.just(ResponseResult.success(this.sysFrontendMenuService.remove(queryWrapper)));
    }

}*/
