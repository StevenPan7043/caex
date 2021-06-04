import request from '@/libs/axios';
import { baseUrl } from '../../../../public/config';


export function getTradePage(params) {
  return request({
    url: `${baseUrl}/otc/trade/queryTradePage`,
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

export function getTradingPage(params) {
  return request({
    url: `${baseUrl}/otc/trade/queryTradingPage`,
    method: 'get',
    params,
  });
}

// 申诉中
export function getComplainingPage(params) {
  return request({
    url: `${baseUrl}/otc/trade/queryComplainingPage`,
    method: 'get',
    params,
  });
}
