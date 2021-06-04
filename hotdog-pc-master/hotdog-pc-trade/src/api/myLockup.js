import request from '@/libs/axios'
import { baseUrl } from '../../public/config'

export function getMember () {
  return request({
    url: baseUrl + '/m/member',
    method: 'get'
  })
}
export function lockTransfer (data) {
  return request({
    url: baseUrl + '/lock',
    method: 'post',
    data: data
  })
}
//锁仓记录
export function getrecord (page,pagesize) {
  return request({
    url: baseUrl + '/lock/record/'+page+'/'+pagesize,
    method: 'get'
  })
}
//锁仓排名
export function getrank (page) {
  return request({
    url: baseUrl + '/lock/rank/'+page,
    method: 'get'
  })
}
//锁仓冻结
export function getamount (symobl) {
  return request({
    url: baseUrl + '/lock/amount/'+symobl,
    method: 'get'
  })
}