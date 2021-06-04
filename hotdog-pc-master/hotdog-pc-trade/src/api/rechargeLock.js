import request from '@/libs/axios'
import { baseUrl } from '../../public/config'

export function getAssetsRechargeAddr (currency) {
  return request({
    url: baseUrl + '/a/assetsRechargeAddr/' + currency + '/' + Date.parse(new Date()),
    method: 'get'
  })
}
export function genCoinAddress (data) {
  return request({
    url: baseUrl + '/a/genCoinAddress',
    data: data,
    method: 'post'
  })
}

export function getMember () {
  return request({
    url: baseUrl + '/m/member',
    method: 'get'
  })
}

export function getLockList (memberId) {
  return request({
    url: baseUrl + '/locked/lockrecharge/member_id/' + memberId,
    method: 'get'
  })
}
