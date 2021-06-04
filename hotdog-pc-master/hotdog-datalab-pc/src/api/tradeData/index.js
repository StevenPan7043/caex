import axios from '../MyAxios';
export function getTradeList(data) {
    return axios({
        method: 'get',
        url: '/dataLab/getTradeList',
        params:data
    })
}