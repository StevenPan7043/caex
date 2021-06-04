import axios from '../MyAxios';
export function feeCurrencyWithdrawal(data) {
    return axios({
        method: 'post',
        url: '/dataLab/feeCurrencyWithdrawal',
        data:data
    })
}
export function getMembers () {
    return axios({
        method: 'get',
        url: '/m/member',
    })
  }
  export function getMail (token, code, useraccount, type) {
    return axios({
        method: 'get',
        url: '/m/mail/' + token + '/' + code + '/' + useraccount + '/' + type,
    })
  }
export function assetsRechargeRec(page,data) {
    return axios({
        method: 'get',
        url: `/a/assetsRecharge/${page}/` + new Date().getTime(),
        params:data
    })
}
