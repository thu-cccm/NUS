import request from '/@/utils/request';

export function useWebMenuApi() {
    return {

        getTreeList(data: object) {
            return request({
                url: '/manageWebMenu/getTreeList',
                method: 'post',
                data: data
            })
        },

        getWebMenuById(id: number) {
            return request({
                url: '/manageWebMenu/' + id,
                method: 'get'
            })
        },

        createWebMenu(data: object) {
            return request({
                url: '/manageWebMenu/createWebMenu',
                method: 'post',
                data: data
            })
        },

        updateWebMenu(data: object) {
            return request({
                url: '/manageWebMenu/updateWebMenu',
                method: 'post',
                data: data
            })
        },

        deleteWebMenu(id: number) {
            return request({
                url: '/manageWebMenu/' + id,
                method: 'delete'
            })
        },
    }
}