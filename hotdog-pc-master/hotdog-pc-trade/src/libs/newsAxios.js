import axios from 'axios'
import Cookies from 'js-cookie'

import {
  Message
} from 'element-ui'
import {
  getLang
} from './utils'

var service = axios.create({

 

})

service.interceptors.request.use(
  config => {
    // config.headers['ZZEX_TOKEN'] = Cookies.get('ZZEX_TOKEN')
    return config
  },

)

service.interceptors.response.use(
  response => {
    let lang = getLang()
    if (!lang) {
      lang = 'zh'
    }
    if (response.data) {
      const res = response.data
      if (res.success === 1) {
        return res
      }
      if (res.state === 1) {
        return response.data
      } else if (res.state === -1) {
        if (res.msg === 'LANG_NO_LOGIN') {
          return res
        }
        let index = message_arr.indexOf(res.msg)

        if (lang == 'zh') {
          Message({
            message: message_arr_zh[index],
            type: 'error',
            duration: 3000,
            showClose: true
          })
        } else if(lang == 'en') {
          Message({
            message: message_arr_en[index],
            type: 'error',
            duration: 3000,
            showClose: true
          })
        } else if(lang == 'ko') {
          Message({
            message: message_arr_ko[index],
            type: 'error',
            duration: 3000,
            showClose: true
          })
        } else if(lang == 'ja') {
          Message({
            message: message_arr_ja[index],
            type: 'error',
            duration: 3000,
            showClose: true
          })
        }

        return res
      } else {
        return res
      }
    } else {
      return response.data
    }
  },
  error => {
    // for debug
    // Message({
    //   message: error.message,
    //   type: 'error',
    //   duration: 5 * 1000
    // })
    return Promise.reject(error)
  }
)

export default service