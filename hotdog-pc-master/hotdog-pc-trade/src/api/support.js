import request from '@/libs/axios'
import { baseUrl } from '../../public/config'


export function getSupport(page, pageSize) {
    return request({
        url: baseUrl + '/n/news/recently/' + page + '/' + pageSize,
        method: 'get'
    })
}