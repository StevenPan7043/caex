import request from '@/libs/axios'
import { baseUrl } from '../../public/config'

export function getMember () {
  return request({
    url: baseUrl + '/m/member',
    method: 'get'
  })
}

export function getOtcAdsList (baseCurrency, quoteCurrency, adType, page, pageSize) {
  return request({
    url: baseUrl + '/o/otc/ads/' + baseCurrency + '/' + quoteCurrency + '/' + adType + '/' + page + '/' + pageSize,
    method: 'get'
  })
}

export function getIsSetCoinPwd () {
  return request({
    url: baseUrl + '/m/isSetCoinPwd',
    method: 'get'
  })
}

export function getOtcAccountInfo () {
  return request({
    url: baseUrl + '/o/otc/account_info',
    method: 'get'
  })
}

export function otcAddorder (data) {
  return request({
    url: baseUrl + '/o/otc/addorder',
    data:data,
    method: 'post'
  })
}
