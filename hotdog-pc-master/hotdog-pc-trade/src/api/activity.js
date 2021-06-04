import request from '@/libs/axios'
import { baseUrl } from '../../public/config'

export function getactivitylist(status,page,pagesize) {
    return request({
        url: baseUrl + '/activity/list/'+status+'/' + page + '/' + pagesize,
        method: 'get'
    })
}