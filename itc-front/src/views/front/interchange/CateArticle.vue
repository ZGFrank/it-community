<template>
  <div>
    <div class="fly-panel" style="margin-bottom: 0;" v-if="category !== 5">
      <div class="fly-panel-title fly-filter" style="display: flex;justify-content: space-between;">
        <div style="display: flex;flex-direction: row">
          <div v-for="(item,index) in nav">
            <el-link @click="queryArticle(index,-1)" :underline="false" :class="{'layui-this':index === query.type}">{{item}}</el-link>
            <span class="fly-mid" v-if="index+1 !== nav.length"></span>
          </div>
        </div>
        <span class="fly-filter-right layui-hide-xs" style="display: flex;flex-direction: row">
            <div v-for="(item,index) in subnav">
              <el-link @click="queryArticle(-1,index)" :underline="false" :class="{'layui-this':index === query.condition}">{{item}}</el-link>
            <span class="fly-mid" v-if="index+1 !== nav.length"></span>
            </div>
        </span>
      </div>
      <article-list :article-data="articles"/>

      <div style="padding: 20px">
        <el-pagination
          background
          hide-on-single-page
          @current-change="loadArticle"
          :current-page.sync="query.currentPage"
          :page-size.sync="query.pageSize"
          layout="prev, pager, next"
          :total="total">
        </el-pagination>
      </div>
    </div>
  </div>
</template>

<script>
  import ArticleList from "../article/ArticleList"

  export default {
    name: "Quiz",
    props:{
      category:{
        type:Number,
        default:0
      }
    },
    data(){
      return {
        nav:['推荐','未结','已结','精华'],
        subnav:['按最新','按热议'],
        total:0,
        query:{
          type:0,
          category:0,
          condition:0,
          currentPage:1,
          pageSize:15
        },
        articles:[],
      }
    },
    created() {
      this.loadArticle()
    },
    methods:{
      queryArticle(type,condition) {
        if (condition === -1) {
          //查询值没有改变时直接返回
          if (this.query.type === type) return
          this.query.type = type
        }
        if (type === -1) {
          if (this.query.condition === condition) return
          this.query.condition = condition
        }
        this.query.currentPage = 1
        this.loadArticle()
      },
      async loadArticle(){
        this.query.category = this.category
        const res = await this.$http.get('article/loadsNormal',{params: this.query})
        if (res.code === 200){
          this.articles = res.data.data
          this.total = res.data.total
        }
      }
    },
    components: {
      ArticleList
    }
  }
</script>

<style scoped>

</style>