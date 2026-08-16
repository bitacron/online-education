import request from '@/utils/request'

export default {

  getPageList(page, limit, courseId) {
    return request({
      url: `/eduservice/comment/${page}/${limit}`,
      method: 'get',
      params: {courseId}
    })
  },
  addComment(comment) {
    return request({
      url: `/eduservice/comment/auth/save`,
      method: 'post',
      data: comment
    })
  },
  getArtilcePageList(page, limit, articleId) {
    return request({
      url: `/eduservice/commentArticle/${page}/${limit}`,
      method: 'get',
      params: {articleId}
    })
  },
  addArtilceComment(articleComment) {
    return request({
      url: `/eduservice/commentArticle/auth/save`,
      method: 'post',
      data: articleComment
    })
  }
}