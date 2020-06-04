<template>
  <div class="interchange-body">
    <el-row :gutter="20">
      <el-col :span="16" id="boxFlag">
        <router-view :key="new Date().getTime()"></router-view>
      </el-col>
      <el-col :span="8" id="boxFixed">
        <i-tool-bar/>
        <news/>
        <sign-in/>
        <zhou-bang/>
        <heat-debate  ref="heat"/>
      </el-col>
    </el-row>
  </div>
</template>

<script>
  import {IToolBar, News, SignIn, ZhouBang, HeatDebate} from '@/components/tools'

  export default {
    name: "Interchange",
    data() {
      return {
        isFixed: true,
        preValue: -10000
      }
    },
    mounted() {
      //window.addEventListener('scroll',this.handleScroll,true) // 监听滚动事件，然后用handleScroll这个方法进行相应的处理
    },
    methods: {
      handleScroll(e){
        console.log()
        let rec = document.querySelector('#boxFixed').getBoundingClientRect()
        let clientTop = document.documentElement.clientTop
        let heightc = document.getElementById('boxFlag').offsetHeight-document.getElementById('boxFixed').offsetHeight
        let value = heightc - (rec.bottom - clientTop)
        //下滑
        if (value > this.preValue) {
          //this.isFixed = value >= 100;  // 如果滚动到顶部了，this.isFixed就为true
        }

      }
    },
    components: {
      IToolBar, News, SignIn, ZhouBang, HeatDebate
    }
  }
</script>

<style scoped>
  .interchange-body {
    padding: 20px 70px;
  }
  .is_fixed{
    position: absolute;
    bottom: 100px;
  }
</style>