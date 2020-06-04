<template>
  <div class="fly-panel">
    <h3 class="fly-panel-title">系统通知</h3>
    <ul class="fly-panel-main fly-list-static">
      <li v-for="(inform,index) in news" :key="inform.newsId">
        <el-link :type="type[inform.level+1]" @click="openDetail(index)" :underline="false">{{inform.title}}</el-link>
      </li>
      <li v-if="news.length===0">暂无通知</li>
    </ul>
  </div>
</template>

<script>
  export default {
    name: "News",
    data(){
      return{
        type:['primary','warning','danger'],
        news:[]
      }
    },
    created() {
      this.getNews()
    },
    methods:{
      openDetail(index){
        this.$notify({
          title: this.news[index].title,
          message: this.news[index].content,
          duration: 0,
          offset: 100
        })
      },
      async getNews(){
        const res = await this.$http.get('news/front')
        if (res.code === 200) {
          this.news = res.data
        }
      }
    }
  }
</script>

<style scoped>

</style>