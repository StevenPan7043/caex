import request from '@/libs/axios'
import { baseUrl } from '../../public/config'

export function getNews (id) {
  return request({
    url: baseUrl + '/n/news/' + id,
    method: 'get'
  })
}
