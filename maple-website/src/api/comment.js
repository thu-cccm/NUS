import request from '@/utils/request'

export function getPageComment(data) {
  return request({
    url: '/webUserComment/getPageList',
    method: 'post',
    data: data
  })
}

export function createComment(data) {
  return request({
    url: '/webUserComment/createWebUserComment',
    method: 'post',
    data: data
  })
}

export function deleteComment(commentId) {
  return request({
    url: '/webUserComment/' + commentId,
    method: 'delete'
  })
}

export function likeComment(data) {
  return request({
    url: '/webUserComment/likeComment',
    method: 'post',
    data: data
  })
}