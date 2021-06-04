import request from '@/libs/axios'
import { baseUrl } from '../../public/config'

export function getAssetsRechargeAddr (currency,select) {
  let params ={
    currencyChainType:select,
  }
  if (currency==='USDT') {
    return request({
      url: baseUrl + '/a/assetsRechargeAddr/' + currency + '/' + Date.parse(new Date()),
      method: 'get',
      params
    })
  }else{
    return request({
      url: baseUrl + '/a/assetsRechargeAddr/' + currency + '/' + Date.parse(new Date()),
      method: 'get',
    })
  }

}
export function genCoinAddress (data) {
  return request({
    url: baseUrl + '/a/genCoinAddress',
    data: data,
    method: 'post'
  })
}

export function getAssetsRecharge (page,member_id) {
  return request({
    url: baseUrl + '/a/assetsRecharge/' + page + '/' + Date.parse(new Date()),
    method: 'get',
    params:{
      "member_id":member_id,
    }
  })
}
export function getAssetsLst () {
  return request({
    url: baseUrl + '/m/assetsLst/' + Date.parse(new Date()),
    method: 'get'
  })
}
//获取锁仓币种
export function getLockCoinList () {
  return request({
    url: baseUrl + '/a/getRuleList',
    method: 'get'
  })
}

//获取锁仓记录

export function getLockRecord (params) {
  return request({
    url: baseUrl + '/a/getWarehouseList',
    method: 'post',
    params
  })
}

export function getMember () {
  return request({
    url: baseUrl + '/m/member',
    method: 'get'
  })
}
