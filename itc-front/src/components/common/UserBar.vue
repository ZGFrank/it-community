<template>
  <div class="nav-header-right">
    <el-dropdown @command="toPage" v-if="Object.keys(userInfo).length">
              <span class="el-dropdown-link">
                <div>
                  <cite class="layui-hide-xs nav-header-name">{{userInfo.name}}</cite>
                  <cite class="layui-hide-xs nav-header-name" style="margin-left: 5px" v-if="userInfo.roleId === 0">(超级管理员)</cite>
                  <cite class="layui-hide-xs nav-header-name" style="margin-left: 5px"
                        v-if="userInfo.roleId > 0">(管理员)</cite>
                <i class="layui-badge fly-badge-vip layui-hide-xs" v-if="userInfo.vip > 0">VIP{{userInfo.vip}}</i>
                <el-avatar :size="50" :src="$fileURL+userInfo.avatar"></el-avatar>
                </div>

              </span>
      <el-dropdown-menu slot="dropdown">
        <el-dropdown-item command="/userSetting" icon="el-icon-s-tools">基本设置</el-dropdown-item>
        <el-dropdown-item :command="`/userPage/${$store.getters.getUserId}`" icon="el-icon-user-solid">我的主页
        </el-dropdown-item>
        <el-dropdown-item command="/userMessage" icon="el-icon-message">
          <el-badge :is-dot="$store.state.msgCount > 0" class="item">
            我的消息
          </el-badge>
        </el-dropdown-item>
        <el-dropdown-item command="/" icon="el-icon-s-home" v-if="$route.path.includes('back')">首页</el-dropdown-item>
        <el-dropdown-item command="/back" icon="el-icon-s-platform" v-show="$store.state.userInfo.roleId != null" v-else>后台管理</el-dropdown-item>
        <el-dropdown-item command="/logout" icon="el-icon-check" divided>退出</el-dropdown-item>
      </el-dropdown-menu>
    </el-dropdown>
    <div v-else>
      <el-avatar :size="50" src="../assets/images/avatar/avatar.jpg"></el-avatar>
      <el-button type="text" id="toLogin" @click="toLogin">登录</el-button>
    </div>
  </div>
</template>

<script>
  import {mapState} from 'vuex'

  export default {
    name: "UserBar",
    data() {
      return {}
    },
    created() {
      this.getMessageCount()
    },
    methods: {
      toLogin() {
        this.$router.push('/login')
      },
      toPage(command) {
        if (command === '/logout') {
          this.$store.commit('clearUser')
          command = '/index'
        }
        this.$router.push(command)
      },
      async getMessageCount() {
        if (this.userInfo.uId != null) {
          const res = await this.$http.get('message/count/' + this.userInfo.uId)
          if (res.code === 200) {
            this.$store.commit('setMsgCount', res.data)
          }
        }
      }
    },
    computed: {
      ...mapState(['userInfo'])
    }
  }
</script>

<style lang="less" scoped>
  .nav-header-right {
    display: flex;
    align-items: center;

    div {
      display: flex;
      align-items: center;
    }

    .fly-badge-vip {
      margin-left: 20px;
    }

    .el-avatar {
      margin-left: 20px;
    }
  }

  #toLogin {
    margin-left: 20px;
  }

  .nav-header-name {
    font-size: 14px;
    color: #ffffff;
    font-style: normal !important;
  }
</style>