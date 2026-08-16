<template>
    <div class="app-container">
        <el-form label-width="120px">
            <el-form-item label="信息描述">
                <el-tag type="info">excel模版说明</el-tag>
                <el-tag>
                    <i class="el-icon-download"/>
                    <a :href="OSS_PATH +'/excel/fileName.xlsx'">点击下载模版</a>
                    <!-- 我们选择上传到本地，不使用OSS，所以注释上一行，改成如下地址 -->
                    <!-- <a :href="'/static/fileName.xlsx'">点击下载模版</a> -->
                </el-tag>

            </el-form-item>

            <el-form-item label="选择Excel">
                <el-upload
                        ref="upload"
                        :auto-upload="false"
                        :on-success="fileUploadSuccess"
                        :on-error="fileUploadError"
                        :disabled="importBtnDisabled"
                        :limit="1"
                        :action="BASE_API+'/eduservice/subject/addSubject'"
                        name="file"
                        accept="application/vnd.ms-excel">
                    <el-button slot="trigger" size="small" type="primary">选取文件</el-button>
                    <el-button
                        :loading="loading"
                        style="margin-left: 10px;"
                        size="small"
                        type="success"
                        @click="submitUpload">上传到服务器</el-button>
                </el-upload>
            </el-form-item>
        </el-form>
    </div>
</template>
<script>
export default {
    // 写核心代码部分
    data() { // 定义变量和初始值
        return {
            BASE_API: process.env.BASE_API, // 接口API地址
            OSS_PATH: process.env.OSS_PATH, // 阿里云OSS地址，暂时把excel存到本地了，没有使用oss
            // fileUploadBtnText: '上传到服务器', // 按钮文字
            importBtnDisabled: false, // 按钮是否禁用,
            loading: false
        }
    },
    created() { // 页面渲染之前执行，一般调用methods定义的方法
        
    },
    methods:{ // 创建具体的方法
        submitUpload() {
            this.fileUploadBtnText = '正在上传'
            this.importBtnDisabled = true
            this.loading = true
            // js: docment.getElementById("upload").submit()
            this.$refs.upload.submit()
        },
        fileUploadSuccess(response) {
            // 提示修改信息
            this.loading = false
            this.$message({
                type: 'success',
                message: '添加课程分类成功!'
            })
            // 跳转到课程分类界面
            this.$router.push({path:'/subject/list'})
        },
        // 上传失败
        fileUploadError(response) {
           this.loading = false
            this.$message({
                type: 'error',
                message: '添加课程分类失败!'
            })
        }
    }    
}
</script>
