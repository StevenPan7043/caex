import request from '@/libs/axios'
import { baseUrl } from '../../public/config'


export function getCurrency(id) {
    return request({
        url: baseUrl + '/activity/' + id,
        method: 'get'
    })
}


export function getCurrencylist(name, size) {
    return request({
        url: baseUrl + '/tr/list/currency/' + name + '/' + size,
        method: 'get'
    })
}

export function getList(name, size) {
    return request({
        url: baseUrl + '/tr/list/' + name + '/' + size,
        method: 'get'
    })
}

export function getActivityist(type, offset, size) {
    return request({
        url: baseUrl + '/activity/list/activity/' + type + '/limit/' + offset + '/' + size,
        method: 'get'
    })
}
export function getlistday(name, size) {
    return request({
        url: baseUrl + '/tr/new/list/day/' + name + '/' + size,
        method: 'get'
    })
}