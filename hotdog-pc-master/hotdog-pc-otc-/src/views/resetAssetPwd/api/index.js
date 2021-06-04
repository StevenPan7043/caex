import request from '@/libs/axios';
import { baseUrl } from '../../../../public/config';

export function getKaptcha() {
  return request({
    url: `${baseUrl}/m/kaptcha`,
    method: 'get',
  });
}

export function setSecPwd(data) {
  return request({
    url: `${baseUrl}/m/setSecPwd`,
    data,
    method: 'post',
  });
}
