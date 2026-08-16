<template>
  <div id="aCoursesList" class="bg-fa of">
    <!-- 文章详情 开始 -->
    <section class="container">    
      <!-- 文章所属分类 开始 -->
      <section class="path-wrap txtOf hLh30">
        <a href="#" title class="c-999 fsize14">首页</a>
        \
        <a href="#" title class="c-999 fsize14">文章列表</a>
        \
        <span class="c-333 fsize14">{{ articleWebVo.cate }}</span>
      </section>
      <!-- 文章所属分类 结束 -->
      <!-- 文章基本信息 开始 -->
      <div>
          <section class="ml20 mr15">
              <h2 class="hLh30 txtOf mt15" align="center">
                  <span class="c-000000 fsize24">{{ articleWebVo.title }}</span>
              </h2>
          </section>

          <aside class="ml20 mr15 fr">
              <section class="c-attr-undis">
                  <span class="c-999 fsize14">作者： {{ articleWebVo.memberName }}&nbsp;&nbsp;&nbsp;</span>
              </section>
              <section class="">
                  <span class="c-999 fsize14">发表于：</span>
                  <b class="c-999" style="fsize14">{{ articleWebVo.gmtCreate }}</b>
              </section>
              <section>
                  <span class="c-999 f-fM">点赞数 {{ articleWebVo.likeCount }}</span>
                  <span>&nbsp; &nbsp;</span>
                  <span class="c-999 f-fM">浏览数 {{ articleWebVo.viewCount }}</span>
              </section>                  
          </aside>
          <div class="clear"/>
      </div>
      <!-- 文章基本信息 结束 -->
      <div class="mt20 c-infor-box">
        <article class="fl col-7">
          <section class="mr30">
            <div class="i-box">
              <article class="ml10 mr10 pt20">
                <!-- 文章内容 开始-->
                <div>
                    <section class="course-txt-body">
                        <!-- 将内容中的html翻译过来 -->
                        <p v-html="articleWebVo.content"></p>
                    </section>
                </div>
                <!-- 文章内容 结束 -->
                <!-- /课程评论 开始 -->
                <div class="mt50 commentHtml">
                    <div>
                        <h6 class="c-c-content c-infor-title" id="i-art-comment">
                            <span class="commentTitle">课程评论</span>
                        </h6>
                        <section class="lh-bj-list pr mt20 replyhtml">
                            <ul>
                                <li class="unBr">
                                    <aside class="noter-pic">
                                    <img width="50" height="50" class="picImg" src="~/assets/img/avatar-boy.gif">
                                    </aside>
                                    <div class="of">
                                    <section class="n-reply-wrap">
                                        <fieldset>
                                        <textarea name="" v-model="comment.content" placeholder="输入您要评论的文字" id="commentContent"></textarea>
                                        </fieldset>
                                        <p class="of mt5 tar pl10 pr10">
                                            <span class="fl "><tt class="c-red commentContentmeg" style="display: none;"></tt></span>
                                            <input type="button" @click="addComment()" value="回复" class="lh-reply-btn">
                                        </p>
                                    </section>
                                    </div>
                                </li>
                            </ul>
                        </section>
                        <section class="">
                            <section class="question-list lh-bj-list pr">
                                <ul class="pr10">
                                <li v-for="(comment,index) in data.items" v-bind:key="index">
                                    <aside class="noter-pic">
                                        <img width="50" height="50" class="picImg" :src="comment.avatar">
                                        </aside>
                                    <div class="of">
                                        <span class="fl"> 
                                        <font class="fsize12 c-blue"> 
                                        {{comment.nickname}}</font>
                                        <font class="fsize12 c-999 ml5">评论：</font></span>
                                    </div>
                                    <div class="noter-txt mt5">
                                        <p>{{comment.content}}</p>
                                    </div>
                                    <div class="of mt5">
                                        <span class="fr"><font class="fsize12 c-999 ml5">{{comment.gmtCreate}}</font></span>
                                    </div>
                                    </li> 
                                </ul>
                            </section>
                        </section>                        
                        <!-- 公共分页 开始 -->
                        <div class="paging">
                            <!-- undisable这个class是否存在，取决于数据属性hasPrevious -->
                            <a
                            :class="{undisable: !data.hasPrevious}"
                            href="#"
                            title="首页"
                            @click.prevent="gotoPage(1)">首</a>
                            <a
                            :class="{undisable: !data.hasPrevious}"
                            href="#"
                            title="前一页"
                            @click.prevent="gotoPage(data.current-1)">&lt;</a>
                            <a
                            v-for="page in data.pages"
                            :key="page"
                            :class="{current: data.current == page, undisable: data.current == page}"
                            :title="'第'+page+'页'"
                            href="#"
                            @click.prevent="gotoPage(page)">{{ page }}</a>
                            <a
                            :class="{undisable: !data.hasNext}"
                            href="#"
                            title="后一页"
                            @click.prevent="gotoPage(data.current+1)">&gt;</a>
                            <a
                            :class="{undisable: !data.hasNext}"
                            href="#"
                            title="末页"
                            @click.prevent="gotoPage(data.pages)">末</a>
                            <div class="clear"/>
                        </div>
                        <!-- 公共分页 结束 -->
                    </div>
                </div>
               <!-- 课程评论 结束 -->
              </article>
            </div>
          </section>
        </article>
        <aside class="fl col-3">
          <div class="i-box">            
            <!-- 编辑文章 -->
            <span v-if="(loginInfo.id) === (articleWebVo.memberId)  " class="jgTag c-attr-mt">
                <el-button type="primary" size="medium" icon="el-icon-edit" @click="editArticle()">编辑文章</el-button>
            </span>            
            <!-- 文章作者 开始-->
            <div>
                <section class="c-infor-tabTitle c-tab-title">
                    <a title href="javascript:void(0)">作者信息</a>
                </section>
                <section class="stud-act-list">
                    <ul style="height: auto;">
                        <li>
                            <div class="u-face">
                                <a :href="'/member/'+articleWebVo.memberId" target="_blank">
                                    <img :src="articleWebVo.avatar" width="50" height="50" alt>
                                </a>
                            </div>
                            <section class="hLh30 txtOf">
                                <a :href="'/member/'+articleWebVo.memberId" class="c-333 fsize16 fl" target="_blank">{{ articleWebVo.memberName }}</a>
                            </section>
                        </li>
                    </ul>
                </section>
            </div>
            <!-- 文章作者 结束 -->
          </div>
        </aside>
        <div class="clear"></div>
      </div>
    </section>
    <!-- 文章详情 结束 -->
  </div>
</template>

<script>
import article from "@/api/article"
import comment from '@/api/comment'
import cookie from 'js-cookie'
export default {
  asyncData({ params, error }) {
    return { 
        articleId: params.id
    }
  },
  data() {
    return {
      data:{},
      page:1,
      limit:4,
      total:10,
      comment:{
        content:'',
        articleId:''
      },
      articleWebVo:{},
      cateList:[],
      loginInfo: {
        id: '',
        age: '',
        avatar: '',
        mobile: '',
        nickname: '',
        sex: ''
      }
    }
  },
  created() {
    this.showInfo(),
    this.initArticleInfo()
    this.initComment()
  },
    methods:{
    //获取课程详情
    initArticleInfo() {
      article.getArticleInfo(this.articleId).then(response => {
        this.articleWebVo=response.data.data.articleWebVo
      })
    },
    // 编辑文章
    editArticle(){
        //跳转到编辑页面
        this.$router.push({ path: '/edit/'+ this.articleId })
    },
    // 从cookie中获取信息
    showInfo() {
      // 从cookie中获取信息
      var userStr = cookie.get("user_ucenter");
      // 把字符串转换为json对象
      if (userStr) {
        this.loginInfo = JSON.parse(userStr)
      }
    },
    initComment(){
       comment.getArtilcePageList(this.page, this.limit, this.articleId).then(response => {
           this.data = response.data.data
       })
    },
    addComment(){
        this.comment.articleId = this.articleId
        comment.addArtilceComment(this.comment).then(response => {
            if(response.data.success){
                this.$message({
                    message: "评论发送成功",
                    type: "success",
                });
                this.comment.content = ''
                this.initComment()
            }
        })
    },
    gotoPage(page){
        comment.getArtilcePageList(page, this.limit,this.articleId).then(response => {
            this.data = response.data.data
        })
    },
  }

}
</script>