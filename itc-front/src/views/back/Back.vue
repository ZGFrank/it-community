<template>
  <el-container class="home-container">
    <!-- 顶部 -->
    <el-header>
      <div class="nav-header">
        <div class="nav-header-left">
          <img src="~assets/logo.png">
        </div>
        <div>
          <user-bar/>
        </div>
      </div>
    </el-header>
    <el-container>
      <!-- 左侧菜单栏 -->
      <el-aside :width="isCollapse ? '64px' : '200px'">
        <!-- 收缩按钮 -->
        <div class="toggle-button" @click="toggleCollapse">|||</div>
        <!-- 导航列表 -->
        <el-menu
          background-color="#333744"
          text-color="#fff"
          active-text-color="#409EFF"
          :default-active="$route.path"
          unique-opened
          router
          :collapse="isCollapse"
          :collapse-transition="false">
          <el-menu-item index="/back/desktop">
            <i class="el-icon-menu"></i>
            <span slot="title">系统桌面</span>
          </el-menu-item>

          <el-submenu :index="(index+1).toString()" v-for="(menu,index) in menus" :key="menu.authName">
            <template slot="title">
              <i :class="menu.icon"></i>
              <span>{{menu.authName}}</span>
            </template>
            <el-menu-item @click="setBreadcrumbName(menu.authName,item.authName)" v-if="item.available === 1" :index="item.path" v-for="item in menu.children" :key="item.authName">
              <i :class="item.icon"></i>
              <span slot="title">{{item.authName}}</span>
            </el-menu-item>
          </el-submenu>
        </el-menu>
      </el-aside>
      <!-- 主体 -->
      <el-main>
        <!-- 路由占位符 -->
        <router-view></router-view>
      </el-main>
    </el-container>
  </el-container>
</template>

<script>
  import UserBar from "../../components/common/UserBar"
  export default {
    name: 'Back',
    data() {
      return {
        isCollapse: false,
        menus:[]
      }
    },
    created() {
      this.getMenu()
    },
    methods: {
      async getMenu() {
        const roleId = this.$store.state.userInfo.roleId
        const res = await this.$http.get('rights/tree',{params:{roleId}})
        if (res.code === 200) {
          this.menus = res.data || []
        }else {
          this.$message("验证出错")
          return this.$router.replace("/main")
        }
      },
      // 点击按钮，切换菜单的折叠与展开
      toggleCollapse() {
        this.isCollapse = !this.isCollapse
      },
      setBreadcrumbName(fName, cName) {
        let param = {fName,cName}
        this.$store.commit('setBreadcrumb',param)
      }
    },components:{
      UserBar
    }
  }
</script>

<style lang="less" scoped>
  .home-container {
    height: 100%;
  }
  .el-header {
    background-color: #373d41;
  }
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
  .el-aside {
    background-color: #333744;

    .el-menu {
      border-right: none;
    }
  }
  .el-main {
    background-color: #eaedf1;
  }

  .toggle-button {
    background-color: #4a5064;
    font-size: 10px;
    line-height: 24px;
    color: #fff;
    text-align: center;
    letter-spacing: 0.2em;
    cursor: pointer;
  }
</style>
