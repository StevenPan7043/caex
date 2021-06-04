import request from '@/libs/axios'
import { baseUrl } from '../../public/config'

export function getMember () {
  return request({
    url: baseUrl + '/m/member',
    method: 'get'
  })
}