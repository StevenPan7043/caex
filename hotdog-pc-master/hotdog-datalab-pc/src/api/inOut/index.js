import axios from '../MyAxios';
export function assetsRecharge(page,data) {
    return axios({
        method: 'get',
        url: `/a/assetsRecharge/${page}/` + new Date().getTime(),
        params:data
    })
}
export function assetsWithDraw(page,data) {
    return axios({
        method: 'get',
        url: `/a/assetsWithDraw/${page}/` + new Date().getTime(),
        params:data
    })
}