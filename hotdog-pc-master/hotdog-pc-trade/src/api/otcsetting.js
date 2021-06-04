import request from '@/libs/axios'
import request2 from '@/libs/axios2'
import { baseUrl } from '../../public/config'

export function getOtcAccountInfo () {
  return request({
    url: baseUrl + '/o/otc/account_info',
    method: 'get'
  })
}
export function getAliOssPolicy () {
  return request({
    url: baseUrl + '/m/aliOssPolicy',
    method: 'get'
  })
}
export function postImg (url, formData) {
  return request2({
    url: url,
    data: formData,
    method: 'post'
  })
}
export function otcAccountInfo (data) {
  return request({
    url: baseUrl + '/o/otc/account_info',
    data: data,
    method: 'post'
  })
}
