export function useDictApi() {
    return {

        getPageList(data: object) {
            return request({
                url: '/dict/getPageList',
                method: 'post',
                data: data
            })
        },

        getDictById(id: number) {
            return request({
                url: '/dict/' + id,
                method: 'get'
            })
        },

        createDict(data: object) {
            return request({
                url: '/dict/createDictType',
                method: 'post',
                data: data
            })
        },

        updateDict(data: object) {
            return request({
                url: '/dict/updateDictType',
                method: 'post',
                data: data
            })
        },

        deleteDict(id: number) {
            return request({
                url: '/dict/' + id,
                method: 'delete'
            })
        },

        getDictByCode(dictCode: string){
            return request({
                url: '/dict/getDictByCode/' + dictCode,
                method: 'get'
            })
        },

        getDictTypeList(){
            return request({
                url: '/dict/getDictTypeList',
                method: 'post'
            })
        }
    }
}

import request from '/@/utils/request';
