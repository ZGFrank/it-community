<template>
    <ul class="fly-list">
        <article-item v-for="article in articleData" :article-item="article" />
    </ul>
</template>

<script>
    import ArticleItem from "../../../components/article/ArticleItem";

    export default {
        name: "ArticleList",
        props:{
            articleData:{
                type:Array,
                default(){
                    return []
                }
            }
        },
        components: {
            ArticleItem
        }
    }
</script>

<style scoped>

</style>