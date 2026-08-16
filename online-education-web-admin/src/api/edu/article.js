import request from '@/utils/request'

export default {

    // 1.讲师列表
    // current当前页、limit每页记录数、articleQuery条件对象
    getArticleListPage(current, limit, articleQuery) {
        return request({
            url: `/eduservice/article/pageArticleCondition/${current}/${limit}`,
            method: 'post',
            // articleQuery条件对象，后端使用RequestBody获取数据
            // data表示把对象转换json进行传递到接口里面
            data: articleQuery
          })
    },
    // 2.根据id删除讲师
    deleteArticleById(id) {
        return request({
            url: `/eduservice/article/deleteArticle/${id}`,
            method: 'delete'
          })
    },
    // 3.添加文章
    addArticle(article) {
        return request({
            url: `/eduservice/article/addArticle`,
            method: 'post',
            data: article
        })
    },
    // 4.获取文章信息
    getArticleInfo(id) {
        return request({
            url: `/eduservice/article/getArticle/${id}`,
            method: 'get'
        })
    },
    // 5.修改文章信息
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
}
