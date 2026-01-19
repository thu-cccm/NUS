import request from '/@/utils/request';

export function useConfigApi() {
  return {

    getPageList(data: object) {
      return request({
        url: '/manage/system/config/getPageList',
        method: 'post',
        data: data
      })
    },

    getConfigById(id: number) {
      return request({
        url: '/manage/system/config/' + id,
        method: 'get'
      })
    },

    createConfig(data: object) {
      return request({
        url: '/manage/system/config/createConfig',
        method: 'post',
        data: data
      })
    },

    updateConfig(data: object) {
      return request({
        url: '/manage/system/config/updateConfig',
        method: 'post',
        data: data
      })
    },

    deleteConfig(id: number) {
      return request({
        url: '/manage/system/config/' + id,
        method: 'delete'
      })
    },
  }
}