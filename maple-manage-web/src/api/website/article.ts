import request from '/@/utils/request';

export function useWebArticleApi() {
  return {

    getPageList(data: object) {
      return request({
        url: '/manageArticle/getPageList',
        method: 'post',
        data: data
      })
    },

    getWebArticleById(id: number) {
      return request({
        url: '/manageArticle/' + id,
        method: 'get'
      })
    },

    createWebArticle(data: object) {
      return request({
        url: '/manageArticle/createWebArticle',
        method: 'post',
        data: data
      })
    },

    updateWebArticle(data: object) {
      return request({
        url: '/manageArticle/updateWebArticle',
        method: 'post',
        data: data
      })
    },

    deleteWebArticle(id: number) {
      return request({
        url: '/manageArticle/' + id,
        method: 'delete'
      })
    },
  }
}