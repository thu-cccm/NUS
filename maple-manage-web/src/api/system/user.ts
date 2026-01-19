import request from '/@/utils/request';

export function useUserApi() {
    return {
        getUserInfo: () => {
            return request({
                url: '/manage/sso/getInfo',
                method: 'get',
            });
        },

        getPageList(data: object) {
            return request({
                url: '/manage/user/getPageList',
                method: 'post',
                data: data
            })
        },

        getUserById(id: number) {
            return request({
                url: '/manage/user/' + id,
                method: 'get'
            })
        },

        createUser(data: object) {
            return request({
                url: '/manage/user/createUser',
                method: 'post',
                data: data
            })
        },
        updateUser(data: object) {
            return request({
                url: '/manage/user/updateUser',
                method: 'post',
                data: data
            })
        },

        deleteUser(id: number) {
            return request({
                url: '/manage/user/' + id,
                method: 'delete'
            })
        },

        updatePwd(password: string, oldPassword: string) {
            const data = {
                password,
                oldPassword
            }
            return request({
                url: '/manage/user/updatePwd',
                method: 'put',
                data: data
            })
        },
    }
}
