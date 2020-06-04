<template>
  <div>
    <el-dialog
      title="选择收藏文件夹"
      :visible.sync="collectShow"
      width="30%"
      @open="getCollectFolder"
      :close-on-click-modal="false"
      :before-close="handleClose">
      <!--内容区-->
      <div class="folders">
        <el-form ref="folderFrm" :model="folderFrm" :rules="folderRules" v-if="createFolder">
          <el-form-item prop="dirname">
            <el-input v-model="folderFrm.dirname" @keyup.enter.native="submitFolder" placeholder="请输入文件夹名称"></el-input>
          </el-form-item>
        </el-form>
        <div class="folder-item" v-for="folder in folders" :key="folder.did">
          <div class="folder-info">
            <span class="folder-name">{{folder.dirname}}</span>
            <span class="num">{{folder.num}}条内容</span>
          </div>
          <el-button type="success" size="mini" @click="colArticle(folder.did)" plain>收藏</el-button>
        </div>
      </div>

      <span slot="footer" class="dialog-footer">
        <el-link type="primary" :underline="false" v-if="!createFolder" @click="createFolder = true">+创建文件夹</el-link>
        <div v-if="createFolder">
          <el-link type="info" :underline="false" @click="createFolder = false">返回</el-link>
        </div>
      </span>
    </el-dialog>
  </div>
</template>

<script>
  export default {
    name: "CollectArticle",
    props:{
      artId:{
        type:Number,
        required:true
      }
    },
    data() {
      return {
        collectShow: false,
        createFolder:false,
        folders:[],
        folderFrm:{
          uId:this.$store.getters.getUserId,
          artId:0,
          dirname:''
        },
        folderRules:{
          dirname: [
            { required: true, message: '请输入文件夹名称', trigger: 'blur' },
            { min: 2, max: 20, message: '长度在 2 到 20 个字符', trigger: 'blur' }
          ]
        }
      }
    },
    methods: {
      //收藏文章
      async colArticle(dId) {
        console.log(dId)
        this.folderFrm.dirId = dId
        this.folderFrm.artId = this.artId
        const res = await this.$http.post('articleFavorite', this.folderFrm)
        if (res.code === 200) {
          this.$message.success("添加成功")
          this.collectShow = false
          //res.data为新增的收藏ID
          this.$emit("changeCollect",res.data)
        }else
          this.$message.error("添加失败")
      },
      //保存文件夹
      async submitFolder(){
        const res = await this.$http.post('favoriteFolder', this.folderFrm)
        if (res.code === 200) {
          this.createFolder = false
          this.$message.success("创建成功")
          this.getCollectFolder()
        }else{
          this.$message.error("创建失败，请重试")
        }
      },
      //获取用户收藏文件夹
      async getCollectFolder(){

        const res = await this.$http.get('favoriteFolder/' + this.$store.getters.getUserId)
        if (res.code === 200) {
          this.folders = res.data
        }
      },
      handleClose(done) {
        this.collectShow = false
      }
    }
  }
</script>

<style lang="less" scoped>
  .folders{
    width: 100%;

    .folder-item{
      display: flex;
      justify-content: space-between;
      align-items: center;

      .folder-info{
        display: flex;
        flex-direction: column;

        .folder-name{
          color: #4d4d4d;
        }
        .num{
          color: #999;
          font-weight: 400;
        }
      }
    }

  }
</style>