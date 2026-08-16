<template>
    <div class="app-container">
        <h2 style="text-align: center;">发布轮播图</h2>

        <el-form label-width="120px">
            <el-form-item label="轮播图标题">
                <el-input v-model="banner.title" placeholder="banner"/>
            </el-form-item>

            <el-form-item label="轮播图链接">
                <el-input v-model="banner.linkUrl" placeholder="轮播图链接"/>
            </el-form-item>
            <el-form-item label="排序">
                <el-input-number v-model="banner.sort" :min="1" controls-position="right"/>
            </el-form-item>
            <!-- 轮播图-->
            <el-form-item label="轮播图">
                <el-upload
                    :show-file-list="false"
                    :on-success="handleAvatarSuccess"
                    :before-upload="beforeAvatarUpload"
                    :action="BASE_API+'/eduoss/fileoss/upload?host=banner'"
                    class="avatar-uploader">
                    <img :src="banner.imageUrl" :width="500" :height="300">
                </el-upload>
            </el-form-item>
            <el-form-item>
                <el-button :disabled="saveBtnDisabled" type="primary" @click="saveOrUpdate">保存</el-button>
            </el-form-item>
        </el-form>
    </div>
</template>

<script>
// 引入调用banner.js文件
import bannerApi from '@/api/banner'
import Tinymce from '@/components/Tinymce' // 引入组件
export default {    
    components: { Tinymce }, // 声明组件
    // 写核心代码部分
    data() { // 定义变量和初始值
        return {
            banner: {
                title: '',
                sort: 0,
                linkUrl: '',
                imageUrl: ''
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
        // 上传封面成功调用的方法
        handleAvatarSuccess(res, file) {
            console.log(res)// 上传响应
            console.log(URL.createObjectURL(file.raw))// base64编码
            this.banner.imageUrl = res.data.url
        },
        // 上传之前调用的方法
        beforeAvatarUpload(file) {
            const isJPG = file.type === 'image/jpeg'
            const isLt2M = file.size / 1024 / 1024 < 2
            if (!isJPG) {
                this.$message.error('上传头像图片只能是 JPG 格式!')
            }
            if (!isLt2M) {
                this.$message.error('上传头像图片大小不能超过 2MB!')
            }
            return isJPG && isLt2M
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
                this.banner = {imageUrl: 'https://online-education-chn.oss-cn-beijing.aliyuncs.com/banner/default.jpg',}
            }
        },
        getInfo(id) {
            bannerApi.getBannerInfoById(id)
                .then(response => {
                    this.banner = response.data.item
                })

        },
        saveOrUpdate() { // 修改和添加共同使用这个方法
            // 判断是修改还是添加
            // 根据teacher对象是否有id进行判断，没有id就是添加，有id就是修改
            if (!this.banner.id) {
                // 无id，添加
                this.saveTeacher()
            } else {
                // 有id，修改
                this.updateTeacher()
            }
        },
        // 修改讲师信息
        updateTeacher() {
            bannerApi.updateBannerInfo(this.banner)
                .then(response => { // 修改成功
                    // 提示修改信息
                    this.$message({
                        type: 'success',
                        message: '修改成功!'
                    })
                    // 回到列表页面 路由跳转（即重定向）
                    this.$router.push({path:'/cms/banner/list'})
                })
        },
        // 保存
        saveTeacher() {
            bannerApi.addBannerInfo(this.banner)
                .then(response => { // 添加成功
                    // 提示添加信息
                    this.$message({
                        type: 'success',
                        message: '添加成功!'
                    })
                    // 回到列表页面 路由跳转（即重定向）
                    this.$router.push({path:'/cms/banner/list'})
                })
        },

    }    
}
</script>
