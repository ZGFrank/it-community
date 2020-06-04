<template>
  <div>
    <div class="fly-panel">
      <div class="fly-panel-title fly-filter">
        <a>置顶</a>
        <!--<a href="#signin" class=" layui-show-xs-block fly-right" id="LAY_goSignin"
           style="color: #FF5722;">去签到</a>-->
      </div>
      <article-list :article-data="topList"/>
    </div>
    <cate-article/>
  </div>
</template>

<script>
  import ArticleList from "../article/ArticleList";
  import CateArticle from "../interchange/CateArticle"
  export default {
    name: "Home",
    data() {
      return {
        topList: []
      }
    },
    created() {
      this.loadTop()
    },
    methods: {
      async loadTop() {
        const res = await this.$http.get('article/loadsTop')
        if (res.code === 200) {
          this.topList = res.data;
        }
      }
    },
    components: {
      ArticleList,CateArticle
    }
  }
</script>

<style scoped>

</style>