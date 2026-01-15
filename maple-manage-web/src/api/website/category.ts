import request from '/@/utils/request';

export function useWebCategoryApi() {
    return {

        getTreeList(data: object) {
            return request({
                url: '/manageCategory/getTreeList',
                method: 'post',
                data: data
            })
        },

        getCategoryList(data: object) {
            return request({
                url: '/manageCategory/getCategoryList',
                method: 'post',
                data: data
            })
        },

        getWebCategoryById(id: number) {
            return request({
                url: '/manageCategory/' + id,
                method: 'get'
            })
        },

        createWebCategory(data: object) {
            return request({
                url: '/manageCategory/createWebCategory',
                method: 'post',
                data: data
            })
        },

        updateWebCategory(data: object) {
            return request({
                url: '/manageCategory/updateWebCategory',
                method: 'post',
                data: data
            })
        },

        deleteWebCategory(id: number) {
            return request({
                url: '/manageCategory/' + id,
                method: 'delete'
            })
        },
    }
}