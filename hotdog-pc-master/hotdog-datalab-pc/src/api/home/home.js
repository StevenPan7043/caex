import axios from '../MyAxios';
export function getDataLabFirst(data) {
    return axios({
        method: 'get',
        url: '/dataLab/getDataLabFirst',
        params:data
    })
}
export function getPositionAcountList(data) {
    return axios({
        method: 'get',
        url: '/dataLab/getPositionAcountList/'+ new Date().getTime(),
        params:data
    })
}