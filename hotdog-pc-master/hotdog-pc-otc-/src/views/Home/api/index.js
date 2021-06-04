import request from '@/libs/axios';
import { baseUrl } from '../../../../public/config';


export function getTradeOtcOrder(params) {
  return request({
    url: `${baseUrl}/otc/order/getTradeOTCOrder`,
    method: 'get',
    params,
  });
}

export function getCurrencyList() {
  return request({
    url: `${baseUrl}/otc/currency/getList`,
    method: 'get',
  });
}

export function getTradeInfo(memberId) {
  return request({
    url: `${baseUrl}/otc/merchant/getInfo/${memberId}`,
    method: 'get',
  });
}

export function addOTCOrder(data) {
  return request({
    url: `${baseUrl}/otc/order/addOTCOrder`,
    method: 'post',
    data,
  });
}
export function getMember() {
  return request({
    url: `${baseUrl}/m/member`,
    method: 'get',
  });
}
// 获取实名认证情况
export function getAuthindentity() {
  return request({
    url: `${baseUrl}/m/authindentity`,
    method: 'get',
  });
}

// 获取收款账号
export function getAccountId() {
  return request({
    url: `${baseUrl}/otc/AccountInfo/selectByCondition`,
    method: 'get',
  });
}
// 根据币种查当前用户的资金
export function getMoneyByCoinType({ memberId, currency }) {
  return request({
    url: `${baseUrl}/otc/Account/select/${memberId}/${currency}`,
    method: 'get',
  });
}
export function tokenLogin(data) {
  return request({
    url: `${baseUrl}/m/tokenLogin`,
    data,
    method: 'post',
  });
}
export function getMoneyInfo(memberId) {
  return request({
    url: `${baseUrl}/otc/Account/select/${memberId}`,
    method: 'get',
  });
}
// 发送消息
export function sendMsgApi(data) {
  return request({
    url: `${baseUrl}/otc/wchar/insertChar`,
    method: 'post',
    data,
  });
}
