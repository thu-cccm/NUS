import request from '@/utils/request'

export function getUnlimitedQrCode() {
  return request({
    url: '/sso/getUnlimitedQrCode',
    method: 'post'
  })
}

export function checkAppletAuth(data) {
  return request({
    url: '/sso/checkAppletAuth',
    method: 'post',
    data: data
  })
}

export function getUserInfo() {
  return request({
    url: '/webUser/getUserInfo',
    method: 'post'
  })
}

export function logout() {
  return request({
    url: '/sso/logout',
    method: 'get'
  })
}
