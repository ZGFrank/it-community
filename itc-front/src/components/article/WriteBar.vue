<template>
  <div v-if="$route.path !== path" class="write-bar" @click="toWritePage" :style="{
        'right': styleRight,
        'bottom': styleBottom
      }">
    <i class="el-icon-edit"></i>
  </div>
</template>

<script>
  export default {
    name: "WriteBar",
    props:{
      path:{
        type:String,
        default:'/write/0'
      },
      right:{
        type:Number,
        default: 40
      },
      bottom:{
        type:Number,
        default: 30
      }
    },
    methods:{
      toWritePage(){
        this.$router.push(this.path)
      }
    },
    computed: {
      styleBottom() {
        return `${this.bottom}px`;
      },
      styleRight() {
        return `${this.right}px`;
      }
    }
  }
</script>

<style scoped>
.write-bar{
  position: fixed;
  height: 40px;
  width: 40px;
  background-color: #ffffff;
  box-shadow: 0 0 6px rgba(0, 0, 0, 0.21);
  line-height: 40px;
  text-align: center;
  color: #1989fa;
  font-size: 25px;
  font-weight: bold;
}
  .write-bar:hover{
    background-color: rgba(215, 215, 215, 0.14);
  }
</style>