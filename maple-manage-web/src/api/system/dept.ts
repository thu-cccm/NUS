import request from '/@/utils/request';

export function useDeptApi() {
    return {

        getTreeList(data: object) {
            return request({
                url: '/system/dept/getTreeList',
                method: 'post',
                data: data
            })
        },

        getDeptById(id: number) {
            return request({
                url: '/system/dept/' + id,
                method: 'get'
            })
        },

        createDept(data: object) {
            return request({
                url: '/system/dept/createDept',
                method: 'post',
                data: data
            })
        },

        updateDept(data: object) {
            return request({
                url: '/system/dept/updateDept',
                method: 'post',
                data: data
            })
        },

        deleteDept(id: number) {
            return request({
                url: '/system/dept/' + id,
                method: 'delete'
            })
        },
    }
}