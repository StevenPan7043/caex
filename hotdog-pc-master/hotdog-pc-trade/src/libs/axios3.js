import axios from 'axios'
import { contractKlienUrl } from '../../public/config'
var service = axios.create({
    baseURL: contractKlienUrl,
    timeout: 6000, // 请求超时时间 

  headers: { 'Content-Type': 'multipart/form-data' }
})
service.interceptors.request.use(
    config => {
        config.withCredentials = true
        return config
    },
)
service.interceptors.response.use(
    response => {
        if (response.data) {
            return response.data
        } else {
            return response.data
        }
    },
    error => {
        return Promise.reject(error)
    }
)
export default service
