import request from '/@/utils/request';

export function useGenerateApi() {
    return {

        getPageList(data: object) {
            return request({
                url: '/manage/generate/getPageList',
                method: 'post',
                data: data
            })
        },

        getDbList(data: object) {
            return request({
                url: '/manage/generate/getDbList',
                method: 'post',
                data: data
            })
        },

        importTable(data: object) {
            return request({
                url: '/manage/generate/importTable',
                method: 'post',
                data: data
            })
        },

        preview(id: number) {
            return request({
                url: '/manage/generate/preview/' + id,
                method: 'get'
            })
        },

        getInfo(id: number) {
            return request({
                url: '/manage/generate/' + id,
                method: 'get'
            })
        },
        updateGenTable(data: object) {
            return request({
                url: '/manage/generate/updateGenTable',
                method: 'post',
                data: data
            }) 
        },
        deleteById(id: number) {
            return request({
                url: '/manage/generate/' + id,
                method: 'delete'
            })
        },
        download(tableName: String) {
            return request({
                url: '/manage/generate/download/' + tableName,
                method: 'get',
                responseType: "blob"
            })
        },
        batchDownload(data: object) {
            return request({
                url: '/manage/generate/batchDownload',
                method: 'post',
                data: data,
                responseType: "blob"
            })
        }
    }
}
