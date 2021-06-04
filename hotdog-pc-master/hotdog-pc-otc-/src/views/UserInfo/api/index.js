import request from '@/libs/axios';
import { baseUrl } from '../../../../public/config';

export function getAuthindentity() {
  return request({
    url: `${baseUrl}/m/authindentity`,
    method: 'get',
  });
}

export function getMember() {
  return request({
    url: `${baseUrl}/m/member`,
    method: 'get',
  });
}

export function getTradeInfo(memberId) {
  return request({
    url: `${baseUrl}/otc/trade/queryMerchantTradeInfo/${memberId}`,
    method: 'get',
  });
}
export function getOTCOrder(params) {
  return request({
    url: `${baseUrl}/otc/order/getOTCOrder`,
    method: 'get',
    params,
  });
}

// 获取用户押金、注册时间
export function getMerchantInfo(params) {
  return request({
    url: `${baseUrl}/otc/merchant/getInfo`,
    method: 'get',
    params,
  });
}
// 获取收款账号
export function getAccountId() {
  return request({
    url: `${baseUrl}/otc/AccountInfo/selectByCondition`,
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
// 根据币种查当前用户的资金
export function getMoneyByCoinType({ memberId, currency }) {
  return request({
    url: `${baseUrl}/otc/Account/select/${memberId}/${currency}`,
    method: 'get',
  });
}
