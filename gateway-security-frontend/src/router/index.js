import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

/* Layout */
import Layout from '@/layout'

/**
 * Note: sub-menu only appear when route children.length >= 1
 * Detail see: https://panjiachen.github.io/vue-element-admin-site/guide/essentials/router-and-nav.html
 *
 * hidden: true                   if set true, item will not show in the sidebar(default is false)
 * alwaysShow: true               if set true, will always show the root menu
 *                                if not set alwaysShow, when item has more than one children route,
 *                                it will becomes nested mode, otherwise not show the root menu
 * redirect: noRedirect           if set noRedirect will no redirect in the breadcrumb
 * name:'router-name'             the name is used by <keep-alive> (must set!!!)
 * meta : {
    roles: ['admin','editor']    control the page roles (you can set multiple roles)
    title: 'title'               the name show in sidebar and breadcrumb (recommend set)
    icon: 'svg-name'/'el-icon-x' the icon show in the sidebar
    breadcrumb: false            if set false, the item will hidden in breadcrumb(default is true)
    activeMenu: '/example/list'  if set path, the sidebar will highlight the path you set
  }
 */

/**
 * constantRoutes
 * a base page that does not have permission requirements
 * all roles can be accessed
 */
export const constantRoutes = [
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

]

// 静态路由表
export const asyncRoutes = {
  /* ==================================用户管理=================================== */
  // 一级菜单 用户管理
  'auth': {
    path: '/auth',
    component: Layout,
    name: 'auth',
    meta: {
      title: '用户管理',
      icon: 'user'
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
  },
  // 二级菜单 公司列表
  'company': {
    path: '/company',
    component: () => import('@/views/auth/company/index'),
    name: 'company',
    meta: {
      title: '公司列表'
    }
  },
  // 二级菜单 部门列表
  'department': {
    path: '/department',
    component: () => import('@/views/auth/department/index'),
    name: 'department',
    meta: {
      title: '单位列表'
    }
  }
}

const createRouter = () => new Router({
  // mode: 'history', // require service support
  scrollBehavior: () => ({
    y: 0
  }),
  routes: constantRoutes
})

const router = createRouter()

// Detail see: https://github.com/vuejs/vue-router/issues/1234#issuecomment-357941465
// 重新设置路由
export function resetRouter() {
  const newRouter = createRouter()
  router.matcher = newRouter.matcher // reset router
}

export default router
