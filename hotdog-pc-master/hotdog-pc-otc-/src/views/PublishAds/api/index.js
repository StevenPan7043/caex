import request from '@/libs/axios';
import { baseUrl } from '../../../../public/config';

export function publishOTCOrder(params) {
  return request({
    url: `${baseUrl}/otc/order/publishOTCOrder`,
    method: 'post',
    data: params,
  });
}

export function getMember() {
  return request({
    url: `${baseUrl}/m/member`,
    method: 'get',
  });
}

export function getOTCList() {
  return request({
    url: `${baseUrl}/otc/currency/getList`,
    method: 'get',
  });
}

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
// 获取各币种与zc的兑换比率
export function getAllTicker() {
  return request({
    url: `${baseUrl}/m/allticker/${Date.parse(new Date())}`,
    method: 'get',
  });
}
