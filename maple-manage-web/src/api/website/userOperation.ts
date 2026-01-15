import request from '/@/utils/request';

export function useWebUserOperationApi() {
  return {

    getPageList(data: object) {
      return request({
        url: '/manageUserOperation/getPageList',
        method: 'post',
        data: data
      })
    },

    getWebUserOperationById(id: number) {
      return request({
        url: '/manageUserOperation/' + id,
        method: 'get'
      })
    },

    createWebUserOperation(data: object) {
      return request({
        url: '/manageUserOperation/createWebUserOperation',
        method: 'post',
        data: data
      })
    },

    updateWebUserOperation(data: object) {
      return request({
        url: '/manageUserOperation/updateWebUserOperation',
        method: 'post',
        data: data
      })
    },

    deleteWebUserOperation(id: number) {
      return request({
        url: '/manageUserOperation/' + id,
        method: 'delete'
      })
    },
  }
}