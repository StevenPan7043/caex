import request from '@/libs/axios';
import { baseUrl } from '../../../../public/config';

export function getMember() {
  return request({
    url: `${baseUrl}/m/member`,
    method: 'get',
  });
}

export function getMoneyInfo(memberId) {
  return request({
    url: `${baseUrl}/otc/Account/select/${memberId}`,
    method: 'get',
  });
}


export function createMerchant({ memberId, isDeposit }) {
  return request({
    url: `${baseUrl}/otc/merchant/create/${memberId}/${isDeposit}`,
    method: 'post',
  });
}
export function getMerchantInfo() {
  return request({
    url: `${baseUrl}/otc/merchant/getInfo`,
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
// 获取押金信息
export function getDepositInfo() {
  return request({
    url: `${baseUrl}/otc/Account/getDepositInfo`,
    method: 'get',
  });
}
export function quirMerchant({ securityPwd, memo }) {
  return request({
    url: `${baseUrl}/otc/merchant/secede/${securityPwd}/${memo}`,
    method: 'post',
  });
}
