import request from '@/utils/request'

export function updateUser(data) {
    return request({
        url: '/webUser/updateUser',
        method: 'post',
        data: data
    })
}