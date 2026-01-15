import request from '@/utils/request'

export function getPageLink(data) {
    return request({
        url: '/friendlyLink/getPageLink',
        method: 'post',
        data: data
    })
}

export function addLinkNum(linksId) {
    return request({
        url: '/friendlyLink/addLinkNum/' + linksId,
        method: 'get'
    })
}
