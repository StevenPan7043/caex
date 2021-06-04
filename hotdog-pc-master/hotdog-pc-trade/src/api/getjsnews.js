import request from '@/libs/axios'
import { baseUrl } from '../../public/config.js'

export function getjinsenew (page,pageSize) {
  return request({
    url:  baseUrl+'/n/list/'+page+'/'+pageSize,
    method: 'get',
  })
}