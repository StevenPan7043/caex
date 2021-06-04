import request from '@/libs/axios';
import { baseUrl } from '../../../../public/config';


export function getGtValidateCode() {
  return request({
    url: `${baseUrl}/m/gtValidateCode`,
    method: 'get',
  });
}

export function memberLogin(data) {
  return request({
    url: `${baseUrl}/m/memberLogin`,
    data,
    method: 'post',
  });
}

export function getMember() {
  return request({
    url: `${baseUrl}/m/member`,
    method: 'get',
  });
}
export function getMail({
  token, code, useraccount, type,
}) {
  return request({
    url: `${baseUrl}/m/mail/${token}/${code}/${useraccount}/${type}?type=app`,
    method: 'get',
  });
}
export function nextLogin({ token, code, mName }) {
  return request({
    url: `${baseUrl}/m/login/verification/${mName}/${code}?token=${token}&last_login_device=3`,
    method: 'get',
  });
}
