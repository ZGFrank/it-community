<template>
  <div class="search-main">
    <div v-if="articles.length === 0"><span class="search-no-remind">没有查询结果</span></div>
    <article-list v-if="articles.length > 0" :article-data="articles"/>

    <div style="padding: 20px">
      <el-pagination
        background
        hide-on-single-page
        @current-change="getSearch"
        :current-page.sync="query.currentPage"
        :page-size.sync="query.pageSize"
        layout="prev, pager, next"
        :total="total">
      </el-pagination>
    </div>
  </div>
</template>

<script>
  import ArticleList from "../article/ArticleList"

  export default {
    name: "SearchArticle",
    data() {
      return {
        total: 0,
        query: {
          title: this.$route.query.title,
          currentPage: 1,
          pageSize: 15
        },
        articles: [],
      }
    },
    mounted() {
      this.getSearch()
    },
    methods: {
      async getSearch() {
        const  res = await this.$http.get('article/search',{params:this.query})
        if (res.code === 200) {
          this.articles = res.data.data || []
          this.total = res.data.total
        }else{
          await this.$router.replace("/")
        }
      }
    },
    components: {
      ArticleList
    }
  }
</script>

<style lang="less" scoped>
.search-main{
  height: 100%;
  background-color: #fff;
  //display: flex;
  //justify-content: center;/*水平主轴居中*/
  //align-items: center;/*垂直交叉轴居中*/
  padding: 30px;

  .search-no-remind{
    font-size: 20px;
    color: #4a5064;
  }
}

</style>