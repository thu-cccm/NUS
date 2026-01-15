import request from '/@/utils/request';

export function useGenerateApi() {
    return {

        getPageList(data: object) {
            return request({
                url: '/generate/getPageList',
                method: 'post',
                data: data
            })
        },

        getDbList(data: object) {
            return request({
                url: '/generate/getDbList',
                method: 'post',
                data: data
            })
        },

        importTable(data: object) {
            return request({
                url: '/generate/importTable',
                method: 'post',
                data: data
            })
        },

        preview(id: number) {
            return request({
                url: '/generate/preview/' + id,
                method: 'get'
            })
        },

        getInfo(id: number) {
            return request({
                url: '/generate/' + id,
                method: 'get'
            })
        },
        updateGenTable(data: object) {
            return request({
                url: '/generate/updateGenTable',
                method: 'post',
                data: data
            }) 
        },
        deleteById(id: number) {
            return request({
                url: '/generate/' + id,
                method: 'delete'
            })
        },
        download(tableName: String) {
            return request({
                url: '/generate/download/' + tableName,
                method: 'get',
                responseType: "blob"
            })
        },
        batchDownload(data: object) {
            return request({
                url: '/generate/batchDownload',
                method: 'post',
                data: data,
                responseType: "blob"
            })
        }
    }
}
