import request from '@/libs/axios'
import { baseUrl } from '../../public/config'


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


export function bb2QcAjax({ memberId, currency, num }) {
  return request({
    url: `${baseUrl}/contract/balance/addChange/${memberId}/${num}/${currency}`,
    method: 'post',
  });
}


export function qc2BbAjax({ memberId, currency, num }) {
  return request({
    url: `${baseUrl}/contract/balance/subtChange/${memberId}/${num}/${currency}`,
    method: 'post',
  });
}

export function bb2ZcAjax({ memberId, currency, num }) {
  return request({
    url: `${baseUrl}/contract/zcBalance/addChange/${memberId}/${num}/${currency}`,
    method: 'post',
  });
}
export function zc2BbAjax({ memberId, currency, num }) {
  return request({
    url: `${baseUrl}/contract/zcBalance/subtChange/${memberId}/${num}/${currency}`,
    method: 'post',
  });
}

export function fb2QcAjax({ memberId, currency, num }) {
  return request({
    url: `${baseUrl}/contract/balance/otcAddChange/${memberId}/${num}/${currency}`,
    method: 'post',
  });
}


export function qc2FbAjax({ memberId, currency, num }) {
  return request({
    url: `${baseUrl}/contract/balance/otcSubtChange/${memberId}/${num}/${currency}`,
    method: 'post',
  });
}

export function fb2ZcAjax({ memberId, currency, num }) {
  return request({
    url: `${baseUrl}/contract/zcBalance/otcAddChange/${memberId}/${num}/${currency}`,
    method: 'post',
  });
}

export function zc2FbAjax({ memberId, currency, num }) {
  return request({
    url: `${baseUrl}/contract/zcBalance/otcSubtChange/${memberId}/${num}/${currency}`,
    method: 'post',
  });
}

export function qc2ZcAjax({ memberId, currency, num }) {
  return request({
    url: `${baseUrl}/contract/balance/handleTrans/${memberId}/${num}/${currency}`,
    method: 'post',
  });
}

export function zc2QcAjax({ memberId, currency, num }) {
  return request({
    url: `${baseUrl}/contract/zcBalance/handleTrans/${memberId}/${num}/${currency}`,
    method: 'post',
  });
}

