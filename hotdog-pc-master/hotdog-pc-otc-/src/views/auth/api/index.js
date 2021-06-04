import request from '@/libs/axios';
import request2 from '@/libs/axios2';
import { baseUrl } from '../../../../public/config';

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
export function authIdentity(data) {
  return request({
    url: `${baseUrl}/m/authIdentity`,
    data,
    method: 'post',
  });
}
// 获取实名认证情况
export function getAuthindentity() {
  return request({
    url: `${baseUrl}/m/authindentity`,
    method: 'get',
  });
}
