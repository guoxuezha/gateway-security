import request from '@/utils/request'
import { getLoginUrl } from '@/api/url'

// 全查
export function selectAllUser() {
  return request({
    url: getLoginUrl() + '/user/selectAll',
    method: 'get'
  })
}

// 分页查询
export function selectByPage(query) {
  return request({
    url: getLoginUrl() + '/user/selectByPage',
    method: 'get',
    params: query
  })
}

// 注册
export function createUser(register) {
  console.log(register)
  return request({
    url: getLoginUrl() + '/user/register',
    method: 'post',
    params: register
  })
}

// 修改
export function updateUser(query) {
  return request({
    url: getLoginUrl() + '/user/update',
    method: 'put',
    params: query
  })
}

// 冻结/解冻用户
export function updateUserStatus(sysUser) {
  return request({
    url: getLoginUrl() + '/user/updateUserStatus',
    method: 'put',
    params: sysUser
  })
}

// 删除
export function deleteUser(userId) {
  return request({
    url: getLoginUrl() + `/user/delete/${userId}`,
    method: 'delete'
  })
}

// post请求不可以加params参数,必须传data
// put请求必须加params: 且参数名必须和后台接收方法的形参一致

// 测试方法
export function getUserName() {
  return request({
    url: getLoginUrl() + '/user/getUserName',
    method: 'get'
  })
}

export function selectNotDeletedSysUser() {
  return request({
    url: getLoginUrl() + '/user/selectNotDeleted',
    method: 'get'
  })
}

