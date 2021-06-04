import request from '@/libs/axios'
import { baseUrl } from '../../public/config'

export function getSearch(page, pageSize, key) {
    return request({
        url: baseUrl + '/n/news/key/' + page + '/' + pageSize + '/' + key,
        method: 'get'
    })
}