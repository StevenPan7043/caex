import request from '@/libs/axios';
import { baseUrl } from '../../../../public/config';

export function getOTCAds(params) {
  return request({
    url: `${baseUrl}/otc/order/getOTCAds`,
    method: 'get',
    params,
  });
}
export function getMember() {
  return request({
    url: `${baseUrl}/m/member`,
    method: 'get',
  });
}

export function getCurrencyList() {
  return request({
    url: `${baseUrl}/otc/currency/getList`,
    method: 'get',
  });
}

export function cancelOrderApi(id) {
  return request({
    url: `${baseUrl}/otc/order/cancel/${id}`,
    method: 'put',
  });
}
// 查询是否是商家
export function getMerchantInfo() {
  return request({
    url: `${baseUrl}/otc/merchant/getInfo`,
    method: 'get',
  });
}
