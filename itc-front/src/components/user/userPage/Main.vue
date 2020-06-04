<template>
  <div class="layui-container">
    <div class="layui-row layui-col-space15">
      <div class="layui-col-md6 fly-home-jie">
        <recent-write :uid="uid"/>
      </div>

      <div class="layui-col-md6 fly-home-da">
        <recent-reply :uid="uid"/>
       </div>
    </div>
  </div>

</template>

<script>

  import RecentReply from "./RecentReply"
  import RecentWrite from "./RecentWrite"
  export default {
    name: "Main",
    props:{
      uid:{
        type:Number
      }
    },
    components:{
      RecentReply,RecentWrite,
    }
  }
</script>

<style scoped>

</style>