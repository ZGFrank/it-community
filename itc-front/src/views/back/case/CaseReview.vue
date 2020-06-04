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
        ref="cateTable"
        style="width: 100%">
        <el-table-column type="expand">
          <template slot-scope="props">
            <div class="back-case-show">
              <div class="back-case-intro">
                <el-tag size="medium">项目介绍</el-tag>
                <div><p>{{props.row.introduce}}</p></div>
              </div>
              <div class="back-case-pic">
                <el-tag size="medium">展示图片</el-tag>
                <div><img :src="$fileURL+props.row.caseImg"></div>
              </div>
            </div>
          </template>
        </el-table-column>
        <el-table-column
          prop="title"
          align="center"
          label="项目名称">
        </el-table-column>
        <el-table-column
          align="url"
          label="项目地址">
          <template slot-scope="scope">
            <a :href="scope.row.url">{{scope.row.url}}</a>
          </template>
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
          fixed="right"
          width="150"
          label="操作"
          align="center">
          <template slot-scope="scope">
            <el-button
              size="mini"
              @click="handleAgree(scope.row)">同意
            </el-button>
            <el-button
              size="mini"
              type="danger"
              @click="handleReject(scope.row)">拒绝
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination
        background
        hide-on-single-page
        layout="prev, pager, next"
        @current-change="getPendingCases"
        :current-page.sync="query.currentPage"
        :page-size.sync="query.pageSize"
        :total="total">
      </el-pagination>
    </el-card>
  </div>
</template>

<script>
  export default {
    name: "CaseReview",
    data(){
      return{
        tableData: [],
        query:{
          currentPage: 1,
          pageSize: 10,
          state:0
        },
        total:0
      }
    },
    created() {
      this.getPendingCases()
    },
    methods:{
      handleAgree(item){
        item.state = 1
        this.updateCase(item,'同意')
      },
      handleReject(item){
        item.state = -1
        this.updateCase(item,'拒绝')
      },
      async updateCase(item,type){
        this.$confirm('您确定要'+type+', 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(async () => {
          item.createTime = null
          item.msg = type
          const res = await this.$http.put('case',item)
          if (res.code === 200) {
            this.$message.success(`${type}成功`)
            this.getPendingCases()
          } else {
            this.$message.error(`${type}失败`)
          }
        })
      },
      async getPendingCases() {
        const res = await this.$http.get('case',{params: this.query})
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

<style lang="less" scoped>
.back-case-show{
  display: flex;
  flex-direction: row;

  .back-case-intro{
    display: flex;
    flex-direction: row;

    div{
      margin-left: 5px;
      width: 300px;
    }
  }

  .back-case-pic{
    display: flex;
    flex-direction: row;
    margin-left: 20px;

    img{
      margin-left: 5px;
      width: 240px;
      height: 150px;
      display: block;
    }
  }
}
</style>