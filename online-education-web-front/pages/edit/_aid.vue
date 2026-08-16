<template>
    <div class="app-container">
        <el-form label-width="120px">
            <el-form-item label="文章标题">
                <el-input v-model="article.title"/>
            </el-form-item>
            <el-form-item label="文章简介">
                <el-input v-model="article.summary"/>
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
                <el-button :disabled="saveBtnDisabled" type="primary" @click="updateArticle">保存</el-button>
            </el-form-item>
        </el-form>
    </div>
</template>
<script>
import articleApi from '@/api/article'

export default {
    asyncData({ params, error }) {
        return { 
            articleId: params.aid
        }
    },
    // 写核心代码部分
    data() { // 定义变量和初始值
        return {
            article: {
                memberId: '',
                memberName: '',
                content: '',
                title:'',
                summary: '',
                cateId: '', 
            },
            cateList:[],
            saveBtnDisabled: false // 保存按钮是否禁用
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
            // 调用根据id查询的方法                
            this.getInfo(this.articleId)
            this.getListCategories()
        },
        getInfo(id) {
            articleApi.getArticleInfo(id)
                .then(response => {
                    this.article = response.data.data.articleWebVo
                })

        },
        // 修改信息
        updateArticle() {
            articleApi.updateArticleInfo(this.article)
                .then(response => { // 修改成功
                    // 提示修改信息
                    this.$message({
                        type: 'success',
                        message: '修改成功!'
                    })
                    // 回到列表页面 路由跳转（即重定向）
                    this.$router.push({path:'/article/'+ this.articleId })
                })
        },
        // 查询所有的文章类别
        getListCategories() {
            articleApi.getListCategories().then(response => {
                this.cateList=response.data.data.list
            })
        },

    }    
}
</script>
