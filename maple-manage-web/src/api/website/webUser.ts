import request from '/@/utils/request';

export function useWebUserApi() {
  return {

    getPageList(data: object) {
      return request({
        url: '/manageWebUser/getPageList',
        method: 'post',
        data: data
      })
    },

    getWebUserById(id: number) {
      return request({
        url: '/manageWebUser/' + id,
        method: 'get'
      })
    },

    createWebUser(data: object) {
      return request({
        url: '/manageWebUser/createWebUser',
        method: 'post',
        data: data
      })
    },

    updateWebUser(data: object) {
      return request({
        url: '/manageWebUser/updateWebUser',
        method: 'post',
        data: data
      })
    },

    deleteWebUser(id: number) {
      return request({
        url: '/manageWebUser/' + id,
        method: 'delete'
      })
    },
  }
}