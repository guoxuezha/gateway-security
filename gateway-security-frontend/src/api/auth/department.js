import request from '@/utils/request'
import { getLoginUrl } from '@/api/url'

// 全查
export function selectNotDeletedDepartment() {
  return request({
    url: getLoginUrl() + '/department/selectNotDeleted',
    method: 'get'
  })
}

// 分页查询
export function selectByPage(query) {
  return request({
    url: getLoginUrl() + '/department/selectByPage',
    method: 'get',
    params: query
  })
}

// 新增
export function createDepartment(department) {
  return request({
    url: getLoginUrl() + '/department/insert',
    method: 'post',
    params: department
  })
}

// 修改
export function updateDepartment(department) {
  return request({
    url: getLoginUrl() + '/department/update',
    method: 'put',
    params: department
  })
}

// 删除
export function deleteDepartment(department) {
  return request({
    url: getLoginUrl() + '/department/delete',
    method: 'put',
    params: department
  })
}
