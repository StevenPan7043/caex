import request from '@/libs/axios';
import { baseUrl } from '../../../../public/config';

export function getKaptcha() {
  return request({
    url: `${baseUrl}/m/kaptcha`,
    method: 'get',
  });
}
export function forgotV2(data) {
  return request({
    url: `${baseUrl}/m/forgotV2`,
    data,
    method: 'post',
  });
}
