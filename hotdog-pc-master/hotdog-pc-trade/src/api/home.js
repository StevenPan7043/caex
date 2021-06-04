import request from '@/libs/axios'
import { baseUrl } from '../../public/config'

import newRequest from '@/libs/newsAxios'

export function getNewsBanner (id='') {
  return request({
    url: baseUrl + '/n/news/banner?column_id='+id,
    method: 'get'
  })
}
export function getRecently (page, pagesize) {
  return request({
    url: baseUrl + '/n/news/recently/' + page + '/' + pagesize,
    method: 'get'
  })
}

export function getNewsList (id, page, pagesize) {
  return request({
    url: baseUrl + '/n/news/' + id + '/' + page + '/' + pagesize,
    method: 'get'
  })
}

export function getAllTicker () {
  return request({
    url: baseUrl + '/m/allticker/' + Date.parse(new Date()),
    method: 'get'
  })
}

export function getFavorite () {
  return request({
    url: baseUrl + '/m/favorite/' + Date.parse(new Date()),
    method: 'get'
  })
}

export function favorite (pair) {
  let data = {
    'pair_dsp_name': pair
  }
  return request({
    url: baseUrl + '/m/favorite',
    method: 'post',
    data: data
  })
}

export function favoriteDel (pair) {
  let data = {
    'pair_dsp_name': pair
  }
  return request({
    url: baseUrl + '/m/favorite/del',
    method: 'post',
    data: data
  })
}
export function tokenLogin(data) {
  return request({
    url: `${baseUrl}/m/tokenLogin`,
    data,
    method: 'post',
  });
}

export function getNewNewsCoin(lang) {
  return newRequest({
    url: `https://hotdogvip.zendesk.com/api/v2/help_center/${lang}/categories/360004132653/articles.json?per_page=4&page=1`,
    method: 'get',
  });
}
// export function getNewNewsOther(lang) {
//   return newRequest({
//     url: `https://peppaex.zendesk.com/api/v2/help_center/${lang}/sections/360005345853/articles.json?per_page=4&page=1`,
//     method: 'get',
//   });
// }

