import request from '@/libs/axios'
import { baseUrl } from '../../public/config'

// 获取资产
export function getAssets() {
  return request({
    url: `${baseUrl}/a/getAssetsForBbAndFb`,
    method: 'get',
  });
}

// 获取全仓划转记录
export function getQcRecordAjax(params) {
  return request({
    url: `${baseUrl}/contract/getMemberDetailsPage`,
    method: 'get',
    params
  });
}

// 获取逐仓划转记录
export function getZcRecordAjax(params) {
  return request({
    url: `${baseUrl}/contract/getMemberZcDetailsPage`,
    method: 'get',
    params
  });
}

export function bindingInviteCode (inviteCode) {
  return request({
    url: `${baseUrl}/contract/bindingInviteCode?invite_code=${inviteCode}`,
    method: 'get',
  });
}



export function swap () {
  return request({
    url: `/swap-ex/market/history/kline?contract_code=BTC-USD&period=1day&from=1587052800&to=1591286400`,
    method: 'get',
  });
}



