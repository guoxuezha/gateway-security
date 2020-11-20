import request from '@/utils/request'
import { getLoginUrl } from '@/api/url'

// 全查
export function selectAllApi() {
  return request({
    url: getLoginUrl() + 'api/selectAll',
    method: 'get'
  })
}

// 分页查询
export function selectByPage(query) {
  return request({
    url: getLoginUrl() + 'api/selectByPage',
    method: 'get',
    params: query
  })
}

// 新增
export function createApi(data) {
  return request({
    url: getLoginUrl() + '/api/insert',
    method: 'post',
    data
  })
}

// 修改
export function updateApi(data) {
  return request({
    url: getLoginUrl() + '/api/update',
    method: 'put',
    data
  })
}

// 删除
export function deleteApi(apiId) {
  return request({
    url: getLoginUrl() + `/api/delete/${apiId}`,
    method: 'delete'
  })
}
