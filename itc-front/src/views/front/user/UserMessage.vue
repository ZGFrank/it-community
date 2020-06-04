<template>
  <div class="fly-panel fly-panel-user" pad20>
    <div class="layui-tab layui-tab-brief" style="margin-top: 15px;">
      <button @click="deleteAll()" class="layui-btn layui-btn-danger layui-btn-sm" v-if="messages.length > 0">清空全部消息
      </button>
      <div id="LAY_minemsg" style="margin-top: 10px;">
        <div class="fly-none" v-if="messages.length === 0">您暂时没有最新消息</div>
        <ul class="mine-msg" v-if="messages.length > 0">
          <li data-id="123" v-for="msg in messages" :key="msg.msgId">
            <blockquote class="layui-elem-quote" v-html="msg.content">
            </blockquote>
            <p><span>{{msg.createTime}}</span>
              <a href="javascript:;" @click="deleteById(msg.msgId)"
                 class="layui-btn layui-btn-small layui-btn-xs layui-btn-danger fly-delete">删除</a></p>
          </li>
        </ul>
      </div>
    </div>
  </div>
</template>

<script>
  export default {
    name: "UserMessage",
    data() {
      return {
        uId: this.$store.getters.getUserId,
        messages: []
      }
    },
    created() {
      this.getMessages()
    },
    methods: {
      async getMessages() {
        const res = await this.$http.get('message/all/' + this.uId)
        if (res.code === 200) {
          this.messages = res.data || []
          this.$store.commit('setMsgCount', this.messages.length)
        } else {
          this.$message.error('消息获取失败，请刷新重试')
        }
      },
      async deleteAll() {
        let ids = this.messages.map(t => t.msgId).join(",")
        const res = await this.$http.delete('message/all', {params: {ids}})
        if (res.code === 200) {
          this.$message.success("删除成功")
          this.getMessages()
        } else {
          this.$message.error("删除失败")
        }
      },
      async deleteById(id) {
        const res = await this.$http.delete('message/' + id)
        if (res.code === 200) {
          this.$message.success("删除成功")
          this.getMessages()
        } else {
          this.$message.error("删除失败")
        }
      }
    }
  }
</script>

<style scoped>
  .layui-elem-quote >>> .remind {
    color: #ff1125;
  }
</style>