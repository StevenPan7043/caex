import axios from "axios";
import QS from "qs";
import { contractUrl } from '../../public/config'

// axios.defaults.timeout = 6000;
axios.defaults.baseURL = contractUrl;

//http request 拦截器
axios.interceptors.request.use(
  config => {
    // const token = getCookie('名称');注意使用的时候需要引入cookie方法，推荐js-cookie
    // config.data = JSON.stringify(config.data);

    // let token = localStorage.getItem('contactToken')
    // if (token === null) {
    //     token = 'web';
    // }
    config.headers = {
      "Content-Type": "application/x-www-form-urlencoded",
    };
    return config;
  },
  error => {
    return Promise.reject(error);
  }
);

//http response 拦截器
axios.interceptors.response.use(
  response => {
    if (response.data.errCode == 2) {
      //   router.push({
      //     path: "/login",
      //     querry: {
      //       redirect: router.currentRoute.fullPath
      //     } //从哪个页面跳转
      //   });
    }
    return response;
  },
  error => {
    return Promise.reject(error);
  }
);

/**
 * 封装get方法
 * @param url
 * @param data
 * @returns {Promise}
 */

export function get(url, params) {
  return new Promise((resolve, reject) => {
    axios
      .get(url, {
        params: params
      })
      .then(response => {
        resolve(response);
      })
      .catch(err => {
        reject(err);
      });
  });
}

/**
 * 封装post请求
 * @param url
 * @param data
 * @returns {Promise}
 */

export function post(url, data) {
  return new Promise((resolve, reject) => {
    axios.post(url, QS.stringify(data)).then(
      response => {
        resolve(response);
      },
      err => {
        reject(err);
      }
    );
  });
}
