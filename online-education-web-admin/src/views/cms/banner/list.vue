<template>
    <div class="app-container">
        <!-- 工具条 -->
        <div>
            <el-button type="primary" size="medium" @click="addBanner()" v-if="hasPerm('banner.add')">添加轮播图</el-button>
        </div>
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
            <el-table-column prop="title" label="名称" width="80" />
            <el-table-column label="轮播图信息" width="470" align="center">
                <template slot-scope="scope">
                    <div class="info">
                        <div class="pic">
                            <img :src="scope.row.imageUrl" alt="scope.row.title" width="150px">
                        </div>
                    </div>
                </template>
            </el-table-column>
            <el-table-column prop="linkUrl" label="链接地址" />
            <el-table-column label="创建时间" align="center">
                <template slot-scope="scope">
                    {{ scope.row.gmtCreate.substr(0, 10) }}
                </template>
            </el-table-column>
            <el-table-column prop="sort" label="排序" width="60" />
            <el-table-column label="操作" width="200" align="center">
                <template slot-scope="scope">
                    <router-link :to="'/cms/banner/update/'+scope.row.id">
                        <el-button type="primary" size="mini" icon="el-icon-edit">修改</el-button>
                    </router-link>
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
// 引入调用banner.js文件
import banner from '@/api/banner'

export default {
    // 写核心代码部分
    // data:{
    // },
    data() { // 定义变量和初始值
        return {
            listLoading: false, // 是否显示loading信息
            list: null, // 查询之后接口返回集合
            page: 1, // 当前页
            limit: 5, // 每页记录数
            total: 0, // 总记录数
            bannerQuery: {} // 条件封装对象
        }
    },
    created() { // 页面渲染之前执行，一般调用methods定义的方法
        // 调用
        this.getList()
    },
    methods:{ // 创建具体的方法，调用teacher.js定义的方法
        // 讲师列表的方法
        getList(page = 1) {
            this.page = page
            banner.getListBanner(this.page, this.limit, this.bannerQuery)
                .then(response => { // 请求成功
                    //response接口返回的数据
                    // console.log(response)
                    this.list = response.data.items
                    this.total = response.data.total
                    console.log(this.list)
                    console.log(this.total)
                })
                .catch(error => { // 请求失败
                    console.log(error)
                })
        },
        // 根据id删除
        removeDataById(id) {
            // alert(id)
            this.$confirm('此操作将永久删除该讲师记录, 是否继续?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {// 点击确定，执行then方法
                banner.deleteBannerById(id)
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
        addBanner(){
            this.$router.push({ path: '/cms/banner/add' })
        },
    }    
}
</script>
