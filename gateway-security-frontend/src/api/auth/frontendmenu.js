import request from '@/utils/request'
import { getLoginUrl } from '@/api/url'

// 全查
export function selectAllMenu(data) {
  return request({
    url: getLoginUrl() + '/menu/selectAll',
    method: 'get',
    params: data
  })
}

// 将list数据转为存在父子结构的json格式
export function getMenuTreeJsonData(menuList) {
  // 第一重遍历,获取菜单项的父节点
  menuList.forEach(menu => {
    const menuPid = menu.menuPid
    if (menuPid !== 0) {
      // 第二重遍历,根据父节点存入children
      menuList.forEach(Menu => {
        if (Menu.menuId === menuPid) {
          // 如果当前父节点还没有children元素,则先赋值一个空数组
          if (!Menu.children) {
            Menu.children = []
          }
          Menu.children.push(menu)
        }
      })
    }
  })
  // forEach遍历完成后,需要将树展开,去除空元素
  menuList = menuList.filter(menu => menu.menuPid === 0)
  return menuList[0]
}

// 分页查询
export function selectByPage(query) {
  return request({
    url: getLoginUrl() + '/menu/selectByPage',
    method: 'get',
    params: query
  })
}

// 新增
export function createMenu(sysFrontendMenu) {
  return request({
    url: getLoginUrl() + '/menu/insert',
    method: 'post',
    params: sysFrontendMenu
  })
}

// 修改
export function updateMenu(sysFrontendMenu) {
  return request({
    url: getLoginUrl() + '/menu/update',
    method: 'put',
    params: sysFrontendMenu
  })
}

// 删除
export function deleteMenu(menu) {
  return request({
    url: getLoginUrl() + '/menu/delete',
    method: 'put',
    params: menu
  })
}

// 根据角色ID查询菜单项,用于回显
export function selectMenuByRoleId(roleId) {
  return request({
    url: getLoginUrl() + '/menu/selectByRoleId',
    method: 'get',
    params: roleId
  })
}

// 为某个角色分配菜单项
export function distributeMenu(query) {
  return request({
    url: getLoginUrl() + '/menu/distributeMenus',
    method: 'get',
    params: query
  })
}

