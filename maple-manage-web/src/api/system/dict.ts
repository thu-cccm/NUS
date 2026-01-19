export function useDictApi() {
    return {

        getPageList(data: object) {
            return request({
                url: '/manage/dict/getPageList',
                method: 'post',
                data: data
            })
        },

        getDictById(id: number) {
            return request({
                url: '/manage/dict/' + id,
                method: 'get'
            })
        },

        createDict(data: object) {
            return request({
                url: '/manage/dict/createDictType',
                method: 'post',
                data: data
            })
        },

        updateDict(data: object) {
            return request({
                url: '/manage/dict/updateDictType',
                method: 'post',
                data: data
            })
        },

        deleteDict(id: number) {
            return request({
                url: '/manage/dict/' + id,
                method: 'delete'
            })
        },

        getDictByCode(dictCode: string){
            return request({
                url: '/manage/dict/getDictByCode/' + dictCode,
                method: 'get'
            })
        },

        getDictTypeList(){
            return request({
                url: '/manage/dict/getDictTypeList',
                method: 'post'
            })
        }
    }
}

import request from '/@/utils/request';
