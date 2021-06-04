import request from '@/libs/axios'
import { baseUrl } from '../../public/config'

export function getMemberLogout () {
  return request({
    url: baseUrl + '/m/member/logout',
    method: 'get'
  })
}
