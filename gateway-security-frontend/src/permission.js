import router from './router'
import store from './store'
import { Message } from 'element-ui'
import NProgress from 'nprogress' // progress bar
import 'nprogress/nprogress.css' // progress bar style
import { getToken } from '@/utils/auth' // get token from cookie
import getPageTitle from '@/utils/get-page-title'

NProgress.configure({ showSpinner: false }) // NProgress Configuration

const whiteList = ['/login'] // no redirect whitelist

// 前端拦截器,用于执行登陆校验
router.beforeEach(async (to, from, next) => {
  // start progress bar
  NProgress.start()

  // 先把要跳转的页面的title加载出来
  document.title = getPageTitle(to.meta.title)

  // 查看是否存在token
  const hasToken = getToken()

  if (hasToken) {
    console.log('hasToken === true!')
    if (to.path === '/login') {
      // 如果token存在且要跳转的路径为login,则直接送进主页面dashboard
      next({ path: '/' })
      NProgress.done()
    } else {
      // 当存在token但访问的不是login页面,则查看用户名
      const userName = store.getters.name
      if (userName) {
        // 用户名存在则直接进入
        next()
      } else {
        try {
          // 没有用户名则尝试通过token获取用户信息
          const { menus } = await store.dispatch('user/getInfo')
          const accessRoutes = await store.dispatch('permission/menusToRoutes', menus)
          router.addRoutes(accessRoutes)
          next({ ...to, replace: true })
        } catch (error) {
          // 如果没有获取到用户信息,则提示重新登陆
          await store.dispatch('user/resetToken')
          Message.error(error || '未能获取到用户信息，请重新登陆！')
          next(`/login?redirect=${to.path}`)
          NProgress.done()
        }
      }
    }
  } else {
    console.log('hasToken === false!')
    // 但如果直接没能获取到token,则无限返回上一层,即无限停留在login页面
    if (whiteList.indexOf(to.path) !== -1) {
      next()
    } else {
      // other pages that do not have permission to access are redirected to the login page.
      next(`/login?redirect=${to.path}`)
      NProgress.done()
    }
  }
})

router.afterEach(() => {
  // finish progress bar
  NProgress.done()
})
