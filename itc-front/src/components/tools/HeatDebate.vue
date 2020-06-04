<template>
  <dl class="fly-panel fly-list-one">
    <dt class="fly-panel-title">本周热议</dt>
    <dd v-for="art in articles" :key="art.artId">
      <router-link :to="`/articleDetail/${art.artId}`">{{art.title}}</router-link>
      <span><i class="iconfont icon-pinglun1"></i> {{art.hitsComment}}</span>
    </dd>

    <!-- 无数据时 -->
    <div v-if="articles.length === 0" class="fly-none">没有相关数据</div>
  </dl>
</template>

<script>
  export default {
    name: "HeatDebate",
    data(){
      return{
        articles:[]
      }
    },
    created() {
      this.getWeekTopArticle()
    },
    methods:{
      async getWeekTopArticle() {
        const res = await this.$http.get("article/weekTop")
        if (res.code === 200) {
          this.articles = res.data
        }
      }
    }
  }
</script>

<style scoped>

</style>