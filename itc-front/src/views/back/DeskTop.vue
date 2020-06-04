<template>
  <div class="desk-top">
    <el-card class="box-card">
      <div class="xi-tong">IT 社区系统后台</div>
      <div class="welcome">欢迎您登录！</div>
    </el-card>
  </div>
</template>

<script>
  export default {
    name: 'desktop'
  }
</script>

<style lang="less" scoped>
  .desk-top{
    width: 100%;
    height: 100%;
    .box-card{
      width: 100%;
      height: 100%;
    }
  }
  .xi-tong{
    font-size: 60px;
    font-weight: bold;
    position: absolute;
    top: 50%;
    left: 50%;
    transform: translate(-25%,-25%);
    color: #1E9FFF;
    font-family: "Times New Roman",serif;
  }
  .welcome{
    font-size: 30px;
    font-weight: 700;
    position: absolute;
    font-family: 宋体,serif;
    top: 40%;
    left: 25%;
  }
</style>