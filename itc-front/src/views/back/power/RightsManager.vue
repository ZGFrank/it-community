<template>
  <div>
    <!-- 面包屑导航区域 -->
    <el-breadcrumb separator-class="el-icon-arrow-right" style="margin-bottom: 10px">
      <el-breadcrumb-item :to="{ path: '/back' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>{{$store.state.breadcrumb.fName}}</el-breadcrumb-item>
      <el-breadcrumb-item>{{$store.state.breadcrumb.cName}}</el-breadcrumb-item>
    </el-breadcrumb>

    <!-- 卡片视图 -->
    <el-card>
      <el-table :data="rightsList" border stripe>
        <el-table-column type="index"></el-table-column>
        <el-table-column label="权限名称" prop="authName"></el-table-column>
        <el-table-column label="路径" prop="path"></el-table-column>
        <el-table-column label="权限等级" prop="level">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.level === 0">一级</el-tag>
            <el-tag type="success" v-else-if="scope.row.level === 1">二级</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="是否可用">
          <template slot-scope="scope">
            <el-switch
              v-if="scope.row.level !== 0 && scope.row.id !== 14"
              @change="updateRights(scope.row)"
              v-model="scope.row.available"
              active-color="#13ce66"
              inactive-color="#ff4949"
              :active-value="1"
              :inactive-value="0">
            </el-switch>
          </template>
        </el-table-column>
        <el-table-column label="图标">
          <template slot-scope="scope">
            <i class="menu-icon-show" :class="scope.row.icon"></i>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script>
  export default {
    name: 'rightsManager',
    data() {
      return {
        // 权限列表
        rightsList: []
      }
    },
    created() {
      // 获取所有的权限
      this.getRightsList()
    },
    methods: {
      // 获取权限列表
      async getRightsList() {
        const res = await this.$http.get('rights/list')
        if (res.code !== 200) {
          return this.$message.error('获取权限列表失败！')
        }

        this.rightsList = res.data
        console.log(this.rightsList)
      },
      async updateRights(row){
        const res = await this.$http.put('rights', row)
        if (res.code !== 200) {
          this.getRightsList()
          this.$message.error('修改失败')
        }
      }
    }
  }
</script>

<style lang="less" scoped>
  .menu-icon-show{
    font-size: 18px;
  }
</style>
