<template>
  <div style="height: 100%">
    <el-tabs v-model="activeName" @tab-click="handleClick" style="height: 100%">
      <el-tab-pane :label="`我的文章(${articles.length})`" name="first" style="height: 100%">
        <div class="layui-tab-item layui-show">
          <ul class="mine-view jie-row">
            <li v-for="art in articles" :key="art.artId">
              <router-link :to="'/articleDetail/'+art.artId" class="jie-title">{{art.title}}</router-link>
              <i>{{$getFriendlyTime(art.createTime)}}</i>
              <router-link class="mine-edit"
                :to="`/write/${art.artId}`">编辑</router-link>
              <el-link type="danger" :underline="false" @click="delArticle(art.artId)">删除</el-link>
              <em>{{art.watch}}阅/{{art.hitsComment}}答/{{art.hitsZan}}赞</em>
            </li>
          </ul>
          <div id="LAY_page"></div>
        </div>
        <div class="buttom-page">
          <el-pagination
            background
            layout="prev, pager, next"
            hide-on-single-page
            @current-change="getArticle"
            :current-page.sync="info.currentPage"
            :page-size.sync="info.pageSize"
            :total="total">
          </el-pagination>
        </div>
      </el-tab-pane>
      <el-tab-pane :label="`我收藏的文章(${collectNum})`" name="second">
        <el-collapse accordion  @change="change">
          <el-collapse-item :name="folder.did" v-for="(folder,index) in folders" :key="folder.did">
            <template slot="title">
              <div class="col">
                <div class="col-info">
                  <span class="col-name" v-if="folderEdit[index] == null || folderEdit[index] === false">{{folder.dirname}}({{folder.list.length}})</span>
                  <el-form style="margin-top:20px" ref="folderFrm" :model="folderFrm" :rules="folderRules" v-if="folderEdit[index]">
                    <el-form-item prop="dirname">
                      <el-input size="mini" v-model="folderFrm.dirname" @click.stop.native @keyup.enter.native="submitFolder" placeholder="请输入文件夹名称"></el-input>
                    </el-form-item>
                  </el-form>
                  <span class="col-create" v-if="folderEdit[index] == null || folderEdit[index] === false">{{folder.createTime}}</span>
                </div>
                <div class="col-operate" v-if="folder.state">
                  <el-link type="primary" @click.stop="updateFolder(index)" :underline="false">修改</el-link>
                  <el-link type="danger" @click.stop="delFolder(index,folder.did)" :underline="false" >删除</el-link>
                </div>
              </div>
            </template>
            <div class="col-main">
              <ul class="mine-view jie-row" v-if="folder.list.length > 0">
                <li v-for="(art,subIndex) in folder.list" :key="art.artId">
                  <router-link :to="'/articleDetail/'+art.artId" class="jie-title">{{art.title}}</router-link>
                  <i>收藏于{{art.createTime}}</i>
                  <em @click="cancelCollect(art.fid,index,subIndex)" style="cursor: pointer">取消收藏</em>
                </li>
              </ul>
            </div>
          </el-collapse-item>
        </el-collapse>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script>
  export default {
    name: "UserCenter",
    data() {
      return {
        activeName: 'first',
        total:0,
        articles:[],
        folders:[],
        info:{
          currentPage:1,
          pageSize:10
        },
        folderEdit:[],
        editIndex:-1,
        folderFrm:{},
        folderRules:{
          dirname: [
            { required: true, message: '请输入文件夹名称', trigger: 'blur' },
            { min: 2, max: 20, message: '长度在 2 到 20 个字符', trigger: 'blur' }
          ]
        }

      }
    },
    mounted() {
      this.getArticle()
      this.getFavorites()
    },
    methods: {
      updateFolder(index) {
        if (this.editIndex !== -1) {
          this.$set(this.folderEdit,this.editIndex,false)
        }
        this.folderFrm = this.folders[index]
        this.$set(this.folderEdit,index,true)
        this.editIndex = index
      },
      delFolder(index, did) {
        if (this.folders[index].list.length > 0) {
          return this.$message.warning("该文件夹下有收藏，不能删除")
        }
        this.$confirm('您确定要删除该文件夹吗?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(async () => {
          const res = await this.$http.delete('favoriteFolder/'+did)
          if (res.code === 200) {
            this.$message.success(`删除成功`)
            this.getFavorites()
          } else {
            this.$message.error(`删除失败`)
          }
        })
      },
      change(param) {
        if (this.editIndex !== -1) {
          this.$set(this.folderEdit,this.editIndex,false)
        }
      },
      handleClick(tab, event) {
        console.log(tab, event)
      },
      async getArticle(){
        const res = await this.$http.get('article/all/'+this.$store.getters.getUserId
          ,{params:this.info})
        if (res.code === 200) {
          this.total = res.data.total
          this.articles = res.data.data
        }else{
          this.$message.error("获取失败，请刷新重试")
        }
      },
      //删除文章
      delArticle(artId) {
        this.$confirm('此操作将永久删除该文章, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(async () => {
          const res = await this.$http.delete('article/' + artId)
          if (res.code === 200) {
            this.$message.success("删除成功")
            this.getArticle()
          }
          this.$message.error("删除失败")
        })

      },
      async getFavorites() {
        const res = await this.$http.get('articleFavorite/'+this.$store.getters.getUserId)
        if (res.code === 200) {
          this.folders = res.data
          for (let i in this.folders) {
            this.folderEdit.push(false)
          }
        }
      },
      async cancelCollect(fId,index,subIndex) {
        const res = await this.$http.delete('articleFavorite/' + fId)
        if (res.code === 200) {
          this.folders[index].list.splice(subIndex, 1)
        } else {
          this.$message.error("取消失败请重试")
        }
      },
      //修改文件夹
      async submitFolder(){
        const res = await this.$http.put('favoriteFolder', this.folderFrm)
        if (res.code === 200) {
          this.$set(this.folderEdit,this.editIndex,false)
          this.$message.success("修改成功")
          this.folders[this.editIndex].dirname = this.folderFrm.dirname
        }else{
          this.$message.error("创建失败，请重试")
        }
      },
    },
    computed:{
      collectNum(){
        let sum = 0
        for (let folder of this.folders) {
          sum += folder.list.length
        }
        return sum
      }
    }
  }
</script>

<style scoped>
  .col{
    width: 100%;
    display: flex;
    justify-content: space-between;
  }
  .col-info{
    align-items: center;
  }
  .col-create{
    position: absolute;
    left: 200px;
  }
  .col-operate {
    margin-right: 20px;
    width: 65px;
    display: flex;
    justify-content: space-between;
  }
  .col-main{
    padding: 10px 30px 10px 40px;
  }
</style>