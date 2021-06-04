import request from '@/libs/axios';
import request2 from '@/libs/axios2';
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
export function modifyNickName({ id, nickName }) {
  return request({
    url: `${baseUrl}/m/modifyNickName/${id}/${nickName}`,
    method: 'put',
  });
}
export function setSecPwd(params) {
  return request({
    url: `${baseUrl}/m/setSecPwd`,
    method: 'post',
    data: params,
  });
}
export function createPayment(params) {
  return request({
    url: `${baseUrl}/otc/AccountInfo/create`,
    method: 'post',
    data: params,
  });
}
export function getKaptcha() {
  return request({
    url: `${baseUrl}/m/kaptcha`,
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
export function deleteAccount({ id, securityPwd }) {
  return request({
    url: `${baseUrl}/otc/AccountInfo/delete/${id}/${securityPwd}`,
    method: 'delete',
  });
}

export function getAliOssPolicy() {
  return request({
    url: `${baseUrl}/m/aliOssPolicy`,
    method: 'get',
  });
}
export function postImg(url, formData) {
  return request2({
    url,
    data: formData,
    method: 'post',
  });
}
