<template>
    <li>
        <router-link :to="'/userPage/'+articleItem.uid" class="fly-avatar">
            <img :src="$fileURL+articleItem.avatar" :alt="articleItem.nickname">
        </router-link>
        <h2>
            <a class="layui-badge">{{articleItem.name}}</a>
            <router-link :to="'/articleDetail/'+articleItem.artId" >{{articleItem.title}}</router-link>
        </h2>
        <div class="fly-list-info">
            <router-link :to="'/userPage/'+articleItem.uid" >
                <cite>{{articleItem.nickname}}</cite>
                <i class="layui-badge fly-badge-vip" v-if="articleItem.vip>0">VIP{{articleItem.vip}}</i>
            </router-link>
            <span>{{$getFriendlyTime(articleItem.createTime)}}</span>

            <span class="fly-list-kiss layui-hide-xs" title="悬赏学币"><i class="el-icon-medal"></i> {{articleItem.learnCoin}}</span>
            <span class="layui-badge fly-badge-accept layui-hide-xs" v-if="articleItem.isOver">完结</span>
            <span class="fly-list-nums">
                <i class="iconfont icon-pinglun1" title="回答"></i> {{articleItem.hitsComment}}
              </span>
        </div>
        <div class="fly-list-badge">
            <!--<span class="layui-badge layui-bg-black">置顶</span>-->
            <span class="layui-badge layui-bg-red" v-if="articleItem.status">精帖</span>
        </div>
    </li>
</template>

<script>
    export default {
        name: "ArticleItem",
        props:{
            articleItem: {
                type:Object,
                default(){
                    return {}
                }
            }
        }

    }
</script>

<style scoped>
</style>