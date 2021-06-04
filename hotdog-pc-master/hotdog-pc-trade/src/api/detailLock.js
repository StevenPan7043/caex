import request from '@/libs/axios'
import { baseUrl } from '../../public/config'

export function getLockRelease (id) {
  return request({
    url: baseUrl + '/locked/currency/id/' + id,
    method: 'get'
  })
}
