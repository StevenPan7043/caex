import request from '@/libs/axios';
import { baseUrl, USDTPriceUrl } from '../../../../public/config';


export function getMember() {
  return request({
    url: `${baseUrl}/m/member`,
    method: 'get',
  });
}
export function getUserCurrency(memberId) {
  return request({
    url: `${baseUrl}/otc/Account/select/${memberId}`,
    method: 'get',
  });
}

export function getCurrencyList() {
  return request({
    url: `${baseUrl}/otc/currency/getList`,
    method: 'get',
  });
}
export function getAssetsLst() {
  return request({
    url: `${baseUrl}/m/assetsLst/${Date.parse(new Date())}`,
    method: 'get',
  });
}

export function accountAddChange({ memberId, currency, num }) {
  return request({
    url: `${baseUrl}/otc/Account/addChange/${memberId}/${num}/${currency}`,
    method: 'post',
  });
}

export function accountSubtChange({ memberId, currency, num }) {
  return request({
    url: `${baseUrl}/otc/Account/subtChange/${memberId}/${num}/${currency}`,
    method: 'post',
  });
}
export function getAccountDetail(params) {
  return request({
    url: `${baseUrl}/otc/Account/getMemberDetailsPage`,
    method: 'get',
    params,
  });
}
export function getMerchantInfo() {
  return request({
    url: `${baseUrl}/otc/merchant/getInfo`,
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

// 获取资产
export function getAssets() {
  return request({
    url: `${baseUrl}/a/getAssetsForBbAndFb`,
    method: 'get',
  });
}

// 获取usdt兑换zc比率
export function getUsdtPrice() {
  return request({
    url: `${USDTPriceUrl}`,
    method: 'get',
  });
}
