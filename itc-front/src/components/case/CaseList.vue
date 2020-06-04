<template>
  <div>
    <ul class="fly-case-list">
      <case-item @editCase="editCase" @refreshCase="getCase" :modify="!loadAll" :is-zan="cids.includes(item.id)" :item="item" v-for="item in cases" :key="item.id"></case-item>
    </ul>
    <blockquote class="layui-elem-quote layui-quote-nm" v-if="cases.length === 0">暂无数据</blockquote>

    <div style="text-align: center;">
      <el-pagination
        background
        hide-on-single-page
        layout="prev, pager, next"
        @current-change="getCase"
        :current-page.sync="query.currentPage"
        :page-size.sync="query.pageSize"
        :total="total">
      </el-pagination>
    </div>
  </div>
</template>

<script>
  import CaseItem from "./CaseItem"

  export default {
    name: "CaseList",
    props: {
      yearSpace: {
        type: Number,
        default: 0
      },
      orderByTime: {
        type: Boolean,
        default: true
      },
      loadAll: {
        type: Boolean,
        default: true
      }
    },
    data() {
      return {
        total: 0,
        query: {
          currentPage: 1,
          pageSize: 8,
          yearSpace: this.yearSpace,
          orderByTime: this.orderByTime,
          uId:null,
          state:1
        },
        cases: [],
        cids: []
      }
    },
    created() {
      this.getCase()
      this.getCaseIds()
    },
    methods: {
      editCase(item) {
        this.$emit('editCase',item)
      },
      async getCase() {
        if (!this.loadAll) {
          this.query.uId = this.$store.getters.getUserId
        }else {
          this.query.uId = null
        }
        this.query.yearSpace = this.yearSpace
        const res = await this.$http.get("case", {params: this.query})
        if (res.code === 200) {
          this.cases = res.data.data || []
          this.total = res.data.total
        } else {
          this.$message.error("加载失败")
        }
      },
      /**
       * 获取当前用户点赞的案例id
       * @returns {Promise<void>}
       */
      async getCaseIds() {
        let uid = this.$store.getters.getUserId
        console.log(uid)
        if (uid == null) return
        const res = await this.$http.get(`caseZan/${this.yearSpace}/caseId/${uid}`)
        if (res.code === 200) {
          this.cids = res.data || []

        }
      }
    },
    watch: {
      yearSpace(pre, next) {
        this.getCase()
        this.getCaseIds()
      },
      loadAll(pre, next) {
        console.log(this.loadAll)
        if (this.loadAll) {
          this.query.state = 1
        }else{
          this.query.state = null
        }
        this.getCase()
        this.getCaseIds()
      }
    },
    components: {
      CaseItem
    }
  }
</script>

<style scoped>

</style>