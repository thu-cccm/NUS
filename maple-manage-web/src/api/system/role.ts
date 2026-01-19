import request from '/@/utils/request';

export function useRoleApi() {
    return {

        getPageList(data: object) {
            return request({
                url: '/manage/system/role/getPageList',
                method: 'post',
                data: data
            })
        },
        getRoleList() {
            return request({
                url: '/manage/system/role/getRoleList',
                method: 'post',
            })
        },

        getRoleById(id: number) {
            return request({
                url: '/manage/system/role/' + id,
                method: 'get'
            })
        },

        createRole(data: object) {
            return request({
                url: '/manage/system/role/createRole',
                method: 'post',
                data: data
            })
        },

        updateRole(data: object) {
            return request({
                url: '/manage/system/role/updateRole',
                method: 'post',
                data: data
            })
        },

        deleteRole(id: number) {
            return request({
                url: '/manage/system/role/' + id,
                method: 'delete'
            })
        },
    }
}