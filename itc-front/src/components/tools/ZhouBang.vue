<template>
  <div class="fly-panel fly-rank fly-rank-reply" id="LAY_replyRank">
    <h3 class="fly-panel-title">回贴周榜</h3>
    <dl>
      <!--<i class="layui-icon fly-loading">&#xe63d;</i>-->
      <dd v-for="u in topUsers" :key="u.uid">
        <router-link :to="`/userPage/${u.uid}`">
          <img :src="$fileURL+u.avatar"><cite>{{u.nickname}}</cite><i>{{u.replySum}}次回答</i>
        </router-link>
      </dd>
    </dl>
  </div>
</template>

<script>
  export default {
    name: "ZhouBang",
    data(){
      return{
        topUsers:[]
      }
    },
    created() {
      this.getWeekTop()
    },
    methods:{
      async getWeekTop() {
        const res = await this.$http.get('user/weekTop')
        if (res.code === 200) {
          this.topUsers = res.data
        }
      }
    }
  }
</script>

<style scoped>

</style>