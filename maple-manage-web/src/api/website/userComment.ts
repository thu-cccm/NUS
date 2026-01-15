import request from '/@/utils/request';

export function useWebUserCommentApi() {
  return {

    getPageList(data: object) {
      return request({
        url: '/manageUserComment/getPageList',
        method: 'post',
        data: data
      })
    },

    getWebUserCommentById(id: number) {
      return request({
        url: '/manageUserComment/' + id,
        method: 'get'
      })
    },

    createWebUserComment(data: object) {
      return request({
        url: '/manageUserComment/createWebUserComment',
        method: 'post',
        data: data
      })
    },

    updateWebUserComment(data: object) {
      return request({
        url: '/manageUserComment/updateWebUserComment',
        method: 'post',
        data: data
      })
    },

    deleteWebUserComment(id: number) {
      return request({
        url: '/manageUserComment/' + id,
        method: 'delete'
      })
    },
  }
}