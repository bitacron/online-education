import request from '@/utils/request'

export default {
    // 1.查询所有讲师
    // current当前页、limit每页记录数、teacherQuery条件对象
    getListBanner(current, limit, bannerQuery) {
        return request({
            url: `/educms/banner/pageBanner/${current}/${limit}`,
            method: 'get',
            data: bannerQuery
        })
    },
    // 2.根据课程id查询课程信息
    getBannerInfoById(id) {
        return request({
            url: `/educms/banner/get/${id}`,
            method: 'get'
        })
    },
    // 3.添加课程信息
    addBannerInfo(bannerInfo) {
        return request({
            url: `/educms/banner/save`,
            method: 'post',
            data:bannerInfo
            })
    },
    // 4.修改课程信息
    updateBannerInfo(bannerInfo) {
        return request({
            url: `/educms/banner/update`,
            method: 'put',
            data: bannerInfo
        })
    },
    // 5.删除banner
    deleteBannerById(id) {
        return request({
            url: `/educms/banner/remove/${id}`,
            method: 'delete'
        })
    }
}


