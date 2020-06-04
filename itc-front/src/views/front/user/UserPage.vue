<template>
  <div>
    <my-header :uid="parseInt($route.params.uId)"></my-header>
    <my-main :uid="parseInt($route.params.uId)"></my-main>
  </div>


</template>

<script>
  import Header from "../../../components/user/userPage/Header"
  import Main from "../../../components/user/userPage/Main"
  export default {
    name: "UserPage",
    components:{
      MyHeader:Header,MyMain:Main
    }
  }
</script>

<style scoped>

</style>