import request from '@/libs/axios'
import { baseUrl } from '../../public/config'

export function getGtValidateCode () {
  return request({
    url: baseUrl + '/m/gtValidateCode',
    method: 'get'
  })
}

export function memberLogin (data) {
  return request({
    url: baseUrl + '/m/memberLogin',
    data: data,
    method: 'post'
  })
}

export function getKaptcha () {
  return request({
    url: baseUrl + '/m/kaptcha',
    method: 'get'
  })
}
export function getMail (token, code, useraccount, type) {
  return request({
    url: baseUrl + '/m/mail/' + token + '/' + code + '/' + useraccount + '/' + type+'?type=app',
    method: 'get'
  })
}

export function getMember () {
  return request({
    url: baseUrl + '/m/member',
    method: 'get'
  })
}
// 获取token
export function getSSOToken() {
  return request({
    url: `${baseUrl}/m/getSSOToken`,
    method: 'get',
  });
}
export function verification(useraccount,code,token){
  return request({
    url: `${baseUrl}/m/login/verification/`+useraccount+'/'+code+'?token='+token+'&last_login_device=3',
  })
}

export function verifyLabAuth() {
  return request({
    url: baseUrl + '/dataLab/getFeeCurrencyPairList',
    method: 'get'
  })
}
