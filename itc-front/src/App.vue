<template>
  <div id="app">
    <router-view></router-view>
    <!--<el-backtop target=".el-main" :visibility-height="20" :right="30" :bottom="80">
    </el-backtop>-->
  </div>
</template>

<script>
  export default {
    name: 'app'
  }
</script>

<style>
  @import "assets/css/app.css";
</style>
