<template>
  <div id="aArticlesList" class="bg-fa of">
    <!-- /文章列表 开始 -->
    <section class="container">
      <header class="comm-title">
        <h2 class="fl tac">
          <span class="c-333">全部文章</span>
        </h2>

        <!-- 发布文章 -->
        <div class="fr">
            <el-button type="primary" size="medium" @click="addArticle()">发布文章</el-button>
        </div>
        <div class="fr jgTag">
          <form action="#" method="post">
            <label class="h-r-s-box">
              <el-input type="text" placeholder="搜索文章" name="keyWord"  v-model="keyWord"/>
              <el-button type="submit" class="s-btn" @click="searchArticle()">
                <em class="icon18">&nbsp;</em>
              </el-button>
            </label>
          </form>
        </div>
        
      </header>
      <section class="c-sort-box">
        <!-- 文章类别显示 开始 -->
        <section class="c-s-dl">
          <dl> 
            <dt>
              <span class="c-999 fsize14">文章类别</span>
            </dt>
            <dd class="c-s-dl-li">
              <ul class="clearfix">
                <li>
                  <a title="全部" href="javascript:void(0);" @click="searchCate('')">全部</a>
                </li>
                <li v-for="(item,index) in cateList" v-bind:key="index" :class="{active:cateIndex==index}">
                  <a :title="item.name" href="javascript:void(0);" @click="searchCate(item.id, index)">{{item.name}}</a>
                </li>
              </ul>
            </dd>
          </dl>
          <div class="clear"/>
        </section>
        <!-- 课程类别显示 结束 --> 
        <div class="js-wrap">
          <section class="fr">
            <span class="c-ccc">
              <i class="c-master f-fM">1</i>/
              <i class="c-666 f-fM">1</i>
            </span>
          </section>
          <!-- 排序方式显示 开始 -->
          <section class="fl">
            <ol class="js-tap clearfix">
              <li :class="{'current bg-orange':viewCountSort!=''}">
                <a title="销量" href="javascript:void(0);" @click="searchViewCount()">销量
                <span :class="{hide:viewCountSort==''}">↓</span>
                </a>
              </li>
              <li :class="{'current bg-orange':gmtCreateSort!=''}">
                <a title="最新" href="javascript:void(0);" @click="searchGmtCreate()">最新
                <span :class="{hide:gmtCreateSort==''}">↓</span>
                </a>
              </li>
            </ol>
          </section>
          <!-- 排序方式显示 结束 -->
        </div>
        <div class="mt40">
          <!-- /无数据提示 开始-->
          <section class="no-data-wrap" v-if="data.total==0">
              <em class="icon30 no-data-ico">&nbsp;</em>
              <span class="c-666 fsize14 ml10 vam">没有相关数据，小编正在努力整理中...</span>
          </section>
          <!-- /无数据提示 结束-->
          <!-- 数据列表 开始-->
          <article v-if="data.total>0">
              <ul id="bna" class="of ">
                  <li v-for="item in data.items" :key="item.id" class="grace-article">
                      <div class="cc-l-wrap">
                          <h3 class="hLh30 txtOf mt10">
                              <a :href="'/article/'+item.id" :title="item.title" class="course-title fsize18 c-333">{{ item.title }}</a>
                          </h3>
                          <section>
                              <span :title="item.content">{{ item.content | showPrevChar }}</span>
                          </section>
                          <section class="mt10 hLh20 of">                            
                              <span class="fr" style="color:#808080" :title="item.cateName">分类：{{getCateName(item.cateId)}}</span>
                              <span class="fr jgTag" style="color:#808080" :title="item.gmtCreate">发布于：{{ item.gmtCreate }}</span>
                              <span class="fr jgTag" style="color:#808080" :title="item.memberName">作者：{{ item.memberName }}</span>
                              <span class="fl jgAttr c-ccc f-fA">
                                  <i class="c-999 f-fA">{{ item.viewCount }}人浏览</i>
                                  |
                                  <i class="c-999 f-fA">{{ item.likeCount }}人喜欢</i>
                              </span>
                          </section>
                      </div>
                  </li>
              </ul>
              <div class="clear"/>
          </article>
          <!-- /数据列表 结束-->
        </div>
        <!-- 公共分页 开始 -->
        <div>
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
        </div>
        <!-- 公共分页 结束 -->
      </section>
    </section>
    <!-- /文章列表 结束 -->
  </div>
</template>

<script>
import article from '@/api/article'
import cookie from 'js-cookie'
export default {
  data () {
    return {
      page:1, // 当前页
      data:{}, // 文章列表
      cateList: [], // 分类列表
      searchObj: {}, // 查询表单对象
      cateIndex:-1,
      viewCountSort:"",
      gmtCreateSort:"",
      cate:"", // 文章分类名
      keyWord:""
    }
  },
  //加载完渲染时
  created () {
    // 获取课程列表
    this.initArticleFirst()
    //获取分类
    this.initCate()
  },    // 数字中间加*号 value是传的参数值(后端返回的，需要你处理的数据)
  filters: {
      showPrevChar (value) { // 显示前120个字符+...
          if(value&& value.length > 100) {
              value= value.substring(0,100)+ '…';
          }
          return value;               
      }
  },
  methods: {
    // 1.查询第一页数据
    initArticleFirst(){
      article.getArticleList(1, 8,this.searchObj).then(response => {
        this.data = response.data.data
      })
    },
    // 2.查询所有文章分类
    initCate(){
      //debugger
      article.getAllCate().then(response => {
        this.cateList = response.data.data.list
      })
    },
    // 3.分页切换查询的方法
    gotoPage(page) {
      this.page = page
      article.getArticleList(page, 8, this.searchObj).then(response => {
        this.data = response.data.data
      })
    },
    // 4.点击某个文章分类，查询数据
    searchCate(cateId, index) {
      // 把index复制，为了样式生效
      this.cateIndex = index
      // 把文章分类点击Id值，赋值给searchObj
      this.searchObj.cateId = cateId;
      // 点击某个文章分类进行条件查询
      this.gotoPage(this.page)
    },
    // 5.根据阅读量排序
    searchViewCount() {
      // 设置对于变量值，为了样式生效
      this.viewCountSort = "1";
      this.gmtCreateSort = "";
      // 把值赋到searchObj
      this.searchObj.viewCountSort = this.viewCountSort;
      this.searchObj.gmtCreateSort = this.gmtCreateSort;
      // 调用方法查询
      this.gotoPage(this.page)
    },
    // 6.根据更新时间排序
    searchGmtCreate() {      
      // 设置对于变量值，为了样式生效
      this.viewCountSort = "";
      this.gmtCreateSort = "1";
      // 把值赋到searchObj
      this.searchObj.viewCountSort = this.viewCountSort;
      this.searchObj.gmtCreateSort = this.gmtCreateSort;
      // 调用方法查询
      this.gotoPage(this.page)
    },
    // 7.跳转到添加文章界面
    addArticle(){
        //从cookie获取登录信息
        var userStr = cookie.get("user_ucenter");
        // 把字符串转换为json对象
        if (userStr) { // 已登录，跳转到添加页面
            this.$router.push({ path: '/article/write' })
            this.loginInfo = JSON.parse(userStr)
        } else { // 未登录，跳转到登录页面
            this.$message({
                type: 'info',
                message: '您好，请先登录!'
            })
            this.$router.push({ path: '/login' })
        }
    },
    // 8.根据类别id获取类别名
    getCateName(cateId){
        for (let i = 0; i < this.cateList.length; i++) {
            // 获取每个一级分类
            const cate = this.cateList[i]; 
            // 比较当前courseInfo里面一级分类id和所有的一级分类id
            if (cateId === cate.id) {
                // 获取一级分类所有的二级分类
                return cate.name
            }
        }
    },
    // 9.跳转到ElasticSearch搜索后的文章界面
    searchArticle(){
        // 把字符串转换为json对象
        if (this.keyWord) { // 搜索栏有关键字
            this.$router.push({ path: '/article/search' , query: {word: this.keyWord}})
            // this.$router.push({ path: '/article/search' , props: {keyWord: this.keyWord}})
        } else { // 搜索栏无关键字        
            console.log("关键字为null")
            this.$router.push({ path: '/article/index' })
        }
    }

  }
}
</script>
<style scoped>
  .active {
    background: #bdbdbd;
  }
  .hide {
    display: none;
  }
  .show {
    display: block;
  }  
  .grace-article{	border-bottom-style:solid; border-bottom-color:#C0C0C0; border-width:1px;}
  .grace-article{ padding:12px;}
</style>
