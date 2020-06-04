<template>
  <div class="fly-home fly-panel">
    <img :src="$fileURL+baseInfo.avatar" :alt="baseInfo.nickname">
    <h1>
      {{baseInfo.nickname}}
      <i class="iconfont icon-nan" v-if="baseInfo.gender === 1"></i>
       <i class="iconfont icon-nv" v-else></i>
      <i class="layui-badge fly-badge-vip" v-if="baseInfo.vip > 0">VIP{{baseInfo.vip}}</i>
    </h1>
    <p style="padding: 10px 0; color: #5FB878;">真实姓名：{{baseInfo.name}}</p>
    <p class="fly-home-info">
      <i class="iconfont icon-kiss" title="学币"></i><span style="color: #FF7200;">{{baseInfo.learnCoin}} 学币</span>
      <i class="iconfont icon-shijian"></i><span>{{baseInfo.classGrade}}</span>
      <i class="iconfont icon-chengshi"></i><span>来自{{baseInfo.city}}</span>
    </p>

    <p class="fly-home-sign">（{{baseInfo.signature || '这个人懒得留下签名'}}）</p>
  </div>
</template>

<script>
  export default {
    name: "Header",
    props:{
      uid:{
        type:Number
      }
    },
    data(){
      return{
        baseInfo:{}
      }
    },
    created() {
      this.getUserBaseInfo()
    },
    methods:{
      //获取用户基本信息
      async getUserBaseInfo() {
        const res = await this.$http.get('user/base/' + this.uid)
        if (res.code === 200) {
          this.baseInfo = res.data
        }else{
          this.$message.error("获取失败，请重新进入")
          this.$router.back()
        }
      }
    },
    watch:{
      $route(to, from) {
        console.log('header',to)
        if (to.path.includes('userPage')) {
          this.getUserBaseInfo()
        }
      }
    }

  }
</script>

<style scoped>

</style>