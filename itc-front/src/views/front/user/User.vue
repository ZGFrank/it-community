<template>
  <div class="user-center">
    <el-row :gutter="20">
      <el-col :span="4" class="user-center-left">
        <el-menu
            :default-active="$route.path"
            background-color="#333744"
            text-color="#fff"
            router
            active-text-color="#409EFF">

          <el-menu-item :index="`/userPage/${$store.getters.getUserId}`">
            <i class="el-icon-user-solid"></i>
            <span slot="title">我的主页</span>
          </el-menu-item>
          <el-menu-item index="/userCenter">
            <i class="el-icon-s-promotion"></i>
            <span slot="title">用户中心</span>
          </el-menu-item>
          <el-menu-item index="/userSetting">
            <i class="el-icon-setting"></i>
            <span slot="title">基础设置</span>
          </el-menu-item>
          <el-menu-item index="/userMessage">
            <i class="el-icon-message"></i>
            <span slot="title">我的消息</span>
          </el-menu-item>
        </el-menu>
      </el-col>
      <el-col :span="20" class="user-center-right">
        <el-card class="user-center-pane">
          <router-view></router-view>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
  export default {
    name: "User"
  }
</script>

<style lang="less" scoped>

  .user-center{
    padding: 20px 70px !important;
    height: 92%;

    .el-row{
      height: 100%;

      .user-center-left{
        height: 100%;
        .el-menu{
          height: 100%;
        }


      }
      .user-center-right{
        padding-right: 70px;
        height: 100%;
        .user-center-pane{
          height: 100%;
        }
      }

    }
  }
</style>