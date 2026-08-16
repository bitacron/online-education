import request from '@/utils/request'

export default {

    // 1.添加课程信息
    addCourseInfo(courseInfo) {
        return request({
            url: `/eduservice/course/addCourseInfo`,
            method: 'post',
            data:courseInfo
          })
    },
    // 2.查询所有讲师
    getListTeacher() {
        return request({
            url: `/eduservice/teacher/findAll`,
            method: 'get'
          })
    },
    // 3.根据课程id查询课程信息
    getCourseInfoById(courseId) {
        return request({
            url: `/eduservice/course/getCourseInfo/${courseId}`,
            method: 'get'
        })
    },
    // 4.修改课程信息
    updateCourseInfo(courseInfo) {
        return request({
            url: `/eduservice/course/updateCourseInfo`,
            method: 'post',
            data: courseInfo
        })
    },
    // 课程确认信息显示
    getPublishCourseInfo(id) {
        return request({
            url: `/eduservice/course/getPublishCourseInfo/`+id,
            method: 'get'
        })
    },
    // 课程最终发布
    publishCourse(id) {
        return request({
            url: `/eduservice/course/publishCourse/${id}`,
            method: 'put'
        })
    },
    // TODO 课程列表    
    // current当前页、limit每页记录数、teacherQuery条件对象
    getListCourse(current, limit, courseQuery) {
        return request({
            url: `/eduservice/course/pageCourseCondition/${current}/${limit}`,
            method: 'post',
            data: courseQuery
        })
    },
    deleteCourseById(id) {
        return request({
            url: `/eduservice/course/${id}`,
            method: 'delete'
        })
    }
}


