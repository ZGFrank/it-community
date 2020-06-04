<template>
  <div class="fly-panel">
    <h3 class="fly-panel-title"><i>{{uid===$store.getters.getUserId?'我':'TA'}}</i> 最近的回答</h3>
    <ul class="home-jieda">
      <li v-for="reply in comments" :key="reply.artCId">
        <p>
          <span>{{$getFriendlyTime(reply.createTime)}}</span>
          在<router-link :to="`/articleDetail/${reply.artId}`">{{reply.title}}</router-link>
          中答复 <router-link :to="`/userPage/${reply.uid}`" v-if="reply.pid !== 0">{{reply.tonickname}}</router-link>：
        </p>
        <div class="home-dacontent">
          <div class="ql-editor" v-html="reply.content"></div>
        </div>
      </li>
    <div v-if="comments.length === 0" class="fly-none" style="min-height: 50px; padding:30px 0; height:auto;"><span>没有回答任何问题</span></div>
    </ul>
  </div>

</template>

<script>
  export default {
    name: "NearReply",
    props: {
      uid: {
        type: Number
      }
    },
    data() {
      return {
        comments: []
      }
    },
    created() {
      this.getRecentReply()
    },
    methods: {
      async getRecentReply() {
        const res = await this.$http.get('articleComment/recent/' + this.uid)
        if (res.code === 200) {
          this.comments = res.data || []
        } else {
          this.$message.error("信息获取失败，请重试")
          this.$router.back()
        }
      }
    },
    watch:{
      $route (to, from) {
        console.log('rr',to)
        if (to.path.includes('userPage')) {
          this.getRecentReply()
        }
      }
    }
  }
</script>

<style scoped>

</style>