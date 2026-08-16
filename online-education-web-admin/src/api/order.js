import request from '@/utils/request'

export default {

    // 1.讲师列表
    // current当前页、limit每页记录数、orderQuery条件对象
    getOrderListPage(current, limit, orderQuery) {
        return request({
            url: `/eduorder/order/pageOrder/${current}/${limit}`,
            method: 'post',
            // teacherQuery条件对象，后端使用RequestBody获取数据
            // data表示把对象转换json进行传递到接口里面
            data: orderQuery
          })
    },
    // 2.查询所有课程
    getListCourse() {
        return request({
            url: `/eduservice/course/findAll`,
            method: 'get'
            })
    },
    // 3.根据id删除订单
    deleteOrderById(id) {
        return request({
            url: `/eduorder/order/${id}`,
            method: 'delete'
          })
    },

}


