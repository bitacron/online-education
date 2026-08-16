<template>
    <div class="app-container">

        <!--查询表单-->
        <el-form :inline="true" class="demo-form-inline">
            <el-form-item>
                <el-input v-model="orderQuery.nickname" placeholder="会员名"/>
            </el-form-item>
            <el-form-item>
                <el-input v-model="orderQuery.teacherName" placeholder="讲师名"/>
            </el-form-item>
            <el-form-item>
                <el-select v-model="orderQuery.status" clearable placeholder="订单状态">
                    <el-option :value="0" label="未支付"/>
                    <el-option :value="1" label="已支付"/>
                </el-select>
            </el-form-item>

            <el-form-item>
                <el-select v-model="orderQuery.payType" clearable placeholder="支付类型">
                    <el-option :value="1" label="微信"/>
                    <el-option :value="2" label="支付宝"/>
                </el-select>
            </el-form-item>
        <!-- 课程 -->
        <el-form-item>
            <el-select v-model="orderQuery.courseId" placeholder="请选择课程">
                <el-option
                    v-for="course in courseList"
                    :key="course.id"
                    :label="course.title"
                    :value="course.id"/>
            </el-select>
        </el-form-item>
            <el-form-item label="添加时间">
                <el-date-picker
                    v-model="orderQuery.begin"
                    type="datetime"
                    placeholder="选择开始时间"
                    value-format="yyyy-MM-dd HH:mm:ss"
                    default-time="00:00:00"
                />
            </el-form-item>

            <el-form-item>
                <el-date-picker
                    v-model="orderQuery.end"
                    type="datetime"
                    placeholder="选择截止时间"
                    value-format="yyyy-MM-dd HH:mm:ss"
                    default-time="00:00:00"
                    />
                </el-form-item>
            <el-button type="primary" icon="el-icon-search" @click="getList()">查询</el-button>
            <el-button type="default" @click="resetData()">清空</el-button>
        </el-form>
        <!-- 表格 -->
        <el-table
            v-loading="listLoading"
            :data="list"
            element-loading-text="数据加载中"
            border
            fit
            highlight-current-row>
            <el-table-column
                label="序号"
                width="70"
                align="center">
                <template slot-scope="scope">
                    {{ (page - 1) * limit + scope.$index + 1 }}
                </template>
            </el-table-column>
            <el-table-column label="课程信息" width="280" align="center">
                <template slot-scope="scope">
                    <div class="info">
                        <div class="pic">
                            <img :src="scope.row.courseCover" alt="scope.row.courseTitle" width="150">
                        </div>
                        <div class="title">
                            <p>{{ scope.row.courseTitle }} </p>
                        </div>
                    </div>
                </template>
            </el-table-column>
            <el-table-column prop="orderNo" label="订单号"/>
            <el-table-column label="支付状态" width="80">
                <template slot-scope="scope">
                    {{ scope.row.status===1?'已支付':'未支付' }}
                </template>
            </el-table-column> 
            <el-table-column label="支付类型" width="80">
                <template slot-scope="scope">
                    {{ scope.row.status===1?'微信':'支付宝' }}
                </template>
            </el-table-column>               
            <el-table-column prop="teacherName" label="讲师名称" width="80" />            
            <el-table-column prop="nickname" label="会员昵称" width="80" />
            <el-table-column prop="totalFee" label="订单金额" width="80"/>
            <el-table-column prop="mobile" label="手机号码" width="120"/>
            <el-table-column prop="gmtCreate" label="添加时间" width="160"/>
            <el-table-column label="操作" width="100" align="center">
                <template slot-scope="scope">
                    <el-button type="danger" size="mini" icon="el-icon-delete"
                        @click="removeDataById(scope.row.id)">删除</el-button>
                </template>
            </el-table-column>
        </el-table>
        <!-- 分页 -->
        <el-pagination
            :current-page="page"
            :page-size="limit"
            :total="total"
            style="padding: 30px 0; text-align: center;"
            layout="total, prev, pager, next, jumper"
            @current-change="getList"
        />
    </div>
</template>

<script>
import orderApi from '@/api/order'
export default {
    data() { // 定义变量和初始值
        return {
            listLoading: false, // 是否显示loading信息
            list: null, // 查询之后接口返回集合
            page: 1, // 当前页
            limit: 10, // 每页记录数
            total: 0, // 总记录数
            orderQuery: {}, // 条件封装对象            
            courseList: [] // 讲师列表
        }
    },
    created() { // 页面渲染之前执行，一般调用methods定义的方法
        // 调用
        this.getListCourse()
        this.getList()
    },
    methods:{ // 创建具体的方法，调用teacher.js定义的方法
        // 讲师列表的方法
        getList(page = 1) {
            this.page = page
            orderApi.getOrderListPage(this.page, this.limit, this.orderQuery)
                .then(response => { // 请求成功
                    //response接口返回的数据
                    // console.log(response)
                    this.list = response.data.rows
                    this.total = response.data.total
                    console.log(this.list)
                    console.log(this.total)
                })
                .catch(error => { // 请求失败
                    console.log(error)
                })
        },
        resetData() { // 清除
            this.orderQuery = {} // 表单输入框数据清空
            this.getList() // 刷新页面重新查询
        },
        // 根据id删除
        removeDataById(id) {
            // alert(id)
            this.$confirm('此操作将永久删除该订单记录, 是否继续?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {// 点击确定，执行then方法
                orderApi.deleteOrderById(id)
                    .then(response => { // 删除成功                    
                        // 提示信息
                        this.$message({
                            type: 'success',
                            message: '删除成功!'
                        })
                        // 回到列表页面
                        this.getList()
                    })
            })// 点击确定，执行catch方法
            .catch((response) => { 
                if (response === 'cancel') {
                    this.$message({
                        type: 'info',
                        message: '已取消删除'
                    })
                } else {
                    this.$message({
                    type: 'error',
                    message: '删除失败'
                    })
                }
            })
        },
        // 查询所有的课程
        getListCourse() {
            orderApi.getListCourse().then(response => {
                this.courseList=response.data.items
                console.log(this.courseList)
            })
        },
    }    
}
</script>
<style scoped>
    .myClassList .info {
        width: 450px;
        overflow: hidden;
    }
    .myClassList .info .pic {
        width: 150px;
        height: 90px;
        overflow: hidden;
        float: left;
    }
    .myClassList .info .pic a {
        display: block;
        width: 100%;
        height: 100%;
        margin: 0;
        padding: 0;
    }
    .myClassList .info .pic img {
        display: block;
        width: 100%;
    }
    .myClassList td .info .title {
        width: 280px;
        float: right;
        height: 90px;
    }
    .myClassList td .info .title a {
        display: block;
        height: 48px;
        line-height: 24px;
        overflow: hidden;
        color: #00baf2;
        margin-bottom: 12px;
    }
    .myClassList td .info .title p {
        line-height: 20px;
        margin-top: 5px;
        color: #818181;
    }
</style>