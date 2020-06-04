<template>
  <el-tabs v-model="activeName" @tab-click="handleClick" style="height: 100%">
    <el-tab-pane label="首页" name="home">
      <home/>
    </el-tab-pane>
    <el-tab-pane v-for="cate in categories" :key="cate.cId" :lazy="true" :label="cate.name" :name="cate.pinyin">
      <cate-article :category="cate.cId"/>
    </el-tab-pane>
  </el-tabs>
</template>

<script>
  import Home from "../home/Home";
  import CateArticle from "./CateArticle"
  export default {
    name: "Main",
    data() {
      return {
        activeName: 'home',
        categories:[]
      }
    },
    created() {
      this.getCategories()
    },
    methods: {
      handleClick(tab, event) {

      },
      async getCategories() {
        const res = await this.$http.get('category/front')
        if (res.code === 200) {
          this.categories = res.data || []
        }
      }
    },
    components: {
      Home, CateArticle
    }
  }
</script>

<style scoped>
  .el-tabs {
    background-color: #fff;
    padding: 0 30px;
    height: 100%;
  }
</style>