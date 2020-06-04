<template>
  <el-container>
    <el-header>
      <div class="nav-header">
        <div class="nav-header-left">
          <img src="~assets/logo.png">
          <el-menu
              default-active="/home"
              class="el-menu-demo"
              mode="horizontal"
              background-color="#393d49"
              text-color="#f2f2f2"
              router
              active-text-color="#ffffff">
            <el-menu-item index="/interchange"><i class="el-icon-chat-dot-round"></i>交流</el-menu-item>
            <el-menu-item index="/case"><i class="el-icon-cpu"></i>案例</el-menu-item>
            <el-menu-item index="/exchange"><i class="el-icon-s-goods"></i>需求</el-menu-item>
          </el-menu>
        </div>
        <div>
          <user-bar/>
        </div>
      </div>
    </el-header>
    <el-main>
      <router-view :key="pathKey"></router-view>
    </el-main>
    <write-bar path="/write/0" :right="30"/>
    <el-backtop target=".el-container .el-main" :visibility-height="20" :right="30" :bottom="80">
    </el-backtop>
  </el-container>
</template>

<script>

  import UserBar from "../components/common/UserBar"
  export default {
    name: "Index",
    computed:{
      pathKey(){
        return new Date().getTime()
      }
    },components:{
      UserBar
    }
  }
</script>

<style lang="less" scoped>
  @import "../assets/css/layui.css";
  @import "../assets/css/global.css";
  .el-container {
    height: 100%;
    position: absolute;
    width: 100%;
    top: 0;
  }

  .el-header {
    background-color: #393d49;


    .nav-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      height: 60px !important;
    }

    .nav-header-left {
      display: flex;
      flex-direction: row;
      align-items: center;

      img {
        margin-right: 50px;
      }

      .header-icon {
        line-height: 24px;
        font-size: 24px;
      }
    }


  }

  .el-main {
    background-color: #f2f2f2;
    padding: 0;
  }

</style>