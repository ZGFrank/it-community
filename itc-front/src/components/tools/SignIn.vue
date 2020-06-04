<template>
  <div class="fly-panel fly-signin">
    <div class="fly-panel-title">
      签到
      <i class="fly-mid"></i>
      <el-link type="primary" @click="explainShow = true" class="fly-link" :underline="false">说明</el-link>
      <span class="fly-signin-days">已连续签到<cite>{{baseInfo.days || 0}}</cite>天</span>
    </div>
    <div class="fly-panel-main fly-signin-main">
      <div v-if="baseInfo == null || !baseInfo.sign">
        <button class="layui-btn layui-btn-danger" @click="signIn">今日签到</button>
        <span>可获得<cite>{{baseInfo.learnCoin || 5}}</cite>学币</span>
      </div>
      <!-- 已签到状态 -->
     <div v-else>
       <button class="layui-btn layui-btn-disabled">今日已签到</button>
       <span>获得了<cite>{{baseInfo.learnCoin}}</cite>学币</span>
     </div>
    </div>
    <el-dialog title="签到说明" :visible.sync="explainShow" width="330px">
      <el-table :data="gridData" stripe border>
        <el-table-column property="days" label="连续签到天数" width="130"></el-table-column>
        <el-table-column property="learnCoin" label="每天可获取学币数" width="150"></el-table-column>
      </el-table>
    </el-dialog>
  </div>
</template>

<script>
  import signExplain from '@/assets/json/signInExplain.json'
  export default {
    name: "SignIn",
    data(){
      return{
        uid:this.$store.getters.getUserId,
        baseInfo:{},
        explainShow:false,
        gridData:signExplain
      }
    },
    created() {
      if (this.uid != null) {
        this.getSignInInfo()
      }
    },
    methods:{
      //获取用户签到信息
      async getSignInInfo(){
        const res = await this.$http.get('sign/info/'+this.uid)
        if (res.code === 200) {
          this.baseInfo = res.data
        }
      },
      async signIn(){
        if (this.uid == null) {
          return this.$router.push('/login')
        }
        const res = await this.$http.post('sign/' + this.uid)
        if (res.code === 200) {
          this.getSignInInfo()
        }
      }
    }
  }
</script>

<style scoped>

</style>