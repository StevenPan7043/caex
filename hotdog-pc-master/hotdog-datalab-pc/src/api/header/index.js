import axios from '../MyAxios';
export function getFeeCurrencyPairList() {
    return axios({
        method: 'get',
        url: '/dataLab/getFeeCurrencyPairList',
    })
}