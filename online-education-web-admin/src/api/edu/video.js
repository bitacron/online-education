import request from '@/utils/request'

export default {

    // 1.添加章节
    addVideo(video) {
        return request({
            url: `/eduservice/video/addVideo`,
            method: 'post',
            data: video
        })
    },
    // 2.删除章节
    deleteVideo(videoId) {
        return request({
            url: `/eduservice/video/`+videoId,
            method: 'delete'
        })
    },
    // 3.根据id查章节
    getVideo(videoId) {
        return request({
            url: `/eduservice/video/getVideoInfo/`+videoId,
            method: 'get'
        })
    },
    // 4.修改章节
    updateVideo(video) {
        return request({
            url: `/eduservice/video/updateVideo`,
            method: 'post',
            data: video
        })
    },
    // 5.删除阿里云视频
    deleteAlyVod(videoId) {
        return request({
            url: `/eduvod/video/removeAlyVideo/`+videoId,
            method: 'delete'
        })
    },



}


