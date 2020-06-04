<template>
  <li data-id="123">
    <a class="fly-case-img" :href="item.url" target="_blank">
      <img :src="$fileURL+item.caseImg" :alt="item.title">
      <cite class="layui-btn layui-btn-primary layui-btn-small">去围观</cite>
    </a>
    <h2><a :href="item.url" target="_blank">{{item.title}}</a></h2>
    <p class="fly-case-desc">{{item.introduce}}</p>
    <div class="tool">
      <div class="layui-btn-group" v-if="modify">
        <button type="button" @click="modifyCase" class="layui-btn layui-btn-normal layui-btn-xs">
          <i class="layui-icon">&#xe642;</i>
        </button>
        <button type="button" @click="delCase" class="layui-btn layui-btn-danger layui-btn-xs">
          <i class="layui-icon">&#xe640;</i>
        </button>
      </div>
      <div>
        <span style="color: #00FF00" v-if="item.state===0">审核中</span>
        <span style="color: #ff1125" v-if="item.state===-1">拒绝申请</span>
        <span style="color: #ff1125" v-if="item.state===-2">已下架</span>
      </div>
    </div>
    <div class="fly-case-info">
      <router-link :to="`/userPage/${item.uid}`" class="fly-case-user">
        <img :src="$fileURL+item.avatar">
      </router-link>
      <p class="layui-elip" style="font-size: 12px;"><span style="color: #666;">{{item.nickname}}</span>{{$getFriendlyTime(item.createTime)}}
      </p>
      <p>获得<a class="fly-case-nums fly-case-active" href="javascript:;" data-type="showPraise"
              style=" padding:0 5px; color: #01AAED;">{{zanNum}}</a>个赞</p>
      <button class="layui-btn layui-btn-primary fly-case-active" @click="hitsOrCancelZan(item.id)" data-type="praise"
              v-if="!hits">点赞
      </button>
      <button class="layui-btn  fly-case-active" @click="hitsOrCancelZan(item.id)" data-type="praise" v-if="hits">已赞
      </button>

    </div>
  </li>
</template>

<script>
  export default {
    name: "CaseItem",
    props: {
      item: {
        type: Object,
        default() {
          return {}
        }
      },
      isZan: {
        type: Boolean,
        default() {
          return false
        }
      },
      modify: {
        type: Boolean,
        default() {
          return false
        }
      }
    },
    data() {
      return {
        hits: this.isZan,
        zanNum: this.item.zan
      }
    },
    methods: {
      modifyCase() {
        this.$emit('editCase', this.item)
      },
      async delCase() {
        this.$confirm('此操作将永久删除该案例, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(async () => {
          const res = await this.$http.delete('case/' + this.item.id)
          if (res.code === 200) {
            this.$message.success("删除成功")
            return this.$emit('refreshCase')
          }
          this.$message.error("删除失败")
        })

      },
      async hitsOrCancelZan(id) {
        let uid = this.$store.getters.getUserId
        if (uid == null) return this.$router.push('/login')
        if (this.hits) {
          //取消点赞
          const res = await this.$http.delete(`caseZan/${id}/user/${uid}`)
          if (res.code === 200) {
            this.zanNum--
            this.hits = !this.hits
          } else {
            this.$message.error("取消失败，请重试")
          }
        } else {
          //点赞
          const zanRecord = {cId: id, uId: uid}
          const res = await this.$http.post('caseZan', zanRecord)
          if (res.code === 200) {
            this.zanNum++
            this.hits = !this.hits
          } else {
            this.$message.error("点赞失败，请重试")
          }
        }
      }
    }
  }
</script>

<style scoped>
  .case-tool {
    float: right;
  }

  .tool {
    display: flex;
    justify-content: space-between;
  }
</style>