import request from '@/libs/axios'
import { baseUrl } from '../../public/config'

export function getOtcorders (baseCurrency, quoteCurrency, page, pageSize, adType, status) {
  return request({
    url: baseUrl + '/o/otc/orders/' + baseCurrency + '/' + quoteCurrency + '/' + page + '/' + pageSize + '/' + adType + '/' + status + '/' + Date.parse(new Date()),
    method: 'get'
  })
}
export function getOtcAds (id) {
  return request({
    url: baseUrl + '/o/otc/ads/' + id,
    method: 'get'
  })
}
export function confirmorder (data) {
  return request({
    url: baseUrl + '/o/otc/confirmorder',
    data: data,
    method: 'post'
  })
}
