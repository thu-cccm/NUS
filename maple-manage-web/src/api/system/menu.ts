import request from '/@/utils/request';

export function useMenuApi() {
    return {

        getRouters() {
            return request({
                url: '/manage/system/menu/getRouters',
                method: 'get'
            })
        },

        getTreeList(data: object) {
            return request({
                url: '/manage/system/menu/getTreeList',
                method: 'post',
                data: data
            })
        },

        getMenuById(id: number) {
            return request({
                url: '/manage/system/menu/' + id,
                method: 'get'
            })
        },

        createMenu(data: object) {
            return request({
                url: '/manage/system/menu/createMenu',
                method: 'post',
                data: data
            })
        },

        updateMenu(data: object) {
            return request({
                url: '/manage/system/menu/updateMenu',
                method: 'post',
                data: data
            })
        },

        deleteMenu(id: number) {
            return request({
                url: '/manage/system/menu/' + id,
                method: 'delete'
            })
        },
    }
}