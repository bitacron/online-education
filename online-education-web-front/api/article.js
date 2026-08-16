import request from '@/utils/request'
export default {
  // 文章列表
  getArticleList(page, limit, searchObj) {
    return request({
      url: `/eduservice/articlefront/getFrontArticleList/${page}/${limit}`,
      method: 'post',
      data: searchObj
    })
  },
  // 获取文章分类
  getAllCate() {
      return request({
          url: `/eduservice/categories/findAll`,
          method: 'get'
      })
  },
  // 文章详情的方法
  getArticleInfo(articleId) {
      return request({
          url: `/eduservice/articlefront/getFrontArticleInfo/${articleId}`,
          method: 'get'
      })
  },
  // 写文章
  addArticle(article) {
    return request({
        url: `/eduservice/article/addArticle`,
        method: 'post',
        data: article
    })
  },
  // 修改文章
  updateArticleInfo(article) {
    return request({
        url: `/eduservice/article/updateArticle`,
        method: 'post',
        data: article
     })
 },
  // 6.查询所有文章类别
  getListCategories() {
      return request({
          url: `/eduservice/categories/findAll`,
          method: 'get'
      })
  },
  // 7.搜索文章
  searchArticle(current, limit, keyWord) {
    return request({
      url: `/edusearch/es/searchPageArticleCondition/${current}/${limit}`,
      method: 'post',
      params: {keyWord}
    })
  }
  // 根据文章id获取文章名
  
}