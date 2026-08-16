import request from '@/utils/request'
// const api_name = '/eduservice/teacherfront'
export default {
  getPageList(page, limit) {   
    return request({
      url: `/eduservice/teacherfront/getTeacherFrontList/${page}/${limit}`,
      method: 'get'
    })
  },
  getById(teacherId) {
    return request({
        url: `/eduservice/teacherfront/getTeacherFront/${teacherId}`,
        method: 'get'
    })
  }
}