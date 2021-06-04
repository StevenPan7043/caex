import request from '@/libs/axios';
import { baseUrl } from '../../../../public/config';

export function getKaptcha() {
  return request({
    url: `${baseUrl}/m/kaptcha`,
    method: 'get',
  });
}
export function resetPwd(data) {
  return request({
    url: `${baseUrl}/m/resetPwd`,
    method: 'post',
    data,
  });
}
export function getMemberLogout() {
  return request({
    url: `${baseUrl}/m/member/logout`,
    method: 'get',
  });
}
