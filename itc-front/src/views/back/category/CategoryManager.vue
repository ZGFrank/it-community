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
      <el-button-group>
        <el-button type="primary" @click="addCate" size="mini" icon="el-icon-edit"></el-button>
        <el-button type="danger" @click="deleteBatch" size="mini" icon="el-icon-delete"></el-button>
      </el-button-group>
    </div>
    <el-table
      :data="tableData"
      stripe
      ref="cateTable"
      style="width: 100%">
      <el-table-column
        type="selection"
        width="55">
      </el-table-column>
      <el-table-column
        prop="name"
        align="center"
        label="标题">
      </el-table-column>
      <el-table-column
        align="center"
        label="普通用户可用">
        <template slot-scope="scope">
          <el-switch
            @change="updateCate(scope.row)"
            v-model="scope.row.state"
            active-color="#13ce66"
            inactive-color="#ff4949"
            :active-value="1"
            :inactive-value="0">
          </el-switch>
        </template>
      </el-table-column>
      <el-table-column
        align="center"
        label="是否可用">
        <template slot-scope="scope">
          <el-switch
            @change="updateCate(scope.row)"
            v-model="scope.row.available"
            active-color="#13ce66"
            inactive-color="#ff4949"
            :active-value="1"
            :inactive-value="0">
          </el-switch>
        </template>
      </el-table-column>
      <el-table-column
        fixed="right"
        width="150"
        label="操作"
        align="center">
        <template slot-scope="scope">
          <el-button
            size="mini"
            @click="handleEdit(scope.row)">编辑
          </el-button>
          <el-button
            size="mini"
            type="danger"
            @click="handleDelete(scope.row.cId)">删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>
  </el-card>

  <el-dialog title="文章分类" :visible.sync="cateDialog" width="400px">
    <el-form :model="form" ref="form" :rules="rules" :inline="true">
      <el-form-item label="分类名称" label-width="80px" prop="name">
        <el-input v-model="form.name" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item label="普通用户可用" label-width="110px">
        <el-switch
          v-model="form.state"
          active-color="#13ce66"
          inactive-color="#ff4949"
          :active-value="1"
          :inactive-value="0">
        </el-switch>
      </el-form-item>
      <el-form-item label="可用" label-width="80px">
        <el-switch
          v-model="form.available"
          active-color="#13ce66"
          inactive-color="#ff4949"
          :active-value="1"
          :inactive-value="0">
        </el-switch>
      </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button @click="cateDialog = false">取 消</el-button>
      <el-button type="primary" @click="submitData">确 定</el-button>
    </div>
  </el-dialog>
</div>
</template>

<script>
  export default {
    name: "CategoryManager",
    data() {
      return {
        tableData: [],
        form:{
          cId: null,
          name: '',
          available: 1,
          state:1
        },
        cateDialog:false,
        rules:{
          name:[
            { required: true, message: '请输入分类名称', trigger: 'blur' },
            { min: 1, max: 4, message: '长度在 1 到 4 个字符', trigger: 'blur' }
          ]
        }
      }
    },
    created() {
      this.getCategories()
    },
    methods: {
      addCate() {
        this.cateDialog = true
        this.$refs.form.resetFields()
      },
      deleteBatch() {
        let cids = this.$refs.cateTable.selection.map(i=>i.cId)
        if (cids.length === 0) return this.$message.warning("您什么都没有选择")
        this.$confirm('此操作将永久删除该分类, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(async () => {
          let ids = ''
          for (let id of cids) {
            console.log(id)
            ids += `ids=${id}&`
          }
          ids = ids.substring(0,ids.length-1)
          const res = await this.$http.delete('category?'+ids)
          if (res.code === 200) {
            this.$message.success("删除成功")
            this.getCategories()
          } else {
            this.$message.error("删除失败")
          }
        })
      },
      submitData() {
        this.$refs.form.validate(async valid => {
          if (!valid) return
          if (this.form.cId == null) {
            const res = await this.$http.post('category', this.form)
            if (res.code === 200) {
              this.getCategories()
              this.$message.success("添加成功")
            } else {
              this.$message.error("添加失败")
            }
          } else {
            const res = await this.$http.put('category', this.form)
            if (res.code === 200) {
              this.getCategories()
              this.$message.success("修改成功")
            } else {
              this.$message.error("修改失败")
            }
          }
          this.$refs.form.resetFields()
        })
      },
      handleEdit(row) {
        this.form = row
        this.cateDialog = true
      },
      handleDelete(cId) {
        this.$confirm('此操作将永久删除该分类, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(async () => {
          const res = await this.$http.delete('category/' + cId)
          if (res.code === 200) {
            this.$message.success("删除成功")
            this.getCategories()
          } else {
            this.$message.error("删除失败")
          }
        })
      },
      async updateCate(item) {
        const res = await this.$http.put('category', item)
        if (res.code !== 200) {
          this.getCategories()
          this.$message.error('修改失败')
        }

      },
      handleClick(row) {
        console.log(row);
      },
      async getCategories() {
        const res = await this.$http.get('category')
        if (res.code === 200) {
          this.tableData = res.data || []
        }
      }
    }
  }
</script>

<style scoped>

</style>