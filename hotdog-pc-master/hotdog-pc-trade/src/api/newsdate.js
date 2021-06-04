import request from '@/libs/axios'
import { baseUrl } from '../../public/config'


export function getNewssub(id, count) {
    return request({
        url: baseUrl + '/n/news/' + id + '/submenu/title/' + count,
        method: 'get'
    })
}