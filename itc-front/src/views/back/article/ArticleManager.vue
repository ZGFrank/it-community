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
        <el-form ref="form" :inline="true" :model="query" label-width="80px" size="mini">
          <el-form-item label="文章名称">
            <el-input v-model="query.title"></el-input>
          </el-form-item>
          <el-form-item label="类型">
            <el-select v-model="query.category" placeholder="请选择">
              <el-option
                v-for="cate in categories"
                :key="cate.cId"
                :label="cate.name"
                :value="cate.cId">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="排序方式">
            <el-radio-group v-model="query.sort">
              <el-radio :label="0">无</el-radio>
              <el-radio :label="1">点赞</el-radio>
              <el-radio :label="2">评论</el-radio>
              <el-radio :label="3">阅览</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="完结">
            <el-radio-group v-model="query.isOver">
              <el-radio label="">无</el-radio>
              <el-radio :label="0">未完</el-radio>
              <el-radio :label="1">已完</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" icon="el-icon-search" @click="getArticles" circle></el-button>
          </el-form-item>
        </el-form>
      </div>
      <el-table
        :data="tableData"
        stripe
        @row-dblclick="seeDetail"
        ref="articleTable"
        style="width: 100%">
        <el-table-column
          prop="title"
          width="400px"
          label="标题">
        </el-table-column>
        <el-table-column
          align="center"
          label="类别">
          <template slot-scope="scope">
            <span>{{getCateName(scope.row.cId)}}</span>
          </template>
        </el-table-column>
        <el-table-column
          align="center"
          label="悬赏">
          <template slot-scope="scope">
            <span>{{scope.row.learnCoin}}</span>
          </template>
        </el-table-column>
        <el-table-column
          align="center"
          label="是否完结">
          <template slot-scope="scope">
            <el-button v-if="scope.row.isOver === 1" type="success" plain size="mini">已完</el-button>
            <el-button v-else type="info" plain  size="mini">未完</el-button>
          </template>
        </el-table-column>
        <el-table-column
          align="center"
          label="点赞">
          <template slot-scope="scope">
            <span>{{scope.row.hitsZan}}/次</span>
          </template>
        </el-table-column>
        <el-table-column
          align="center"
          label="评论">
          <template slot-scope="scope">
            <span>{{scope.row.hitsComment}}/次</span>
          </template>
        </el-table-column>
        <el-table-column
          align="center"
          label="阅览">
          <template slot-scope="scope">
            <span>{{scope.row.watch}}/次</span>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination
        background
        hide-on-single-page
        layout="prev, pager, next"
        @current-change="getArticles"
        :current-page.sync="query.currentPage"
        :page-size.sync="query.pageSize"
        :total="total">
      </el-pagination>
    </el-card>
  </div>
</template>

<script>
  export default {
    name: "ArticleManager",
    data(){
      return{
        tableData: [],
        categories: [],
        query: {
          currentPage: 1,
          pageSize: 10,
          title: '',
          category: '',
          isOver: '',
          sort:0
        },
        total:0
      }
    },
    created() {
      this.getArticles()
      this.getCategories()
    },
    methods:{
      seeDetail(row, column, event) {
        this.$router.push('/articleDetail/' + row.artId)
      },
      async getArticles() {
        const res = await this.$http.get('article/back',{params: this.query})
        if (res.code === 200) {
          this.tableData = res.data.data || []
          this.total = res.data.total || 0
        }
      },
      async getCategories() {
        const res = await this.$http.get('category')
        if (res.code === 200) {
          this.categories = res.data || []
        }
      },
      getCateName(cid) {
        for (let cate of this.categories) {
          if (cid === cate.cId) {
            return cate.name
          }
        }
      }
    }
  }
</script>

<style scoped>

</style>