import request from '@/utils/request'

export function getHeaderMenu() {
    return request({
        url: '/common/getMenuTreeList',
        method: 'post',
        data: {
            menuType: "H",
            status: 1,
            parentId: 1
        }
    })
}

export function getFooterMenu() {
    return request({
        url: '/common/getMenuTreeList',
        method: 'post',
        data: {
            menuType: "F",
            status: 1,
            parentId: 2
        }
    })
}

export function getWebMenuByPath(path) {
    return request({
        url: '/common/getWebMenuByPath/' + path,
        method: 'get',
    })
}

export function getAllConfigList() {
    return request({
        url: '/common/getAllConfigList',
        method: 'post'
    })
}

export function getDictByCode(dictCode){
    return request({
        url: '/common/getDictByCode/' + dictCode,
        method: 'get'
    })
}
