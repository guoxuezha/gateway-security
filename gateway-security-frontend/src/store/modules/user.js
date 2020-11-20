import { login, logout, getInfo } from '@/api/auth/login'
import { getToken, setToken, removeToken } from '@/utils/auth'
import { resetRouter } from '@/router'
const getDefaultState = () => {
  return {
    token: getToken(),
    name: '',
    avatar: '',
    userObj: undefined,
    company: undefined
  }
}

// 初始化token
const state = getDefaultState()

// 值变化的方法
const mutations = {
  RESET_STATE: (state) => {
    Object.assign(state, getDefaultState())
  },
  SET_TOKEN: (state, token) => {
    state.token = token
  },
  SET_USER: (state, userObj) => {
    state.userObj = userObj
  },
  SET_NAME: (state, name) => {
    state.name = name
  },
  SET_AVATAR: (state, avatar) => {
    state.avatar = avatar
  },
  SET_MENUS: (state, menus) => {
    state.menus = menus
  },
  SET_COMPANY: (state, company) => {
    state.company = company
  }
}

const actions = {

  // 用户登录
  login({ commit }, userInfo) {
    console.log('modules/user/login => 用户登录')
    return new Promise((resolve, reject) => {
      login(userInfo).then(response => {
        console.log(response.data.token)
        commit('SET_TOKEN', response.data.token)
        setToken(response.data.token)
        resolve()
      }).catch(error => {
        reject(error.msg)
      })
    })
  },

  // 通过token获取用户基本信息
  getInfo({ commit, state }) {
    console.log('modules/user/getInfo => 获取用户基本信息')
    return new Promise((resolve, reject) => {
      // 通过token访问后台,再获取用户其他信息
      getInfo(state.token).then(response => {
        const { user, menus, company } = response.data
        console.log(user)
        const userName = user.userName
        if (!userName) {
          return reject('验证失败，请重新登陆！')
        }
        commit('SET_USER', user)
        commit('SET_NAME', userName)
        commit('SET_MENUS', menus)
        commit('SET_COMPANY', company)
        console.log('如果执行到这里则说明getUserByToken没错')
        resolve(response.data)
      }).catch(error => {
        reject(error)
      })
    })
  },

  // 用户登出
  logout({ commit, state }) {
    console.log('modules/user/getInfo => 用户登出')
    return new Promise((resolve, reject) => {
      logout(state.token).then(response => {
        // 先把token清除
        removeToken()
        // 再清除路由
        resetRouter()
        commit('RESET_STATE')
        resolve()
      }).catch(error => {
        reject(error)
      })
    })
  },

  // 清空token
  resetToken({ commit }) {
    console.log('modules/user/getInfo => 清空token')
    return new Promise(resolve => {
      removeToken() // must remove  token  first
      commit('RESET_STATE')
      resolve()
    })
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}

