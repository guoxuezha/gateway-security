import request from '@/utils/request'
import { getLoginUrl } from '@/api/url'

// 全查
export function selectNotDeletedCompany(companyId) {
  return request({
    url: getLoginUrl() + '/company/selectNotDeleted',
    method: 'get',
    params: { companyId: companyId }
  })
}

// 分页查询
export function selectByPage(query) {
  return request({
    url: getLoginUrl() + '/company/selectByPage',
    method: 'get',
    params: query
  })
}

// 新增
export function createCompany(company) {
  return request({
    url: getLoginUrl() + '/company/insert',
    method: 'post',
    params: company
  })
}

// 修改
export function updateCompany(company) {
  return request({
    url: getLoginUrl() + '/company/update',
    method: 'put',
    params: company
  })
}

// 删除
export function deleteCompany(company) {
  return request({
    url: getLoginUrl() + '/company/delete',
    method: 'put',
    params: company
  })
}
