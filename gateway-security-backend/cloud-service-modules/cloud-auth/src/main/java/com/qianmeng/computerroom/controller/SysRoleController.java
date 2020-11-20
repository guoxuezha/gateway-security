package com.qianmeng.computerroom.controller;

import com.baomidou.mybatisplus.extension.api.ApiController;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qianmeng.computerroom.po.SysRole;
import com.qianmeng.computerroom.service.SysRoleBackendApiService;
import com.qianmeng.computerroom.service.SysRoleFrontendMenuService;
import com.qianmeng.computerroom.service.SysRoleService;
import com.qianmeng.computerroom.service.SysUserRoleService;
import com.qianmeng.computerroom.util.resulthelper.ResponseResult;
import com.qianmeng.computerroom.vo.SysRoleAndPermissionVo;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;

/**
 * (SysRole)表控制层
 *
 * @author qianmeng
 * @since 2020-08-12 14:31:50
 */
@RestController
@RequestMapping("/auth/role")
@Slf4j
public class SysRoleController extends ApiController {

    /**
     * 角色
     */
    @Resource
    private SysRoleService sysRoleService;

    /**
     * 角色-用户
     */
    @Resource
    private SysUserRoleService sysRoleUserService;

    /**
     * 角色-前端菜单
     */
    @Resource
    private SysRoleFrontendMenuService sysRoleFrontendMenuService;

    /**
     * 角色-API
     */
    @Resource
    private SysRoleBackendApiService sysRoleBackendApiService;

    @GetMapping("/selectAll")
    @ApiOperation(value = "角色全查", notes = "查询全部角色信息")
    public Mono<ResponseResult> selectAll() {
        List<SysRole> sysRoles = sysRoleService.list();
        return Mono.just(ResponseResult.success(sysRoles));
    }

    /**
     * 分页查询所有数据
     *
     * @param page  当前页
     * @param limit 每页容量
     * @return 所有数据
     */
    @GetMapping("/selectByPage")
    @ApiOperation(value = "角色分页查询", notes = "查询全部角色信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "当前页数", required = true, dataType = "Integer", paramType = "path"),
            @ApiImplicitParam(name = "limit", value = "每页条数", required = true, dataType = "Integer", paramType = "path"),
            @ApiImplicitParam(name = "name", value = "用户名", required = true, dataType = "String", paramType = "path")
    })
    public Mono<ResponseResult> selectByPage(Integer page, Integer limit, String name) {
        Page pages = new Page(page, limit);
        return Mono.just(sysRoleService.selectByPage(pages, name));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param roleId 角色ID
     * @return 单条数据
     */
    @GetMapping("selectOne/{roleId}")
    @ApiOperation(value = "根据ID查询角色", notes = "根据ID查询角色")
    @ApiImplicitParam(name = "roleId", value = "角色ID", required = true, dataType = "Integer", paramType = "path")
    public Mono<ResponseResult> selectOne(@PathVariable("roleId") Serializable roleId) {
        return Mono.just(ResponseResult.success(this.sysRoleService.getById(roleId)));
    }

    /**
     * 根据userId查询roleId集合
     *
     * @param userId 用户ID
     * @return 用户具有的角色ID集合
     */
    @GetMapping("/selectByUserId")
    @ApiOperation(value = "根据UserId查询roleId集合", notes = "根据UserId查询roleId集合")
    @ApiImplicitParam(name = "userId", value = "y用户ID", required = true, dataType = "Integer", paramType = "path")
    public Mono<ResponseResult> selectByUserId(Integer userId) {
        log.info("userId = " + userId);
        List<SysRole> roles = sysRoleService.selectByUserId(userId);
        return Mono.just(ResponseResult.success(roles));
    }

    /**
     * 新增数据
     *
     * @param sysRole 实体对象
     * @return 新增结果
     */
    @PostMapping("/insert")
    @ApiOperation(value = "新增角色", notes = "新增角色信息")
    @ApiImplicitParam(name = "sysRole", value = "角色信息", required = true, dataType = "SysRole", paramType = "path")
    public Mono<ResponseResult> insert(@RequestBody SysRole sysRole) {
        SysRole sysRole1 = null;
        try {
            sysRole1 = sysRoleService.insert(sysRole);
        } catch (Exception e) {
            e.printStackTrace();
            return Mono.just(ResponseResult.fail(e.getMessage()));
        }
        return Mono.just(ResponseResult.success(sysRole1));
    }

    /**
     * 修改数据
     *
     * @param sysRole 实体对象
     * @return 修改结果
     */
    @PutMapping("/update")
    @ApiOperation(value = "修改角色", notes = "修改角色信息")
    @ApiImplicitParam(name = "sysRole", value = "修改后的角色信息", required = true, dataType = "SysRole", paramType = "path")
    public Mono<ResponseResult> update(@RequestBody SysRole sysRole) {
        return Mono.just(ResponseResult.success(this.sysRoleService.updateById(sysRole)));
    }

    /**
     * 批量删除数据
     *
     * @param idList 主键集合
     * @return 删除结果
     */
    @DeleteMapping("/delete")
    public Mono<ResponseResult> delete(@RequestParam("idList") List<String> idList) {
        return Mono.just(ResponseResult.success(this.sysRoleService.removeByIds(idList)));
    }

    /**
     * 为用户分配角色
     *
     * @param userId  用户ID
     * @param roleIds 角色ID集合
     * @return 执行成功
     */
    @GetMapping("/distributeRoles")
    public Mono<ResponseResult> distributeRoles(Integer userId, String roleIds) {
        log.info("userId = " + userId + "  roleIds = " + roleIds);
        sysRoleService.distributeRoles(userId, roleIds);
        return Mono.just(ResponseResult.success());
    }

    //-----------------------------------------------------------------------

    /**
     * 对应前端：系统管理->用户角色设置->右边用户查询
     *
     * @param roleId 角色ID
     * @return 查询拥有该角色的用户集合
     */
    @GetMapping("/roleUser/{roleId}")
    public Mono<ResponseResult> getRoleAndUserList(@PathVariable("roleId") String roleId) {
        return Mono.just(ResponseResult.success(sysRoleService.getRoleAndUserList(roleId)));
    }

    /**
     * 基于角色新增用户-角色关联关系
     *
     * @param roleId                  角色ID
     * @param sysRoleAndPermissionVos 对应的用户集合
     * @return 关联结果
     */
    @PostMapping("/roleUser/insert")
    public Mono<ResponseResult> saveRoleUser(@RequestParam("roleId") String roleId,
                                             @RequestBody SysRoleAndPermissionVo[] sysRoleAndPermissionVos) {

        log.info("-------------现在进入方法saveRoleUser-----------");
        return Mono.just(ResponseResult.success(sysRoleUserService.saveRoleUser(roleId, sysRoleAndPermissionVos)));
    }

    //-----------------------------------------------------------------------

    /**
     * 对应前端：系统管理->菜单角色设置->右边菜单查询
     *
     * @param roleId 角色ID
     * @return 根据角色ID获取可访问菜单集合
     */
    @GetMapping("/roleMenu/{roleId}")
    public Mono<ResponseResult> getRoleAndMenuList(@PathVariable("roleId") String roleId) {
        return Mono.just(ResponseResult.success(sysRoleService.getRoleAndMenuList(roleId)));
    }

    /**
     * 基于角色新增角色-菜单关联关系
     *
     * @param roleId                  角色ID
     * @param sysRoleAndPermissionVos 该角色可以访问的菜单项集合
     * @return 执行结果
     */
    @PostMapping("/roleMenu/insert")
    public Mono<ResponseResult> saveRoleMenu(@RequestParam("roleId") String roleId,
                                             @RequestBody SysRoleAndPermissionVo[] sysRoleAndPermissionVos) {

        log.info("--------现在进入方法saveRoleMenu-------");
        return Mono.just(ResponseResult.success(sysRoleFrontendMenuService.saveRoleMenu(roleId, sysRoleAndPermissionVos)));
    }

    //-----------------------------API粒度太细了,暂且先不引入---------------------------------

    /**
     * 对应前端：系统管理->菜单角色设置->右边api查询
     *
     * @param roleId 角色ID
     * @return 查询该角色具有访问权限的的API集合
     */
    @GetMapping("/roleApi/{roleId}")
    public Mono<ResponseResult> getRoleAndApiList(@PathVariable("roleId") String roleId) {
        return Mono.just(ResponseResult.success(sysRoleService.getRoleAndApiList(roleId)));
    }

    /**
     * 基于角色新增角色-API关联关系
     *
     * @param roleId                  角色ID
     * @param sysRoleAndPermissionVos 要分配给该角色的API集合
     * @return 执行结果
     */
    @PostMapping("/roleApi/insert")
    public Mono<ResponseResult> saveRoleApi(@RequestParam("roleId") String roleId,
                                            @RequestBody SysRoleAndPermissionVo[] sysRoleAndPermissionVos) {
        log.info("-------现在进入方法saveRoleApi-------");
        return Mono.just(ResponseResult.success(sysRoleBackendApiService.saveRoleAip(roleId, sysRoleAndPermissionVos)));
    }

}