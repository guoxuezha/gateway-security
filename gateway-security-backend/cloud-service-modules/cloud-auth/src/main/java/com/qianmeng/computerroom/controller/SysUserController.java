package com.qianmeng.computerroom.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.api.ApiController;
import com.qianmeng.computerroom.po.SysUser;
import com.qianmeng.computerroom.service.SysUserService;
import com.qianmeng.computerroom.util.resulthelper.ResponseResult;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 登录接口
 *
 * @author qianmeng
 */
@RestController
@RequestMapping("/auth/user")
@Slf4j
public class SysUserController extends ApiController {

    @Resource
    private SysUserService userService;

    /**
     * 查询所有用户
     *
     * @return 用户大集合
     */
    @GetMapping("/selectAll")
    @ApiOperation(value = "用户全查", notes = "查询全部用户信息")
    public Mono<ResponseResult> selectAll() {
        List<SysUser> list = userService.list();
        return Mono.just(ResponseResult.success(list));
    }

    @GetMapping("/selectNotDeleted")
    @ApiOperation(value = "用户全查", notes = "查询未被删除的用户信息")
    public Mono<ResponseResult> selectNotDeleted() {
        LambdaQueryWrapper<SysUser> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(SysUser::getIsDeleted, 0);
        List<SysUser> list = userService.list(queryWrapper);
        return Mono.just(ResponseResult.success(list));
    }

    /**
     * 分页查询用户列表
     *
     * @param page   当前页
     * @param limit  每页条数
     * @param name   用户名
     * @param roleId 角色ID
     * @return 用户集合
     */
    @GetMapping("/selectByPage")
    @ApiOperation(value = "用户分页查询", notes = "查询全部用户信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "当前页数", required = true, dataType = "Integer", paramType = "path"),
            @ApiImplicitParam(name = "limit", value = "每页条数", required = true, dataType = "Integer", paramType = "path"),
            @ApiImplicitParam(name = "name", value = "用户名", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "roleId", value = "角色ID", required = true, dataType = "Integer", paramType = "path")
    })
    public Mono<ResponseResult> selectByPage(Integer page, Integer limit, String name, Integer roleId, Integer companyId) {
        log.info("companyId === " + companyId);
        return Mono.just(userService.selectByPage(page, limit, name, roleId, companyId));
    }

    /**
     * 检测用户是否存在
     *
     * @param userName 用户名
     * @return 检测结果
     */
    @GetMapping("/validUsername")
    @ApiOperation(value = "根据userName查询用户", notes = "检测用户是否存在")
    @ApiImplicitParam(name = "userName", value = "用户名", required = true, dataType = "String", paramType = "path")
    public Mono<ResponseResult> validUsername(String userName) {
        LambdaQueryWrapper<SysUser> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(SysUser::getIsDeleted, 0).eq(SysUser::getUserName, userName);
        SysUser user = userService.getOne(queryWrapper);
        if (user != null) {
            return Mono.just(ResponseResult.success());
        } else {
            return Mono.just(ResponseResult.fail("用户不存在！"));
        }
    }

    /**
     * 根据token获取user信息
     *
     * @param token token
     * @return userMap
     */
    @GetMapping("/getUserInfoByToken")
    public Mono<ResponseResult> getUserInfoByToken(String token) {
        Map<String, Object> infoMap = userService.getUserInfo(token);
        log.info("根据token获取到的用户信息 = " + infoMap.toString());
        return Mono.just(ResponseResult.success(infoMap));
    }

    /**
     * 添加用户、用户自行注册。
     *
     * @param register 前台Form表单提交的user对象
     * @return 注册结果
     */
    @PostMapping("/register")
    @ApiOperation(value = "新增用户", notes = "新增用户,密码自动加密")
    @ApiImplicitParam(name = "sysUserVo", value = "要新增的用户信息", required = true, dataType = "SysUserVo", paramType = "path")
    public Mono<ResponseResult> register(SysUser register) {
        log.info("========================" + register.toString());
        try {
            SysUser user = userService.register(register);
            return Mono.just(ResponseResult.success(user));
        } catch (Exception e) {
            return Mono.just(ResponseResult.fail(e.getMessage()));
        }
    }

    /**
     * 修改数据
     *
     * @param sysUser 实体对象
     * @return 修改结果
     */
    @PutMapping("/update")
    @ApiOperation(value = "修改用户基本信息", notes = "修改用户基本信息")
    @ApiImplicitParam(name = "sysUserVo", value = "修改后的用户信息", required = true, dataType = "SysUserVo", paramType = "path")
    public Mono<ResponseResult> update(SysUser sysUser) {
        return Mono.just(ResponseResult.success(this.userService.updateById(sysUser)));
    }

    /**
     * 冻结/解冻用户
     *
     * @param sysUser 要冻结的用户ID
     * @return 执行结果
     */
    @PutMapping("/updateUserStatus")
    @ApiOperation(value = "冻结用户", notes = "冻结用户")
    @ApiImplicitParam(name = "sysUser", value = "要冻结的用户ID集合", required = true, dataType = "List<String>", paramType = "path")
    public Mono<ResponseResult> freeze(SysUser sysUser) {
        LambdaQueryWrapper<SysUser> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(SysUser::getUserId, sysUser.getUserId());
        this.userService.update(sysUser, queryWrapper);
        return Mono.just(ResponseResult.success(sysUser));
    }

    /**
     * 删除数据
     *
     * @param userId 要删除的用户ID
     * @return 删除结果
     */
    @DeleteMapping("/delete/{userId}")
    @ApiOperation(value = "批量删除用户", notes = "物理删除")
    @ApiImplicitParam(name = "idList", value = "要删除的用户ID集合", required = true, dataType = "List<String>", paramType = "path")
    public Mono<ResponseResult> delete(@PathVariable("userId") Integer userId) {
        userService.delete(userId);
        return Mono.just(ResponseResult.success());
    }

}
