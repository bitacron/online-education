import request from '@/utils/request'

export default {

    // 1.讲师列表
    // current当前页、limit每页记录数、teacherQuery条件对象
    getTeacherListPage(current, limit, teacherQuery) {
        return request({
            // url: '/eduservice/teacher/pageTeacherCondition/'+current+"/"+limit,
            url: `/eduservice/teacher/pageTeacherCondition/${current}/${limit}`,
            method: 'post',
            // teacherQuery条件对象，后端使用RequestBody获取数据
            // data表示把对象转换json进行传递到接口里面
            data: teacherQuery
          })
    },
    // 2.根据id删除讲师
    // current当前页、limit每页记录数、teacherQuery条件对象
    deleteTeacherById(id) {
        return request({
            url: `/eduservice/teacher/${id}`,
            method: 'delete'
          })
    },
    // 3.添加讲师
    addTeacher(teacher) {
        return request({
            url: `/eduservice/teacher/addTeacher`,
            method: 'post',
            data: teacher
        })
    },
    // 4.获取讲师信息
    getTeacherInfo(id) {
        return request({
            url: `/eduservice/teacher/getTeacher/${id}`,
            method: 'get'
        })
    },
    // 5.修改讲师信息
    updateTeacherInfo(teacher) {
        return request({
            url: `/eduservice/teacher/updateTeacher`,
            method: 'post',
            data: teacher
        })
    },

}


