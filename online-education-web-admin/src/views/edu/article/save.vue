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
                <el-button :disabled="saveBtnDisabled" type="primary" @click="saveOrUpdate">保存</el-button>
            </el-form-item>
        </el-form>
    </div>
</template>
<script>
import articleApi from '@/api/edu/article'
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
            // 判断路径是否有Id值。如果有，说明是修改方法，获取信息；如果没有则是添加方法，不用获取信息
            if (this.$route.params && this.$route.params.id) {
                // 获取路径id值
                const id = this.$route.params.id
                // 调用根据id查询的方法                
                this.getInfo(id)
            } else { // 路径没有id值，做添加
                // 登录时获取用户信息，存入到Vue的全局变量$store中，以后可随时调用
                var userId = this.$store.state.user.id; // 获取当前录用户id，发文章是添加
                var nickName = this.$store.state.user.nickName; // 获取当前登录用户昵称，发文章是添加
                // 清空表单
                this.article = {memberId: userId, memberName: nickName,}
            }
            this.getListCategories()
        },
        getInfo(id) {
            articleApi.getArticleInfo(id)
                .then(response => {
                    this.article = response.data.articleInfo
                })

        },
        saveOrUpdate() { // 修改和添加共同使用这个方法
            // 判断是修改还是添加
            // 根据article对象是否有id进行判断，没有id就是添加，有id就是修改
            if (!this.article.id) {
                // 无id，添加
                this.saveArticle()
            } else {
                // 有id，修改
                this.updateArticle()
            }
        },
        // 修改讲师信息
        updateArticle() {
            articleApi.updateArticleInfo(this.article)
                .then(response => { // 修改成功
                    // 提示修改信息
                    this.$message({
                        type: 'success',
                        message: '修改成功!'
                    })
                    // 回到列表页面 路由跳转（即重定向）
                    this.$router.push({path:'/article/list'})
                })
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
                    this.$router.push({path:'/article/list'})
                })
        },
        // 查询所有的文章类别
        getListCategories() {
            articleApi.getListCategories().then(response => {
                this.cateList=response.data.list
            })
        },

    }    
}
</script>
