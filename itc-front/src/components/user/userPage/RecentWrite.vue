<template>
  <div class="fly-panel">
    <h3 class="fly-panel-title"><i>{{uid===$store.getters.getUserId?'我':'TA'}}</i> 最近的文章</h3>
    <ul class="jie-row">
      <li v-for="art in articles" :key="art.artId">
        <span class="fly-jing" v-if="art.status">精</span>
        <router-link :to="`/articleDetail/${art.artId}`" class="jie-title">{{art.title}}</router-link>
        <i>{{$getFriendlyTime(art.createTime)}}</i>
        <em class="layui-hide-xs">{{art.watch}}阅/{{art.hitsComment}}答/{{art.hitsZan}}赞</em>
      </li>

      <div v-if="articles.length === 0" class="fly-none" style="min-height: 50px; padding:30px 0; height:auto;">
        <i style="font-size:14px;">没有发表任何求解</i>
      </div>
    </ul>
  </div>
</template>

<script>
  export default {
    name: "NearWrite",
    props: {
      uid: {
        type: Number
      }
    },
    data(){
      return{
        articles:[]
      }
    },
    created() {
      this.getRecentArticle()
    },
    methods:{
      async getRecentArticle(){
        const res = await this.$http.get('article/recent/' + this.uid)
        if (res.code === 200) {
          this.articles = res.data || []
        }else{
          this.$message.error("信息获取失败，请重试")
          this.$router.back()
        }
      }
    },
    watch:{
      $route(to, from) {
        console.log('rw',to)
        if (to.path.includes('userPage')) {
          this.getRecentArticle()
        }
      }
    }
  }
</script>

<style scoped>

</style>