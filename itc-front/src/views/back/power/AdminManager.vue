<template>
  <div>
    <!-- 面包屑导航区域 -->
    <el-breadcrumb separator-class="el-icon-arrow-right" style="margin-bottom: 10px">
      <el-breadcrumb-item :to="{ path: '/back' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>{{$store.state.breadcrumb.fName}}</el-breadcrumb-item>
      <el-breadcrumb-item>{{$store.state.breadcrumb.cName}}</el-breadcrumb-item>
    </el-breadcrumb>

    <!-- 卡片视图区域 -->
    <el-card>
      <div slot="header" class="clearfix">
        <el-button size="mini" type="primary" @click="addAdmin">添加管理员</el-button>
      </div>
      <!-- 用户列表区域 -->
      <el-table :data="userlist" border stripe>
        <el-table-column type="index"></el-table-column>
        <el-table-column label="姓名" prop="name"></el-table-column>
        <el-table-column label="邮箱" prop="email"></el-table-column>
        <el-table-column label="电话" prop="phone"></el-table-column>
        <el-table-column label="角色" prop="roleName"></el-table-column>
        <el-table-column label="是否可用">
          <template slot-scope="scope">
            <el-switch
              v-model="scope.row.available"
              @change="userStateChanged(scope.row)"
              active-color="#13ce66"
              inactive-color="#ff4949"
              :active-value="1"
              :inactive-value="0">
            </el-switch>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="180px">
          <template slot-scope="scope">
            <!-- 删除按钮 -->
            <el-button type="danger" icon="el-icon-delete" size="mini" @click="removeUserById(scope.row.id)"></el-button>
            <!-- 分配角色按钮 -->
            <el-tooltip effect="dark" content="分配角色" placement="top" :enterable="false">
              <el-button type="warning" icon="el-icon-setting" size="mini" @click="setRole(scope.row)"></el-button>
            </el-tooltip>
          </template>
        </el-table-column>
      </el-table>
    </el-card>


    <!-- 分配角色的对话框 -->
    <el-dialog title="分配角色" :visible.sync="setRoleDialogVisible" width="50%" @close="setRoleDialogClosed">
      <div>
        <el-input v-if="addOrSet" v-model="account" @keyup.enter.native="searchUser" size="mini"></el-input>
        <p style="margin: 20px">当前的用户：{{userInfo.name}}</p>
        <p  v-if="!addOrSet">当前的角色：{{userInfo.roleName}}</p>
        <p>分配新角色：
          <el-select v-model="selectedRoleId" placeholder="请选择">
            <el-option v-for="item in rolesList" :key="item.id" :label="item.roleName" :value="item.id">
            </el-option>
          </el-select>
        </p>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="setRoleDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="saveRoleInfo">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
  export default {
    name: 'adminManager',
    data() {
      return {
        userlist: [],
        total: 0,
        // 控制分配角色对话框的显示与隐藏
        setRoleDialogVisible: false,
        // 需要被分配角色的用户信息
        userInfo: {},
        account: '',
        addOrSet:true,
        // 所有角色的数据列表
        rolesList: [],
        // 已选中的角色Id值
        selectedRoleId: ''
      }
    },
    created() {
      this.getUserList()
    },
    methods: {
      async addAdmin() {
        // 在展示对话框之前，获取所有角色的列表
        const res = await this.$http.get('role')
        if (res.code !== 200) {
          return this.$message.error('获取角色列表失败！')
        }

        this.rolesList = res.data
        this.addOrSet = true
        this.setRoleDialogVisible = true
        this.account = ''
      },
      async searchUser() {
        console.log('ss')
        const res = await this.$http.get('user/account/' + this.account)
        if (res.code !== 200) {
          return this.$message.error('查无此人！')
        }
        this.userInfo = res.data
      },
      async getUserList() {
        const res = await this.$http.get('user/admin', {
          params: this.queryInfo
        })
        if (res.code !== 200) {
          return this.$message.error('获取用户列表失败！')
        }
        this.userlist = res.data
      },
      // 监听 switch 开关状态的改变
      async userStateChanged(userinfo) {
        console.log(userinfo)
      },
      // 根据Id删除对应的用户信息
      async removeUserById(id) {
        // 弹框询问用户是否删除数据
        const confirmResult = await this.$confirm(
          '此操作将永久删除该用户, 是否继续?',
          '提示',
          {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }
        ).catch(err => err)

        // 如果用户确认删除，则返回值为字符串 confirm
        // 如果用户取消了删除，则返回值为字符串 cancel
        // console.log(confirmResult)
        if (confirmResult !== 'confirm') {
          return this.$message.info('已取消删除')
        }

        const { data: res } = await this.$http.delete('users/' + id)

        if (res.meta.status !== 200) {
          return this.$message.error('删除用户失败！')
        }

        this.$message.success('删除用户成功！')
        this.getUserList()
      },
      // 展示分配角色的对话框
      async setRole(userInfo) {
        this.userInfo = userInfo

        // 在展示对话框之前，获取所有角色的列表
        const res = await this.$http.get('role')
        if (res.code !== 200) {
          return this.$message.error('获取角色列表失败！')
        }

        this.rolesList = res.data

        this.setRoleDialogVisible = true
        this.addOrSet = false
      },
      // 点击按钮，分配角色
      async saveRoleInfo() {
        if (!this.selectedRoleId) {
          return this.$message.error('请选择要分配的角色！')
        }

        const res = await this.$http.put(`user/${this.userInfo.uId}/role/${this.selectedRoleId}`)

        if (res.code !== 200) {
          return this.$message.error('更新角色失败！')
        }

        this.$message.success('更新角色成功！')
        this.getUserList()
        this.setRoleDialogVisible = false
      },
      // 监听分配角色对话框的关闭事件
      setRoleDialogClosed() {
        this.selectedRoleId = ''
        this.userInfo = {}
      }
    }
  }
</script>

<style lang="less" scoped>
</style>
