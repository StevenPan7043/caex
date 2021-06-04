import request from '@/libs/axios'
import { baseUrl } from '../../public/config'

export function getMembers () {
  return request({
    url: baseUrl + '/m/member',
    method: 'get'
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
    url: baseUrl + '/m/mail/' + token + '/' + code + '/' + useraccount + '/' + type,
    method: 'get'
  })
}

export function resetPwd (data) {
  return request({
    url: baseUrl + '/m/resetPwd',
    method: 'post',
    data: data
  })
}
export function getMemberLogout () {
  return request({
    url: baseUrl + '/m/member/logout',
    method: 'get'
  })
}
export function isSetCoinPwd () {
  return request({
    url: baseUrl + '/m/isSetCoinPwd',
    method: 'get'
  })
}
export function setSecPwd (data) {
  return request({
    url: baseUrl + '/m/setSecPwd',
    method: 'post',
    data: data
  })
}
