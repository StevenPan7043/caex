import request from '@/libs/axios4'
import { chatUrl } from '../../public/config'
//管理员禁言
export function banMember(params) {
    return request({
        url: chatUrl + 'banMember',
        method: 'post',
        params
    })
}
//添加管理员
export function updateManager(params) {
    return request({
        url: chatUrl+'updateManager',
        method: 'post',
        params
    })
}
//修改会员等级
export function updateInfoById(params) {
    return request({
        url: chatUrl+'updateInfoById',
        method: 'post',
        params
    })
}