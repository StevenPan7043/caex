import request from '@/libs/axios'

export function getChartData (url) {
  return request({
    url: url + 'm/allticker/' + Date.parse(new Date()),
    method: 'get'
  })
}

export function send (url) {
  return request({
    url: url ,
    method: 'get'
  })
}