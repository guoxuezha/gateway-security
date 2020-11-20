const getters = {
  // 侧边栏菜单
  sidebar: state => state.app.sidebar,
  // 设备类型
  device: state => state.app.device,
  // 用户token
  token: state => state.user.token,
  //
  avatar: state => state.user.avatar,
  // 用户
  userObj: state => state.user.userObj,
  // 用户名
  name: state => state.user.name,
  // 当前用户所属的公司信息
  company: state => state.user.company,
  // 动态路由
  permission_routes: state => state.permission.routes
}
export default getters
