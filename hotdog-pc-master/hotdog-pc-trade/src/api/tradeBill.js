import request from '@/libs/axios'
import { baseUrl } from '../../public/config'

export function getSymbol () {
  return request({
    url: baseUrl + '/m/symbol',
    method: 'get'
  })
}
export function getOrdersI (symbol, page, status) {
  return request({
    url: baseUrl + '/o/ordersI/' + symbol + '/' + page + '/' + status + '/' + Date.parse(new Date()),
    method: 'get'
  })
}
export function orderCancel (id, no, symbol) {
  let data = {
    'id': id,
    'o_no': no,
    'symbol': symbol
  }
  return request({
    url: baseUrl + '/o/order/cancelI',
    method: 'post',
    data: data
  })
}

export function getTrades (symbol, page) {
  return request({
    url: baseUrl  + '/o/tradesI/' + symbol + '/' + page + '/' + Date.parse(new Date()),
    method: 'get'
  })
}
