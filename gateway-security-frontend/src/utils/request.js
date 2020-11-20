import axios from 'axios'
import { MessageBox, Message } from 'element-ui'
import store from '@/store'
import { getToken } from '@/utils/auth'

// 创建Axios实例
const service = axios.create({
  baseURL: process.env.VUE_APP_BASE_API, // url = base url + request url
  // withCredentials: true, // send cookies when cross-domain requests
  timeout: 50000 // request timeout
})

// request拦截器,用于自定义请求头中属性
service.interceptors.request.use(
  config => {
    // do something before request is sent
    if (store.getters.token) {
      // 如果store对象中的token非空,则直接把token加入每一次请求的请求头中
      // 这里的Authorization需要跟后台SpringSecurity配置拦截器的Key相一致
      config.headers['Authorization'] = getToken()
    }
    return config
  },
  error => {
    console.log('发送request请求时发生错误:' + error)
    return Promise.reject(error)
  }
)

// response拦截器,用于接收响应时执行统一操作
service.interceptors.response.use(
  /**
   * If you want to get http information such as headers or status
   * Please return  response => response
  */

  /**
   * Determine the request status by custom code
   * Here is just an example
   * You can also judge the status by HTTP Status Code
   */
  response => {
    const res = response.data

    // response的data.code(ReseponseResult)或data.data.code(PrintWriter.write)的成功码为20000
    if (res.code !== 20000) {
      Message({
        message: res.message || res.msg || '发生系统响应错误！响应码不为20000',
        type: 'error',
        duration: 3 * 1000
      })

      // 以下为自定义错误响应code以及提示
      // 50008: Illegal token; 50012: Other clients logged in; 50014: Token expired;
      if (res.code === 50008 || res.code === 50012 || res.code === 50014) {
        // to re-login
        MessageBox.confirm('You have been logged out, you can cancel to stay on this page, or log in again', 'Confirm logout', {
          confirmButtonText: 'Re-Login',
          cancelButtonText: 'Cancel',
          type: 'warning'
        }).then(() => {
          store.dispatch('user/resetToken').then(() => {
            location.reload()
          })
        })
      }
      return Promise.reject(new Error(res.message || res.meg || 'Error'))
    } else {
      return res
    }
  },
  error => {
    console.log('err' + error) // for debug
    Message({
      message: error.message || error.msg || '',
      type: 'error',
      duration: 3 * 1000
    })
    return Promise.reject(error)
  }
)

export default service
