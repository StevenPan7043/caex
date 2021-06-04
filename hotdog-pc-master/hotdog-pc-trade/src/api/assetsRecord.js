import request from '@/libs/axios'
import { baseUrl } from '../../public/config'

export function getAssetsRecharge (page,uid) {
  return request({
    url: baseUrl + '/a/assetsRecharge/' + page + '/' + Date.parse(new Date()),
    method: 'get',
    params:{
      "member_id":uid,
    }
  })
}

export function getAssetsWithDraw (page,uid) {
  return request({
    url: baseUrl + '/a/assetsWithDraw/' + page + '/' + Date.parse(new Date()),
    method: 'get',
    params:{
      "member_id":uid,
    }
  })
}

export function getWarehouseList (page) {
  return request({
    url: baseUrl + '/a/getWarehouseList/' + page + '/' + Date.parse(new Date()),
    method: 'get'
  })
}
