import request from '@/libs/axios';
import { baseUrl } from '../../../../public/config';

export function getMerchantInfo() {
  return request({
    url: `${baseUrl}/otc/merchant/getInfo`,
    method: 'get',
  });
}
// 获取正在交易中订单
export function getTradingPage() {
  return request({
    url: `${baseUrl}/otc/trade/queryTradingPage`,
    method: 'get',
  });
}
// 获取token
export function getSSOToken() {
  return request({
    url: `${baseUrl}/m/getSSOToken`,
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
export function getMember() {
  return request({
    url: `${baseUrl}/m/member`,
    method: 'get',
  });
}
