import axios from '../MyAxios';
export function getFeeAmount(data) {
    return axios({
        method: 'get',
        url: '/dataLab/getFeeAmount',
        params:data
    })
}
export function getFeePairDetails (data) {
    return axios({
        method: 'get',
        url: '/dataLab/getFeePairDetails',
        params:data
    })
}