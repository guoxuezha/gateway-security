import request from '@/utils/request'
import { getLoginUrl } from '@/api/url'

// 全查
export function selectAllRole() {
  return request({
    url: getLoginUrl() + '/role/selectAll',
    method: 'get'
  })
}

// 分页查询
export function selectByPage(query) {
  return request({
    url: getLoginUrl() + '/role/selectByPage',
    method: 'get',
    params: query
  })
}

// 新增
export function createRole(data) {
  return request({
    url: getLoginUrl() + '/role/insert',
    method: 'post',
    data
  })
}

// 修改
export function updateRole(data) {
  return request({
    url: getLoginUrl() + '/role/update',
    method: 'put',
    data
  })
}

// 删除
export function deleteRole(role) {
  return request({
    url: getLoginUrl() + '/role/delete',
    method: 'put',
    params: role
  })
}

// 根据userId查询某角色已有的roleId,分配角色时实现回显
export function getRoleIdByUserId(userId) {
  return request({
    url: getLoginUrl() + '/role/selectByUserId',
    method: 'get',
    params: userId
  })
}

// 为用户分配角色
export function distributeRole(query) {
  return request({
    url: getLoginUrl() + '/role/distributeRoles',
    method: 'get',
    params: query
  })
}
