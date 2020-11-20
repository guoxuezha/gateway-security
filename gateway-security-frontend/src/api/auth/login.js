import request from '@/utils/request'
import { getLoginUrl } from '@/api/url'

const USER_AUTH_KEY = 'Authorization'
let authObj = null

// 检测用户名是否存在
export function validUsername(userName) {
  return request({
    url: getLoginUrl() + '/user/validUsername',
    method: 'get',
    params: userName
  })
}

// 执行登陆
export function login(data) {
  return request({
    url: 'http://localhost:9527/login',
    method: 'get',
    params: data
  })
}

// 通过Token获取用户基本信息
export function getInfo(token) {
  return request({
    url: 'http://localhost:9527/getUserInfoByToken',
    method: 'get',
    params: { token: token }
  })
}

// 退出
export function logout(token) {
  return request({
    url: 'http://localhost:9527/logout',
    method: 'get',
    params: { token: token }
  })
}

// 保存用户基本权限信息
export function setAuth(auth) {
  if (auth) {
    const authString = JSON.stringify(auth)
    window.sessionStorage.setItem(USER_AUTH_KEY, authString)
  }
}

// 清除sessionStorage中的userAuth
export function deleteAuth() {
  window.sessionStorage.removeItem(USER_AUTH_KEY)
}

/**
 * 可以直接调用此方法获得当前登录人
 * @returns {null}
 */
export function getUserName() {
  return this._getVal('username')
}

/**
 * 可以直接调用此方法获得角色名称
 * @returns {null}
 */
export function getAuth() {
  return this._getVal('auth')
}

/**
 * 可以直接调用此方法获得该用户的菜单
 * @returns {null}
 */
export function getMenus() {
  return this._getVal('menus')
}

/**
 * 可以直接调用此方法获得token，一般不需要调用
 * 在checkIsLogin.js中已经设置到全局的headers中
 * 访问后台API时，自动携带了token，无须每次都设置
 * @returns {null}
 */
export function getToken() {
  return this._getVal('token')
}

/**
 * 进入index页面
 * @param token
 */
export function gotoManagerCenter(token) {
  if (token) {
    document.location.href = './index.html'
  }
}

/**
 * 进入登录页面
 * @param token
 */
export function gotoLogin(token) {
  if (!token) {
    document.location.href = './login.html'
  }
}

// 获取token中的权限信息
export function _getAuth() {
  const authString = window.sessionStorage.getItem(USER_AUTH_KEY)
  if (authString) {
    // eslint-disable-next-line no-const-assign
    authObj = JSON.parse(authString)
  }
}

// 获取菜单内容和结构
export function _getVal(val) {
  if (authObj == null) {
    // console.log("加载sessionStorage中的userAuth信息......");
    this._getAuth()
  }
  return authObj ? authObj[val] : null
}

