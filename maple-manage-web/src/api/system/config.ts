import request from '/@/utils/request';

export function useConfigApi() {
  return {

    getPageList(data: object) {
      return request({
        url: '/system/config/getPageList',
        method: 'post',
        data: data
      })
    },

    getConfigById(id: number) {
      return request({
        url: '/system/config/' + id,
        method: 'get'
      })
    },

    createConfig(data: object) {
      return request({
        url: '/system/config/createConfig',
        method: 'post',
        data: data
      })
    },

    updateConfig(data: object) {
      return request({
        url: '/system/config/updateConfig',
        method: 'post',
        data: data
      })
    },

    deleteConfig(id: number) {
      return request({
        url: '/system/config/' + id,
        method: 'delete'
      })
    },
  }
}