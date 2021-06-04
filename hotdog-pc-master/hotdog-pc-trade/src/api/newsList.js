import request from '@/libs/axios'
import { baseUrl } from '../../public/config'

export function getNewsList(id, page, pagesize) {
    return request({
        url: baseUrl + '/n/news/' + id + '/' + page + '/' + pagesize,
        method: 'get'
    })
}

export function getNewtopsList(id, page, pagesize) {
    return request({
        url: baseUrl + '/n/news/' + id + '/' + page + '/' + pagesize,
        method: 'get'
    })
}