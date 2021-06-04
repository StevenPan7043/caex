import request from '@/libs/axios';
import { baseUrl } from '../../public/config';

// 获取验证码
export function getMail({
  token, code, useraccount, type,
}) {
  return request({
    url: `${baseUrl}/m/mail/${token}/${code}/${useraccount}/${type}`,
    method: 'get',
  });
}

// 获取用户信息
export function getMember() {
  return request({
    url: `${baseUrl}/m/member`,
    method: 'get',
  });
}

// 登出接口
export function logoutApi() {
  return request({
    url: `${baseUrl}/m/member/logout`,
    method: 'get',
  });
}
