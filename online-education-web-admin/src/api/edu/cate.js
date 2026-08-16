import request from '@/utils/request'

export default {

    // 1.查询所有分类（由于功能特殊性，所以不设置条件也不分页）
    getListCategories() {
        return request({
            url: `/eduservice/categories/getCategoriesInfoList`,
            method: 'get'
        })
    },

    // 2.添加分类
    addCategories(cate) {
        return request({
            url: `/eduservice/categories/addCategories`,
            method: 'post',
            data: cate
        })
    },
    // 3.获取分类信息
    getCategoriesInfo(id) {
        return request({
            url: `/eduservice/categories/getCategories/${id}`,
            method: 'get'
        })
    },
    // 4.修改分类信息
    updateCategoriesInfo(cate) {
        return request({
            url: `/eduservice/categories/updateCategories`,
            method: 'post',
            data: cate
        })
    },
    // 5.根据id删除分类
    deleteCategoriesById(id) {
        return request({
            url: `/eduservice/categories/deleteCategories/${id}`,
            method: 'delete'
        })
    },

}
