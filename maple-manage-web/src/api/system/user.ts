import request from '/@/utils/request';

export function useUserApi() {
    return {
        getUserInfo: () => {
            return request({
                url: '/sso/getInfo',
                method: 'get',
            });
        },

        getPageList(data: object) {
            return request({
                url: '/user/getPageList',
                method: 'post',
                data: data
            })
        },

        getUserById(id: number) {
            return request({
                url: '/user/' + id,
                method: 'get'
            })
        },

        createUser(data: object) {
            return request({
                url: '/user/createUser',
                method: 'post',
                data: data
            })
        },
        updateUser(data: object) {
            return request({
                url: '/user/updateUser',
                method: 'post',
                data: data
            })
        },

        deleteUser(id: number) {
            return request({
                url: '/user/' + id,
                method: 'delete'
            })
        },

        updatePwd(password: string, oldPassword: string) {
            const data = {
                password,
                oldPassword
            }
            return request({
                url: '/user/updatePwd',
                method: 'put',
                data: data
            })
        },
    }
}
