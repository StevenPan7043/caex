import request from '@/libs/axios'
import { baseUrl } from '../../public/config'

export function getLoginlog () {
  return request({
    url: baseUrl + '/m/loginlog',
    method: 'get'
  })
}
