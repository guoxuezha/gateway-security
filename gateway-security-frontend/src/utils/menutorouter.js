import Layout from '@/layout'

// 静态路由表
const asyncRoutes = {
  /* ==================================资产管理=================================== */
  // 一级菜单 资产管理
  'assets': {
    path: '/assets',
    component: Layout,
    name: 'assets',
    meta: {
      title: '资产管理',
      icon: 'nested'
    }
  },
  // 二级菜单 资产类型
  'assetsType': {
    path: '/type',
    component: () => import('@/views/assets/type/index'),
    name: 'assetsType',
    meta: {
      title: '资产类型'
    }
  },
  // 三级菜单 新增资产类型
  'assetsTypeEdit': {
    path: '/type-edit',
    component: () => import('@/views/assets/type/edit'),
    name: 'assetsTypeEdit',
    meta: {
      title: '新增资产类型'
    }
  },
  // 三级菜单 资产类型列表
  'assetsTypeList': {
    path: '/type-list',
    component: () => import('@/views/assets/type/list'),
    name: 'assetsTypeList',
    meta: {
      title: '资产类型列表'
    }
  },
  // 二级菜单 硬件资产
  'hardware': {
    path: '/hardware',
    component: () => import('@/views/assets/hardware/index'),
    name: 'hardware',
    meta: {
      title: '硬件资产'
    }
  },
  // 三级菜单 新增硬件资产
  'hardwareEdit': {
    path: '/hardware-edit',
    component: () => import('@/views/assets/hardware/edit'),
    name: 'hardwareEdit',
    meta: {
      title: '新增硬件资产'
    }
  },
  // 三级菜单 硬件资产列表
  'hardwareList': {
    path: '/hardware-list',
    component: () => import('@/views/assets/hardware/list'),
    name: 'hardwareList',
    meta: {
      title: '硬件资产列表'
    }
  },
  // 二级菜单 软件资产
  'software': {
    path: '/software',
    component: () => import('@/views/assets/software/index'),
    name: 'software',
    meta: {
      title: '软件资产'
    }
  },
  // 三级菜单 新增软件资产
  'softwareEdit': {
    path: '/software-edit',
    component: () => import('@/views/assets/software/edit'),
    name: 'softwareEdit',
    meta: {
      title: '新增软件资产'
    }
  },
  // 三级菜单 软件资产列表
  'softwareList': {
    path: '/software-list',
    component: () => import('@/views/assets/software/list'),
    name: 'softwareList',
    meta: {
      title: '软件资产列表'
    }
  },
  /* ==================================用户管理=================================== */
  // 一级菜单 用户管理
  'auth': {
    path: '/auth',
    component: Layout,
    name: 'auth',
    meta: {
      title: '用户管理',
      icon: 'nested'
    }
  },
  // 二级菜单 用户列表
  'sysuser': {
    path: '/sysuser',
    component: () => import('@/views/auth/sysuser/index'),
    name: 'sysuser',
    meta: {
      title: '用户列表'
    }
  },
  // 二级菜单 角色列表
  'sysrole': {
    path: '/sysrole',
    component: () => import('@/views/auth/sysrole/index'),
    name: 'sysrole',
    meta: {
      title: '角色列表'
    }
  },
  // 二级菜单 菜单列表
  'frontendMenu': {
    path: '/frontendMenu',
    component: () => import('@/views/auth/frontendmenu/index'),
    name: 'frontendMenu',
    meta: {
      title: '菜单列表'
    }
  }
}

// 将菜单信息转成对应的路由信息 动态添加
export function menusToRoutes(data) {
  const result = []
  const children = []

  // 先给根节点设置好三个基础菜单项
  result.push(
    {
      path: '/login',
      component: () => import('@/views/login/index'),
      hidden: true
    },

    /* {
      path: '/404',
      component: () => import('@/views/404'),
      hidden: true
    }, */

    {
      path: '/',
      component: Layout,
      redirect: '/dashboard',
      children: [{
        path: 'dashboard',
        name: 'Dashboard',
        component: () => import('@/views/dashboard/index'),
        meta: {
          title: '仪表盘',
          icon: 'dashboard'
        }
      }]
    }
  )

  /**
   * 方案一:
   * 1.先把列表转为树形结构
   * 2.遍历该树形结构,根据menuCodeName映射生成另一棵由静态路由表中元素构成的树
   */
  // 先把菜单列表转为树形结构
  data.forEach(menu => {
    const menuPid = menu.menuPid
    if (menuPid !== 0) {
      data.forEach(Menu => {
        if (Menu.menuId === menuPid) {
          if (!Menu.children) {
            Menu.children = []
          }
          Menu.children.push(menu)
        }
      })
    }
  })
  // 只保留一级菜单
  data = data.filter(menu => menu.menuSort === 1)

  // 解析menu树,构造动态菜单
  data.forEach(menu => {
    generateRoutes(children, menu)
  })

  children.forEach(menu => {
    result.push(menu)
  })

  // 最后添加404页面 否则会在登陆成功后跳到404页面
  result.push(
    {
      path: '*',
      redirect: '/404',
      hidden: true
    }
  )
  return result
}

// 向菜单树中添加节点
function generateRoutes(children, item) {
  if (item.children) {
    // 先把该节点放入children
    const parentMenu = asyncRoutes[item.menuCodeName]
    children.push(parentMenu)
    // 如果当前父节点没有children的话则创建一个
    if (!parentMenu.children) {
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
}

