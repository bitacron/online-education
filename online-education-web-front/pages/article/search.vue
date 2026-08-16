<template>
  <div id="aArticlesList" class="bg-fa of">
    <!-- /文章列表 开始 -->
    <section class="container">
      <header class="comm-title">
        <h2 class="fl tac">
          <span class="c-333">搜索结果</span>
        </h2>
        <span class="fl" style="color:#808080">&nbsp;&nbsp;&nbsp;&nbsp;共计为您搜索出</span>
        <span class="fl" style="color:red">{{data.total}}</span>
        <span class="fl" style="color:#808080">个结果</span>
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
        <div class="mt40">
          <!-- /无数据提示 开始-->
          <section class="no-data-wrap" v-if="data.total==0">
              <em class="icon30 no-data-ico">&nbsp;</em>
              <span class="c-666 fsize14 ml10 vam">未检索到文章，换个关键词试试吧...</span>
          </section>
          <!-- /无数据提示 结束-->
          <!-- 数据列表 开始-->
          <article v-if="data.total>0">
              <ul id="bna" class="of ">
                  <li v-for="item in data.items" :key="item.id" class="grace-article">
                      <div class="cc-l-wrap">
                          <h3 class="hLh30 txtOf mt10">
                              <!-- <a :href="'/article/'+item.id" :title="item.title" class="course-title fsize18 c-333">{{ item.title }}</a> -->
                              <a :href="'/article/'+item.id" v-html="item.title" class="course-title fsize18 c-333"></a>
                          </h3>
                          <section>
                              <!-- <div  :title="item.content">{{ item.content | showPrevChar }}</div> -->
                              <div  v-html="item.content"></div>
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
      cateIndex:-1,
      viewCountSort:"",
      gmtCreateSort:"",
      cate:"", // 文章分类名
      keyWord:""
    }
  },
  //加载完渲染时
  created () {
    this.init()
    this.getRouterData()
    this.initCate()
    // 获取课程列表
    this.initSearchArticleFirst()
  },    // 数字中间加*号 value是传的参数值(后端返回的，需要你处理的数据)
    watch: { // 监听
        $route(to, from) { // 路由变化方式，路由发生变化，方法就会执行
            // console.log('watch $route')
            this.init()
            this.initSearchArticleFirst()
        }
    },
  filters: {
      showPrevChar (value) { // 显示前120个字符+...
          if(value&& value.length > 100) {
              value= value.substring(0,100)+ '…';
          }
          return value;               
      }
  },
  methods: {
    getRouterData() {
      this.keyWord = this.$route.query.word
      console.log('The keyWord is : ', this.keyWord)
    },
    // 1.查询第一页数据   
    initSearchArticleFirst(){
      article.searchArticle(1, 8,this.keyWord).then(response => {
        this.data = response.data.data
        console.log(response.data.data)
      })
    },
    // 3.分页切换查询的方法
    gotoPage(page) {
      this.page = page
      article.searchArticle(page, 8, this.keyWord).then(response => {
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
    init(){
      this.data = {}
      this.page = 1;
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
