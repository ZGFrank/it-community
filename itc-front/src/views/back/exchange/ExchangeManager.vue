<template>
  <div>
    <!-- 面包屑导航区域 -->
    <el-breadcrumb separator-class="el-icon-arrow-right" style="margin-bottom: 10px">
      <el-breadcrumb-item :to="{ path: '/back' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>{{$store.state.breadcrumb.fName}}</el-breadcrumb-item>
      <el-breadcrumb-item>{{$store.state.breadcrumb.cName}}</el-breadcrumb-item>
    </el-breadcrumb>
    <el-card class="box-card">
      <el-table
        :data="tableData"
        stripe
        style="width: 100%">
        <el-table-column type="expand">
          <template slot-scope="props">
            <div class="back-case-show">
              {{props.row.claim}}
            </div>
          </template>
        </el-table-column>
        <el-table-column
          align="center"
          label="申请人">
          <template slot-scope="scope">
            <router-link :to="`/userPage/${scope.row.uId}`">{{scope.row.name}}</router-link>
          </template>
        </el-table-column>
        <el-table-column
          prop="title"
          align="center"
          label="需求名称">
        </el-table-column>
        <el-table-column
          prop="qq"
          align="center"
          label="QQ">
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
          fixed="right"
          width="150"
          label="操作"
          align="center">
          <template slot-scope="scope">

            <el-button
              size="mini"
              type="danger"
              @click="handleReject(scope.row)">下架
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination
        background
        hide-on-single-page
        layout="prev, pager, next"
        @current-change="getPendingExchange"
        :current-page.sync="query.currentPage"
        :page-size.sync="query.pageSize"
        :total="total">
      </el-pagination>
    </el-card>
  </div>
</template>

<script>
  export default {
    name: "ExchangeManager",
    data(){
      return{
        tableData: [],
        query:{
          currentPage: 1,
          pageSize: 10,
          state:1
        },
        total:0
      }
    },
    created() {
      this.getPendingExchange()
    },
    methods:{
      handleReject(item){
        item.state = -2
        this.updateExchange(item,'下架')
      },
      async updateExchange(item,type){
        this.$confirm('您确定要'+type+', 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(async () => {
          item.msg = type
          const res = await this.$http.put('exchange/back',item)
          if (res.code === 200) {
            this.$message.success(`${type}成功`)
            this.getPendingExchange()
          } else {
            this.$message.error(`${type}失败`)
          }
        })
      },
      async getPendingExchange() {
        const res = await this.$http.get('exchange',{params: this.query})
        if (res.code === 200) {
          this.tableData = res.data.data || []
          this.total = res.data.total || 0
        }else{
          this.$message.error("加载失败，请刷新重试")
        }
      }
    }
  }
</script>

<style scoped>

</style>