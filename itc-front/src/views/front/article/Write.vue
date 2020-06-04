<template>
  <div class="write-pane">
    <mavon-editor
      class="editor"
      ref=md
      @imgAdd="$imgAdd"
      @imgDel="imgDel"
      @save="writeSave"
      :ishljs="true"
      codeStyle="atelier-cave-dark"
      v-model="article.content"/>
    <div class="write-help" @click="drawer = true">
      <i class="el-icon-s-help"></i>
    </div>
    <el-drawer
      :with-header="false"
      :append-to-body="true"
      :visible.sync="drawer"
      direction="rtl">
      <el-card class="box-card">
        <div slot="header" class="clearfix">
          <span>帮助文档</span>
        </div>
        <div>
          <el-table
            :data="writeHelp"
            stripe
            :height="tableH"
            style="width: 100%">
            <el-table-column
              prop="key"
              label="快捷键"
              width="180">
            </el-table-column>
            <el-table-column
              prop="function"
              label="功能">
            </el-table-column>
          </el-table>
        </div>
      </el-card>

    </el-drawer>

    <el-dialog title="上传文章" :visible.sync="dialogFormVisible">
      <el-form status-icon :model="article" :rules="rules">
        <el-form-item label="文章标题" :label-width="formLabelWidth" prop="title">
          <el-input v-model="article.title" autocomplete="off"></el-input>
          <el-input v-show="false" v-model="article.uId" autocomplete="off"></el-input>
          <el-input v-show="false" v-model="article.content" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="文章类型" :label-width="formLabelWidth" prop="cId">
          <el-select v-model="article.cId" placeholder="请选择文章类型">
            <el-option v-for="item in categories" :key="item.cId" :label="item.name" :value="item.cId"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="文章标签" :label-width="formLabelWidth">
          <el-tag
            :key="tag"
            v-for="tag in dynamicTags"
            closable
            :disable-transitions="false"
            @close="handleClose(tag)">
            {{tag}}
          </el-tag>
          <el-input
            class="input-new-tag"
            v-if="inputVisible"
            v-model="inputValue"
            ref="saveTagInput"
            size="small"
            @keyup.enter.native="handleInputConfirm"
            @blur="handleInputConfirm">
          </el-input>
          <el-button v-else class="button-new-tag" size="small" @click="showInput">+ New Tag</el-button>
        </el-form-item>

        <el-form-item label="悬赏学币" :label-width="formLabelWidth" prop="learnCoin">
          <el-input v-model="article.learnCoin" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="submitArticle">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import kys from '@/assets/json/key.json'

  export default {
    name: "Write",
    data() {
      let validateLearnCoin = (rule, value, callback) => {
        if (!Number.isInteger(value)) {
          return callback(new Error('请输入数值'));
        }
        if (value > this.$store.state.userInfo.learnCoin) {
          callback(new Error('您的学币不够支付'));
        } else if (value < 0) {
          callback(new Error('这种行为极度可耻'));
        } else {
        }
        callback();
      }

      return {
        dynamicTags: [],
        inputVisible: false,
        inputValue: '',
        formLabelWidth: '80px',
        drawer: false,
        writeHelp: kys,
        tableH: 500,
        dialogFormVisible: false,
        article: {
          title: '',
          content: '',
          cId: '',
          tag: '',
          learnCoin: 0,
          isOver: 1,
          uId: this.$store.getters.getUserId,
        },
        categories: [],
        rules: {
          title: [
            {required: true, message: '标题不能为空', trigger: 'blur'}
          ],
          cId: [
            {required: true, message: '类型必选', trigger: 'change'}
          ],
          learnCoin: [
            {validator: validateLearnCoin, trigger: 'blur'}
          ]
        }
      }
    },
    created() {
      let artId = this.$route.params.artId
      //>0表示修改
      if (artId > 0) {
        this.getArticle(artId)
      }
      this.initTableHeight()
      this.getArticleCategories()
    },
    methods: {
      async getArticle(artId) {
        let uId = this.$store.getters.getUserId
        if (uId == null) {
          return this.$router.push("/")
        }
        const res = await this.$http.get(`article/${artId}/user/${uId}`)
        if (res.code !== 200) {
          return this.$router.push("/")
        }
        if (res.data.uid !== uId) {
          return this.$router.push("/")
        }
        this.article = res.data
        this.dynamicTags = this.article.tag.split(',')
      },
      async submitArticle() {
        if (this.dynamicTags.length > 0)
          this.article.tag = this.dynamicTags.join(',')
        let res;
        if (this.article.artId != null) {
          //修改文章
          res = await this.$http.put('article/update', this.article)
          if (res.code === 200) {
            this.$message.success("修改成功")
            this.dialogFormVisible = false
            await this.$router.back()
          } else {
            this.$message.error("修改失败")
          }
        } else {
          //增加文章
          //如果设置学币数不为零，则表示未完结
          if (this.article.learnCoin > 0) {
            this.article.isOver = 0
          }
          res = await this.$http.post('article/save', this.article)
          if (res.code === 200) {
            this.$message.success(res.msg)
            this.dialogFormVisible = false
            //增加成功直接去自己的用户中心
            await this.$router.replace('/userCenter')
          } else {
            this.$message.error("操作失败")
          }
        }
      },
      handleClose(tag) {
        this.dynamicTags.splice(this.dynamicTags.indexOf(tag), 1);
      },

      showInput() {
        if (this.dynamicTags.length === 3) {
          this.$message.warning("最多只能创建三个标签")
          return
        }
        this.inputVisible = true;
        this.$nextTick(_ => {
          this.$refs.saveTagInput.$refs.input.focus();
        });
      },

      handleInputConfirm() {
        let inputValue = this.inputValue;
        if (inputValue) {
          this.dynamicTags.push(inputValue);
        }
        this.inputVisible = false;
        this.inputValue = '';
      },
      async getArticleCategories() {
        const res = await this.$http.get('category/front/loads/1')
        if (res.code === 200) {
          this.categories = res.data;
        } else {
          this.$message.error(res.msg)
        }
      },
      writeSave(value, render) {

        if (value === '') {
          return this.$message.warning('您什么都没有输入')
        }
        this.dialogFormVisible = true
      },
      async $imgAdd(pos, $file) {
        // 第一步.将图片上传到服务器.
        let formdata = new FormData();
        formdata.append('file', $file);
        const res = await this.$http({
          url: 'upload',
          method: 'post',
          data: formdata,
          headers: {'Content-Type': 'multipart/form-data'},
        })
        if (res.code === 200) {
          let url = this.$fileURL + res.data.fileUrl
          this.$refs.md.$img2Url(pos, url)
        } else {
          this.$message.error("图片上传失败")
        }
      },
      imgDel(pos) {
        console.log("del")
        console.log(pos)
        /*var formdata = new FormData()
        formdata.append('url', pos[0])
        delfile(formdata)
          .then(() => {
            Message.success('删除成功')
          })
          .catch(res => {
            console.log(res)
          })*/
      },
      initTableHeight() {
        let h = document.documentElement.clientHeight || document.body.clientHeight
        this.tableH = h - 120
      }
    }
  }
</script>

<style lang="less" scoped>
  .write-pane {
    height: 100%;
    padding: 0 50px;

    .editor {
      height: 100%;
    }

  }

  .write-help {
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
    right: 10px;
    top: 100px;
  }

  .write-help:hover {
    background-color: rgba(215, 215, 215, 0.14);
  }

  .el-tag + .el-tag {
    margin-left: 10px;
  }

  .button-new-tag {
    margin-left: 10px;
    height: 32px;
    line-height: 30px;
    padding-top: 0;
    padding-bottom: 0;
  }

  .input-new-tag {
    width: 90px;
    margin-left: 10px;
    vertical-align: bottom;
  }
</style>