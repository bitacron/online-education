<template>
    <div class="app-container">

        <el-button
            type="primary"
             size="medium" 
            @click="openSaveDialog()">添加分类
        </el-button>

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
                    {{ scope.$index+1 }}
                </template>
            </el-table-column>
            <el-table-column prop="name" label="名称" width="200" />         
            <el-table-column prop="description" label="文章描述" />   
            <el-table-column prop="sort" label="排序" width="100" />            
            <el-table-column prop="articleCount" label="文章数目" width="100" />
            <el-table-column prop="gmtCreate" label="添加时间" width="160"/>            
            <el-table-column prop="gmtModified" label="修改时间" width="160"/>
            <el-table-column label="操作" width="200" align="center">
                <template slot-scope="scope">
                    <el-button type="danger" size="mini"
                        @click="openUpdateDialog(scope.row.id)">修改分类
                    </el-button>
                    <!-- <router-link :to="'/teacher/edit/'+scope.row.id">
                        <el-button type="primary" size="mini" icon="el-icon-edit">修改</el-button>
                    </router-link> -->
                    <el-button type="danger" size="mini" icon="el-icon-delete"
                        @click="removeDataById(scope.row.id)">删除</el-button>
                </template>
            </el-table-column>
        </el-table>
        <!-- 添加与修改分类的弹窗 -->
        <el-dialog :visible.sync="dialogFormVisible" title="添加分类">
            <el-form ref="cate" :model="cate" label-width="120px">
                <el-form-item label="分类名称" prop="name">
                    <el-input v-model="cate.name"/>
                </el-form-item>
                <el-form-item label="分类描述" prop="path">
                    <el-input v-model="cate.description"/>
                </el-form-item>
                <el-form-item label="分类排序" prop="path">
                    <el-input v-model="cate.sort"/>
                </el-form-item>
            </el-form>
        <div slot="footer" class="dialog-footer">
            <el-button @click="restData()">取 消</el-button>
            <el-button type="primary" @click="saveOrUpdateCate()">确 定</el-button>
        </div>
        </el-dialog>

    </div>
</template>

<script>
import cateApi from '@/api/edu/cate'

export default {

    data() { // 定义变量和初始值
        return {
            listLoading: false, // 是否显示loading信息
            list: null, // 查询之后接口返回集合
            dialogFormVisible: false, // 添加与修改的窗口
            cate: {// 分类对象
                name: '', // 分类名
                sort: 0, // 排序
                description: '', // 分类描述
            },
        }
    },
    created() { // 页面渲染之前执行，一般调用methods定义的方法
        // 调用
        this.getList()
    },
    methods:{ // 创建具体的方法，调用teacher.js定义的方法
        // 讲师列表的方法
        getList() {
            cateApi.getListCategories()
                .then(response => { // 请求成功
                    //response接口返回的数据
                    // console.log(response)
                    this.list = response.data.items
                })
                .catch(error => { // 请求失败
                    console.log(error)
                })
        },
        // 打开修改分类的窗口
        openUpdateDialog(id) {
            // 打开弹窗
            this.dialogFormVisible = true
            // 调用接口
            cateApi.getCategoriesInfo(id).then(response => {
                this.cate = response.data.categories
            })
        },
        // 打开添加分类的窗口
        openSaveDialog() {            
            this.dialogFormVisible = true,
            this.cate.name='',
            this.cate.sort=0,
            this.cate.description=''
        },
        // 点击取消按键后执行的方法，清除数据
        restData() {
            this.dialogFormVisible = false
            this.cate = {}
        },
        // 点击确定按钮后执行的方法，添加或修改分类
        saveOrUpdateCate(){
            if (!this.cate.id) { // 添加
                this.saveCate() // 分类的添加
            } else { // 修改
                this.updateCate()
            }
        },  
        saveCate(){
            cateApi.addCategories(this.cate).then(response => {
                // 关闭弹窗                
                this.dialogFormVisible = false
                // 提示信息
                this.$message({
                    type: 'success',
                    message: '添加分类成功!'
                })  
                // 刷新页面
                this.paramsFlush()
                // 把cate对象中的属性值清空，以免对下次添加造成干扰                                
            })
        },
        updateCate(){
            cateApi.updateCategoriesInfo(this.cate).then(response => {
                // 关闭弹窗
                this.dialogFormVisible = false
                // 提示信息
                this.$message({
                    type: 'success',
                    message: '修改分类成功!'
                })
                // 刷新页面
                this.paramsFlush()
                // 把cate对象中的属性值清空，以免对下次添加造成干扰
            })
        },
        paramsFlush(){ // 添加或修改分类后初始化页面参数，防止参数下一次操作产生影响
            this.dialogChapterFormVisible = false// 如果保存成功则关闭对话框
            this.getList()// 刷新列表
            this.cate.name = ''// 重置章节标题
            this.cate.sort = 0// 重置章节排序     
            this.cate.description='',// 重置章节描述
            this.saveBtnDisabled = false
            this.cate.id = '' // 重置章节id
        },
        // 根据id删除
        removeDataById(id) {
            // alert(id)
            this.$confirm('此操作将永久删除该分类记录, 是否继续?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {// 点击确定，执行then方法
                cateApi.deleteCategoriesById(id)
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
    }    
}
</script>
