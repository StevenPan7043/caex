import request from '@/libs/axios'
import { baseUrl } from '../../public/config'

export function getAssetsLst () {
  return request({
    url: baseUrl + '/m/assetsLst/' + Date.parse(new Date()),
    method: 'get'
  })
}

export function getKaptcha () {
  return request({
    url: baseUrl + '/m/kaptcha',
    method: 'get'
  })
}
export function getMember () {
  return request({
    url: baseUrl + '/m/member',
    method: 'get'
  })
}

export function saveWithdrawAddr (currency, addrlabel, addr, smscode,chain) {

  let data = {
    'currency': currency,
    'addr_label': addrlabel,
    'addr': addr,
    'sms_code': smscode
  }
  if (currency==='USDT'){
    data['currency_chain_type']=chain
  }
  return request({
    url: baseUrl + '/a/saveWithdrawAddr',
    method: 'post',
    data: data
  })
}

export function getWithdrawAddrs (currency) {
  return request({
    url: baseUrl + '/a/withdrawAddr/' + currency + '/' + Date.parse(new Date()),
    method: 'get'
  })
}

export function delWithdrawAddr (data) {
  return request({
    url: baseUrl + '/a/delWithdrawAddr',
    method: 'POST',
    data: data
  })
}
