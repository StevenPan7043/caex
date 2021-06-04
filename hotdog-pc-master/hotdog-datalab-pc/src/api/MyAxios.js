import axios from 'axios';
import { Message } from 'element-ui';
const config = {
    // baseURL:'http://localhost:3000/'
    // baseURL:'http://47.75.218.160:8884',
    // baseURL:'http://192.168.2.10:8081',
    // baseURL: 'http://47.89.13.21:8080',
    baseURL:'',
}
const myAxios = axios.create(config);

myAxios.interceptors.request.use(
  config => {
    let token = localStorage.getItem('token')
    if(token===null){
      token='web';
    }
    config.headers={
      'PEPPAEX_TOKEN':token

    }
    return config
  },

)


myAxios.interceptors.response.use(function (response) {
    // 对响应数据做点什么
    if(response.data.msg == 'LANG_NO_LOGIN') {
       Message({
        message: '请先登陆',
        type: 'error',
        duration: 3000,
        showClose: true,
        onClose: function() {
          window.location.href= response.config.baseURL + "/#/login"; 
        }
      })
    }
    return response;
  }, function (error) {
      console.log(error)
    // 对响应错误做点什么
    return Promise.reject(error);
  });
export default myAxios;