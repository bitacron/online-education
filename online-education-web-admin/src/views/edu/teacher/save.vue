<template>
    <div class="app-container">
        <el-form label-width="120px">
            <el-form-item label="讲师名称">
                <el-input v-model="teacher.name"/>
            </el-form-item>
            <el-form-item label="讲师排序">
                <el-input-number v-model="teacher.sort" controls-position="right" :min="0"/>
            </el-form-item>
            <el-form-item label="讲师头衔">
                <el-select v-model="teacher.level" clearable placeholder="请选择">
                    <!--数据类型一定要和取出的json中的一致，否则没法回填.因此，这里value使用动态绑定的值，保证其数据类型是number-->
                    <el-option :value="1" label="高级讲师"/>
                    <el-option :value="2" label="首席讲师"/>
                </el-select>
            </el-form-item>
            <el-form-item label="讲师资历">
                <el-input v-model="teacher.career"/>
            </el-form-item>
            <el-form-item label="讲师简介">
                <el-input v-model="teacher.intro" :rows="10" type="textarea"/>
            </el-form-item>

            <!-- 讲师头像：TODO -->
            <!-- 讲师头像 -->
            <el-form-item label="讲师头像">
                <!-- 头衔缩略图 -->
                <pan-thumb :image="teacher.avatar"/>
                <!-- 文件上传按钮 -->
                <el-button type="primary" icon="el-icon-upload" @click="imagecropperShow=true">更换头像</el-button>
                <!--
                    v-show：是否显示上传组件
                    :key：类似于id，如果一个页面多个图片上传控件，可以做区分
                    :url：后台上传的url地址， 可选参数avatar指定头像上传文件夹
                    @close：关闭上传组件
                    @crop-upload-success：上传成功后的回调
                    <input type="file" name="file"/>
                -->
                <image-cropper
                    v-show="imagecropperShow"
                    :width="300"
                    :height="300"
                    :key="imagecropperKey"
                    :url="BASE_API+'/eduoss/fileoss/upload?host=avatar'"
                    field="file"
                    @close="close"
                    @crop-upload-success="cropSuccess"/>
            </el-form-item>

            <el-form-item>
                <el-button :disabled="saveBtnDisabled" type="primary" @click="saveOrUpdate">保存</el-button>
            </el-form-item>
        </el-form>
    </div>
</template>
<script>
// 引入调用teacher.js文件
import teacherApi from '@/api/edu/teacher'
import ImageCropper from '@/components/ImageCropper'
import PanThumb from '@/components/PanThumb'

export default {
    components: { ImageCropper, PanThumb },
    // 写核心代码部分
    data() { // 定义变量和初始值
        return {
            teacher: {
                name: '',
                sort: 0,
                level: 1,
                career: '',
                intro: '',
                avatar: ''
            },
            BASE_API: process.env.BASE_API, // 获取dev.en.js的地址
            imagecropperShow: false, // 上传组件是否显示
            imagecropperKey: 0, // 上传组件key值
            saveBtnDisabled: false // 保存按钮是否禁用
        }
    },
    created() { // 页面渲染之前执行，一般调用methods定义的方法
        this.init()
    },
    watch: { // 监听
        $route(to, from) { // 路由变化方式，路由发生变化，方法就会执行
            // console.log('watch $route')
            this.init()
        }
    },
    methods:{ // 创建具体的方法，调用teacher.js定义的方法
        close() { // 关闭上传弹窗的方法
            this.imagecropperShow = false // 上传弹框的值等于false，就关闭弹窗了
            this.imagecropperKey = this.imagecropperKey+1 // 上传组件初始化
        },
        cropSuccess(data) { // 上传成功之后的方法        
            // console.log(data)
            // 关弹窗
            this.imagecropperShow = false
            // 上传之后的接口返回图片的地址            
            this.teacher.avatar = data.url
            this.imagecropperKey = this.imagecropperKey+1 // 上传组件初始化
        },
        init(){
            // 判断路径是否有Id值。如果有，说明是修改方法，获取信息；如果没有则是添加方法，不用获取信息
            if (this.$route.params && this.$route.params.id) {
                // 获取路径id值
                const id = this.$route.params.id
                // 调用根据id查询的方法
                this.getInfo(id)
            } else { // 路径没有id值，做添加
                // 清空表单
                this.teacher = {avatar: 'https://online-education-chn.oss-cn-beijing.aliyuncs.com/avatar/default.jpg',}
            }
        },
        getInfo(id) {
            teacherApi.getTeacherInfo(id)
                .then(response => {
                    this.teacher = response.data.teacher
                })

        },
        saveOrUpdate() { // 修改和添加共同使用这个方法
            // 判断是修改还是添加
            // 根据teacher对象是否有id进行判断，没有id就是添加，有id就是修改
            if (!this.teacher.id) {
                // 无id，添加
                this.saveTeacher()
            } else {
                // 有id，修改
                this.updateTeacher()
            }
        },
        // 修改讲师信息
        updateTeacher() {
            teacherApi.updateTeacherInfo(this.teacher)
                .then(response => { // 修改成功
                    // 提示修改信息
                    this.$message({
                        type: 'success',
                        message: '修改成功!'
                    })
                    // 回到列表页面 路由跳转（即重定向）
                    this.$router.push({path:'/teacher/table'})
                })
        },
        // 保存
        saveTeacher() {
            teacherApi.addTeacher(this.teacher)
                .then(response => { // 添加成功
                    // 提示添加信息
                    this.$message({
                        type: 'success',
                        message: '添加成功!'
                    })
                    // 回到列表页面 路由跳转（即重定向）
                    this.$router.push({path:'/teacher/table'})
                })
        },

    }    
}
</script>
