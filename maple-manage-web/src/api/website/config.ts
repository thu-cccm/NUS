import request from '/@/utils/request';

export function useWebConfigApi() {
  return {

    getPageList(data: object) {
      return request({
        url: '/manageConfig/getPageList',
        method: 'post',
        data: data
      })
    },

    getWebConfigById(id: number) {
      return request({
        url: '/manageConfig/' + id,
        method: 'get'
      })
    },

    createWebConfig(data: object) {
      return request({
        url: '/manageConfig/createWebConfig',
        method: 'post',
        data: data
      })
    },

    updateWebConfig(data: object) {
      return request({
        url: '/manageConfig/updateWebConfig',
        method: 'post',
        data: data
      })
    },

    deleteWebConfig(id: number) {
      return request({
        url: '/manageConfig/' + id,
        method: 'delete'
      })
    },
  }
}