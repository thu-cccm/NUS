import request from '@/utils/request'

export function getHomeData() {
    return request({
        url: '/getHomeData',
        method: 'get'
    })
}

export function search(data) {
    return request({
        url: '/search',
        method: 'post',
        data: data
    })
}

export function getPageCategory(data) {
    return request({
        url: '/webCategory/getPageCategory',
        method: 'post',
        data: data
    })
}

export function getBlogCategory(data) {
  return request({
    url: '/webCategory/getPageCategory',
    method: 'post',
    data: data
  })
}

export function getCategoryById(categoryId) {
    return request({
        url: '/webCategory/' + categoryId,
        method: 'get'
    })
}

export function getPageArticle(data) {
    return request({
      url: '/webArticle/getPageArticle',
      method: 'post',
      data: data
    })
}

export function getArticleById(articleId) {
    return request({
        url: '/webArticle/' + articleId,
        method: 'get'
    })
}

export function collectArticle(data) {
    return request({
        url: '/operation/collectTitle',
        method: 'post',
        data: data
    })
}

export function likeArticle(data) {
    return request({
        url: '/operation/likeTitle',
        method: 'post',
        data: data
    })
}

export function downResource(data) {
    return request({
        url: '/operation/downResource',
        method: 'post',
        data: data,
        responseType: "blob"
    })
}
