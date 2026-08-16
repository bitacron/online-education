<template>
    <div class="app-container">
        <el-form label-width="120px">
            <el-form-item label="文章标题">
                <el-input v-model="article.title"/>
            </el-form-item>

            <el-form-item label="文章内容">
                <el-input v-model="article.content" :rows="10" type="textarea"/>
            </el-form-item>
            <el-form-item label="文章类别">
                <el-select v-model="article.cateId" placeholder="文章类别">
                    <el-option
                        v-for="cate in cateList"
                        :key="cate.id"
                        :label="cate.name"
                        :value="cate.id"/>
                </el-select>
            </el-form-item>
            <el-form-item>
                <el-button :disabled="saveBtnDisabled" type="primary" @click="saveArticle">保存</el-button>
            </el-form-item>
        </el-form>
    </div>
</template>
<script>
import articleApi from '@/api/article'
import cookie from 'js-cookie'
export default {
    // 写核心代码部分
    data() { // 定义变量和初始值
        return {
            article: {
                memberId: '',
                memberName: '',
                content: '',
                cateId: '', 
            },
            cateList:[],
            saveBtnDisabled: false, // 保存按钮是否禁用
            loginInfo: {
                id: '',
                avatar: '',
                mobile: '',
                nickname: '',
                sex: ''
            }
        }
    },
    created() { // 页面渲染之前执行，一般调用methods定义的方法
        this.init()
    },
    watch: { // 监听
        $route(to, from) { // 路由变化方式，路由发生变化，方法就会执行
            this.init()
        }
    },
    methods:{
        init(){
            // 从cookies获取到用户信息
            this.showInfo();
            // 登录时获取用户信息，存入到Vue的全局变量$store中，以后可随时调用
            var userId = this.loginInfo.id; // 获取当前录用户id，发文章是添加
            // console.log(userId)
            var nickName = this.loginInfo.nickname; // 获取当前登录用户昵称，发文章是添加
            this.article = {memberId: userId, memberName: nickName,}            
            this.getListCategories()
        },

        // 保存
        saveArticle() {
            articleApi.addArticle(this.article)
                .then(response => { // 添加成功
                    // 提示添加信息
                    this.$message({
                        type: 'success',
                        message: '添加成功!'
                    })
                    // 回到列表页面 路由跳转（即重定向）
                    this.$router.push({path:'/article'})
                })
        },
        // 查询所有的文章类别
        getListCategories() {
            articleApi.getListCategories().then(response => {
                this.cateList=response.data.data.list
            })
        },
        // 创建方法：从cookie中获取信息
        showInfo() {
            // 从cookie中获取信息
            var userStr = cookie.get("user_ucenter");
            // 把字符串转换为json对象
            if (userStr) {
                this.loginInfo = JSON.parse(userStr)
            }
        },

    }    
}
</script>
