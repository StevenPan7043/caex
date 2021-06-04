import request from '@/libs/axios';
import { baseUrl } from '../../../../public/config';


export function getOrderDetailApi(id) {
  return request({
    url: `${baseUrl}/otc/trade/query/${id}`,
    method: 'get',
  });
}

export function getOppositeAccountInfo(id) {
  return request({
    url: `${baseUrl}/otc/trade/getOrderAccountInfo/${id}`,
    method: 'get',
  });
}
export function getTradeInfo(memberId) {
  return request({
    url: `${baseUrl}/otc/trade/queryMerchantTradeInfo/${memberId}`,
    method: 'get',
  });
}
export function payTrade({ id, acountId }) {
  return request({
    url: `${baseUrl}/otc/trade/pay/${id}/${acountId}`,
    method: 'put',
  });
}
export function cancelTrade(id, reason) {
  return request({
    url: `${baseUrl}/otc/trade/cancel/${id}/${reason}`,
    method: 'put',
  });
}
export function tradeConfirmed(id) {
  return request({
    url: `${baseUrl}/otc/trade/confirmed/${id}`,
    method: 'put',
  });
}
// 交易申诉

export function tradeComplain({ id, complainType, memo }) {
  return request({
    url: `${baseUrl}/otc/trade/complain/${id}/${complainType}/${memo}`,
    method: 'put',
  });
}
// 取消申诉
export function cancelComplain(id) {
  return request({
    url: `${baseUrl}/otc/trade/cancelComplain/${id}`,
    method: 'put',
  });
}

// 获取用户session
export function getJsSession() {
  return request({
    url: `${baseUrl}/m/getJsSession`,
    method: 'get',
  });
}
// 获取聊天记录
export function getRecordPage(params) {
  return request({
    url: `${baseUrl}/otc/wchar/getRecordPage`,
    method: 'get',
    params,
  });
}
// 获取登陆状态
export function getMember() {
  return request({
    url: `${baseUrl}/m/member`,
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
