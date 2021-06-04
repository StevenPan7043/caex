import request from '@/libs/axios'
import { baseUrl } from '../../../../public/config'

export function getMember () {
  return request({
    url: baseUrl + '/m/member',
    method: 'get'
  })
}

export function getReutrncommi () {
    return request({
        url: baseUrl + '/m/reutrncommi/intro/total',
        method: 'get'
    })
}

export function getPeopleByPage({page,pageSize}){
    return request({
        url: baseUrl + `/m/reutrncommi/intro/${page}/${pageSize}`,
        method: 'get'
    })
}
  
export function getMoneyByPage({page,pageSize}){
    return request({
        url: baseUrl + `/m/reutrncommi/${page}/${pageSize}`,
        method: 'get'
    })
}
