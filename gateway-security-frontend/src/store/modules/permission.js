import { asyncRoutes, constantRoutes } from '@/router'

const state = {
  routes: [],
  addRoutes: []
}

const mutations = {
  SET_ROUTES: (state, routes) => {
    state.addRoutes = routes
    state.routes = constantRoutes.concat(routes)
  }
}

// 将菜单信息转成对应的路由信息 动态添加
const actions = {
  menusToRoutes({ commit }, menus) {
    return new Promise(resolve => {
      console.log('menusToRoutes0')
      const result = []
      let children = []

      /**
       * 方案一:
       * 1.先把列表转为树形结构
       * 2.遍历该树形结构,根据menuCodeName映射生成另一棵由静态路由表中元素构成的树
       */
      // 先把菜单列表转为树形结构
      menus.forEach(menu => {
        const menuPid = menu.menuPid
        if (menuPid !== 0) {
          menus.forEach(Menu => {
            if (Menu.menuId === menuPid) {
              if (!Menu.children) {
                Menu.children = []
              }
              Menu.children.push(menu)
            }
          })
        }
      })
      console.log('menusToRoutes1')
      // 只保留一级菜单
      menus = menus.filter(menu => menu.menuSort === 1)

      console.log('menusToRoutes2')
      // 解析menu树,构造动态菜单
      menus.forEach(menu => {
        children = generateRoutes(children, menu)
        console.log(children)
      })

      console.log('menusToRoutes3')
      children.forEach(menu => {
        result.push(menu)
      })

      console.log('menusToRoutes4')
      // 最后添加404页面 否则会在登陆成功后跳到404页面
      result.push(
        {
          path: '*',
          redirect: '/404',
          hidden: true
        }
      )

      commit('SET_ROUTES', result)
      console.log('如果执行到这里则说明menusToRoutes没错')
      resolve(result)
    })
  }
}

// 向菜单树中添加节点
function generateRoutes(children, item) {
  if (item.children) {
    // 先把该节点放入children
    const parentMenu = asyncRoutes[item.menuCodeName]
    // 这里的判断是为了避免数据库添加了权限菜单但尚未在router/index.js中进行配置导致报错
    if (parentMenu === undefined) {
      return children
    }
    children.push(parentMenu)
    // 如果当前父节点没有children的话则创建一个
    if (!parentMenu.childrens) {
      parentMenu.children = []
    }

    // 既然进了下一层循环,要操作的数组自然是下一层children
    item.children.forEach(e => {
      generateRoutes(parentMenu.children, e)
    })
    // 为叶子节点时才去静态路由表里找
  } else if (item.menuCodeName) {
    children.push(asyncRoutes[item.menuCodeName])
  }
  console.log(children)
  return children
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}
