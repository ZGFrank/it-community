<template>
  <div class="itool-bar">
    <el-input
      v-model="title"
      @keyup.enter.native="doSearch"
      class="keep-dis">
      <i slot="prefix" class="el-input__icon el-icon-search"></i>
    </el-input>
    <el-button type="primary" @click="$router.push('/write/0')">发布文章</el-button>
  </div>
</template>

<script>
  export default {
    name: "IToolBar",
    data() {
      return {
        title: ''
      };
    },
    methods: {
      doSearch() {
        if (this.title.trim()) {
          this.$router.push({
            path: '/search',
            query: {
              title: this.title
            }
          })
        } else {
          this.$message.warning("您没有输入任何查询内容")
        }
      }
    }
  }
</script>

<style lang="less" scoped>
  .itool-bar {
    display: flex;
    justify-content: space-between;
    background-color: #fff;
    padding: 10px 10px 5px;
  }

  .keep-dis {
    margin-right: 3px;
  }
</style>