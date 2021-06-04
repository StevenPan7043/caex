import request from '@/libs/axios'
import { baseUrl } from '../../public/config'

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

export function getMail (token, code, useraccount, type) {
  return request({
    url: baseUrl + '/m/mail/' + token + '/' + code + '/' + useraccount + '/' + type,
    method: 'get'
  })
}

export function apiToken (data) {
  return request({
    url: baseUrl + '/m/apitoken',
    data: data,
    method: 'post'
  })
}

export function putApiToken (data) {
  return request({
    url: baseUrl + '/m/apitoken',
    data: data,
    method: 'put'
  })
}

export function getApiTokens () {
  return request({
    url: baseUrl + '/m/apitokens',
    method: 'get'
  })
}
export function deleteApiToken (data) {
  return request({
    url: baseUrl + '/m/apitoken',
    data: data,
    method: 'DELETE'
  })
}
