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
          <el-button type="primary" @click="addNews" size="mini" icon="el-icon-edit"></el-button>
          <el-button type="danger" @click="deleteBatch" size="mini" icon="el-icon-delete"></el-button>
        </el-button-group>
      </div>
      <el-table
        :data="tableData"
        stripe
        ref="tableData"
        style="width: 100%">
        <el-table-column type="expand">
          <template slot-scope="props">
            <p>{{props.row.content}}</p>
          </template>
        </el-table-column>
        <el-table-column type="selection" width="55"></el-table-column>
        <el-table-column
          prop="title"
          align="center"
          label="标题">
        </el-table-column>
        <el-table-column
          align="center"
          label="申请时间">
          <template slot-scope="scope">
            <i class="el-icon-time"></i>
            <span style="margin-left: 10px">{{ scope.row.createTime }}</span>
          </template>
        </el-table-column>
        <el-table-column
          align="center"
          label="是否失效">
          <template slot-scope="scope">
            <el-button size="mini" type="success" round v-if="Date.parse(scope.row.expiryTime)>new Date().getTime()">
              有效
            </el-button>
            <el-button size="mini" type="danger" round v-else>失效</el-button>
          </template>
        </el-table-column>
        <el-table-column
          align="center"
          label="失效时间">
          <template slot-scope="scope">
            <i class="el-icon-time"></i>
            <span style="margin-left: 10px">{{ scope.row.expiryTime }}</span>
          </template>
        </el-table-column>
        <el-table-column
          align="center"
          label="紧急程度">
          <template slot-scope="scope">
            <el-tag type="danger" v-if="scope.row.level === 3">紧急</el-tag>
            <el-tag type="success" v-else-if="scope.row.level === 2">重要</el-tag>
            <el-tag v-else>普通</el-tag>
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
              @click="handleDelete(scope.row.newsId)">删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination
        background
        hide-on-single-page
        layout="prev, pager, next"
        @current-change="getNews"
        :current-page.sync="query.currentPage"
        :page-size.sync="query.pageSize"
        :total="total">
      </el-pagination>
    </el-card>

    <el-dialog title="添加通知" :visible.sync="newDialog" width="500px" :destroy-on-close="true">
      <el-form :model="form" ref="newsFrm">
        <el-form-item label="标题" label-width="80px" prop="title">
          <el-input v-model="form.title" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="内容" label-width="80px" prop="content">
          <el-input v-model="form.content" type="textarea" row="7" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="失效时间" label-width="80px" prop="expiryDate">
          <el-date-picker
            v-model="form.expiryDate"
            type="datetime"
            value-format="yyyy-MM-dd HH:mm"
            placeholder="选择日期时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="优先级" label-width="80px" prop="level">
          <el-select v-model="form.level" placeholder="请选择">
            <el-option label="普通" value="1"></el-option>
            <el-option label="重要" value="2"></el-option>
            <el-option label="紧急" value="3"></el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="newDialog = false">取 消</el-button>
        <el-button type="primary" @click="submitData">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  export default {
    name: "NewsManager",
    data() {
      return {
        tableData: [],
        query: {
          currentPage: 1,
          pageSize: 10,
        },
        total: 0,
        form: {
          title: '',
          content: '',
          expiryDate: '',
          level: ''
        },
        newDialog: false,
      }
    },
    created() {
      this.getNews()
    },
    methods: {
      addNews() {
        this.$delete(this.form,'expiryTime')
        this.newDialog = true
        this.$refs.newsFrm.resetFields()
      },
      deleteBatch() {
        let cIds = this.$refs.cateTable.selection.map(i => i.cId)
        if (cIds.length === 0) return this.$message.warning("您什么都没有选择")
        this.$confirm('此操作将永久删除该分类, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(async () => {
          const res = await this.$http.delete('news', {params: {ids: cIds.join(',')}})
          if (res.code === 200) {
            this.$message.success("删除成功")
            this.getNews()
          } else {
            this.$message.error("删除失败")
          }
        })
      },
      async submitData() {
        if (this.form.newsId == null) {
          this.form.sysuId = this.$store.getters.getUserId
          const res = await this.$http.post('news', this.form)
          if (res.code === 200) {
            this.getNews()
            this.$message.success("添加成功")
            this.newDialog = false
          } else {
            this.$message.error("添加失败")
          }
        } else {
          this.$delete(this.form,'expiryTime')
          const res = await this.$http.put('news', this.form)
          if (res.code === 200) {
            this.getNews()
            this.$message.success("修改成功")
            this.newDialog = false
          } else {
            this.$message.error("修改失败")
          }
        }
      },
      handleEdit(row) {
        this.form = row
        this.form.expiryDate = row.expiryTime
        this.$delete(this.form,'expiryTime')
        this.newDialog = true
      },
      handleDelete(newsId) {
        this.$confirm('确定删除该通知吗?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(async () => {
          const res = await this.$http.delete('news/' + newsId)
          if (res.code === 200) {
            this.$message.success("删除成功")
            this.getNews()
          } else {
            this.$message.error("删除失败")
          }
        })
      },
      async updateCate(item) {
        const res = await this.$http.put('category', item)
        if (res.code !== 200) {
          this.getNews()
          this.$message.error('修改失败')
        }

      },
      handleClick(row) {
      },
      async getNews() {
        const res = await this.$http.get('news/back', {params: this.query})
        if (res.code === 200) {
          this.tableData = res.data.data || []
          this.total = res.data.total || []
        }
      }
    }
  }
</script>

<style scoped>

</style>