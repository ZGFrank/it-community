<template>
  <div>
    <!-- 面包屑导航区域 -->
    <el-breadcrumb separator-class="el-icon-arrow-right" style="margin-bottom: 10px">
      <el-breadcrumb-item :to="{ path: '/back' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>{{$store.state.breadcrumb.fName}}</el-breadcrumb-item>
      <el-breadcrumb-item>{{$store.state.breadcrumb.cName}}</el-breadcrumb-item>
    </el-breadcrumb>
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <el-row>
          <el-form ref="form" :inline="true" :model="query" label-width="60px" size="mini">
            <el-form-item label="用户名">
              <el-input v-model="query.name"></el-input>
            </el-form-item>
            <el-form-item label="昵称">
              <el-input v-model="query.nickname"></el-input>
            </el-form-item>
            <el-form-item label="班级">
              <el-input v-model="query.classGrade"></el-input>
            </el-form-item>
            <el-form-item label="性别" >
              <el-radio-group v-model="query.gender">
                <el-radio :label="null">无</el-radio>
                <el-radio :label="0">女</el-radio>
                <el-radio :label="1">男</el-radio>
              </el-radio-group>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" icon="el-icon-search" @click="getUsers" circle></el-button>
            </el-form-item>
          </el-form>
        </el-row>
        <el-row class="top-bar">
          <el-upload
            class="import-btn"
            :action="$baseURL+'/user/import'"
            :headers="{'Authorization':userToken}"
            accept="application/vnd.ms-excel,application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
            :on-success="importSuccess"
            :before-upload="importBefore"
            :show-file-list="false">
            <el-tooltip class="item" effect="dark" content="选择表头为姓名、学号、班级、性别、电话、邮箱的excel表格，姓名、学号必选" placement="bottom-start">
              <el-button type="info" size="mini"><i v-if="isLoad" class="el-icon-loading"></i>{{ !isLoad?'导入':''}}</el-button>
            </el-tooltip>
          </el-upload>
          <el-button type="primary" size="mini" @click="handleAddUser">添加</el-button>
          <el-button type="danger" size="mini" @click="handleBatchDel">删除</el-button>
        </el-row>
      </div>
      <el-table
        :data="tableData"
        stripe
        @row-dblclick="toUserPage"
        ref="tableData"
        style="width: 100%">
        <el-table-column type="selection" width="40"></el-table-column>
        <el-table-column prop="name" align="center" label="姓名"></el-table-column>
        <!--<el-table-column prop="nickname" align="center" label="昵称"> </el-table-column>-->
        <el-table-column prop="account" align="center" label="账号"></el-table-column>
        <el-table-column prop="phone" align="center" label="电话"></el-table-column>
        <el-table-column prop="email" align="center" width="200" label="邮箱"></el-table-column>
        <el-table-column prop="classGrade" align="center" label="班级"></el-table-column>
        <el-table-column prop="gender" align="center" label="性别" width="50">
          <template slot-scope="scope">
            <i class="el-icon-male" v-if="scope.row.gender === 1" style="color: #1E9FFF"></i>
            <i class="el-icon-female" v-else style="color: #FF00FF"></i>
          </template>
        </el-table-column>
        <el-table-column align="center" label="角色">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.roleId == null">普通用户</el-tag>
            <el-tag v-else type="success">管理员</el-tag>
          </template>
        </el-table-column>
        <el-table-column
          fixed="right"
          width="160"
          label="操作">
          <template slot-scope="scope">
            <el-button
              size="mini"
              type="warning"
              @click="handleResetPass(scope.row.uId)">
              重置密码
            </el-button>
            <el-button
              size="mini"
              type="danger"
              icon="el-icon-delete"
              v-if="scope.row.roleId == null"
              @click="handleDelete(scope.row.uId)">
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination
        background
        hide-on-single-page
        layout="prev, pager, next"
        @current-change="getUsers"
        :current-page.sync="query.currentPage"
        :page-size.sync="query.pageSize"
        :total="total">
      </el-pagination>
    </el-card>

    <el-dialog title="用户操作" :visible.sync="dialogFormVisible">
      <el-form :model="userFrm" ref="userFrm">
        <el-form-item label="用户名" label-width="100px">
          <el-input v-model="userFrm.name" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="用户账号" label-width="100px">
          <el-input v-model="userFrm.account" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="班级" label-width="100px">
          <el-input v-model="userFrm.classGrade" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="性别" label-width="100px">
          <el-radio-group v-model="userFrm.gender">
            <el-radio :label="1">男</el-radio>
            <el-radio :label="0">女</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="saveUser">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  export default {
    name: "UserManager",
    data() {
      return {
        dialogFormVisible: false,
        tableData: [],
        query: {
          currentPage: 1,
          pageSize: 10,
          name: '',
          nickname: '',
          classGrade: '',
          gender: null
        },
        userFrm: {
          name: '',
          account: '',
          classGrade: '',
          gender: null,
        },
        isLoad: false,
        total: 0
      }
    },
    created() {
      this.getUsers()
    },
    methods: {
      toUserPage(row, column, event) {
        this.$router.push('/userPage/' + row.uId)
      },
      async getUsers() {
        const res = await this.$http.get('user', {params: this.query})
        if (res.code === 200) {
          this.tableData = res.data.data || []
          this.total = res.data.total || 0
        }
      },
      importBefore() {
        this.isLoad = true
      },
      importSuccess(res) {
        this.isLoad = false
        if (res.code === 200) {
          this.getUsers()
          this.$message.success("导入成功")
        } else {
          this.$message.error("导入失败，请重试")
        }
      },
      async saveUser() {
        const res = await this.$http.post('user', this.userFrm)
        if (res.code === 200) {
          this.dialogFormVisible = false
          this.getUsers()
          return this.$message("操作成功")
        }
        this.$message.error("操作失败")
      },
      handleAddUser() {
        this.dialogFormVisible = true
        this.$refs.userFrm.resetField()
      },
      handleBatchDel() {
        let uIds = this.$refs.tableData.selection.map(i => i.uId)
        if (uIds.length === 0) return this.$message.warning("您未选择")
        this.$confirm('您确定要删除这些用户吗?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(async () => {
          const res = await this.$http.delete('user', {params: {ids: uIds.join(',')}})
          if (res.code === 200) {
            this.getUsers()
            return this.$message.success("操作成功")
          }
          this.$message.error("操作失败")
        })
      },
      handleDelete(uId) {
        this.$confirm('您确定要删除该用户吗?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(async () => {
          const res = await this.$http.delete(`user/${uId}`)
          if (res.code === 200) {
            this.getUsers()
            return this.$message.success("操作成功")
          }
          this.$message.error("操作失败")
        })
      },
      handleResetPass(uId) {
        this.$confirm('您确定要重置该用户密码吗?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(async () => {
          const res = await this.$http.update(`user/reset/${uId}`)
          if (res.code === 200) {
            return this.$message.success("操作成功")
          }
          this.$message.error("操作失败")
        })
      }
    },

    computed: {
      userToken() {
        return window.sessionStorage.getItem('token')
      }
    }
  }
</script>

<style scoped>
  .top-bar {
    display: flex;

  }

  .import-btn {
    margin-right: 10px;
  }
</style>