<template>
  <div>
    <el-dialog title="编辑" :visible.sync="editorShow" width="50%" >
      <div v-show="false">
        <el-upload
            :headers="{'Authorization':userToken}"
            :action="$baseURL+'/upload'"
            :show-file-list="false"
            :on-success="handleImgSuccess"
            :before-upload="beforeImgUpload">
          <el-button id="imgupload" v-show="false" size="small" type="primary">点击上传</el-button>
        </el-upload>
      </div>
      <quill-editor v-model="CommentInfo.content" ref="myQuillEditor" style="height: 220px;" :options="editorOption">
        <!-- 自定义toolar -->
        <div id="toolbar" slot="toolbar">
          <!-- Add a bold button -->
          <button class="ql-bold" title="加粗">Bold</button>
          <button class="ql-italic" title="斜体">Italic</button>
          <button class="ql-underline" title="下划线">underline</button>
          <button class="ql-strike" title="删除线">strike</button>
          <button class="ql-blockquote" title="引用"></button>
          <button class="ql-code-block" title="代码"></button>
          <select class="ql-font" title="字体">
            <option value="SimSun">宋体</option>
            <option value="SimHei">黑体</option>
            <option value="Microsoft-YaHei">微软雅黑</option>
            <option value="KaiTi">楷体</option>
            <option value="FangSong">仿宋</option>
            <option value="Arial">Arial</option>
          </select>
          <button class="ql-image" title="图片"></button>
        </div>
      </quill-editor>
      <span slot="footer" class="dialog-footer">
    <el-button @click="editorShow = false">取 消</el-button>
    <el-button type="primary" @click="submitComment">确 定</el-button>
  </span>
    </el-dialog>
  </div>
</template>

<script>
  import {
    Quill,
    quillEditor
  } from 'vue-quill-editor'
  import 'quill/dist/quill.core.css'
  import 'quill/dist/quill.snow.css'
  import 'quill/dist/quill.bubble.css'
  //引入font.css
  import '@/assets/editor/font.css'

  // 自定义字体大小
  let Size = Quill.import('attributors/style/size')
  Size.whitelist = ['10px', '12px', '14px', '16px', '18px', '20px']
  Quill.register(Size, true)

  // 自定义字体类型
  var fonts = ['SimSun', 'SimHei', 'Microsoft-YaHei', 'KaiTi', 'FangSong', 'Arial', 'Times-New-Roman', 'sans-serif',
    '宋体', '黑体'
  ]
  var Font = Quill.import('formats/font')
  Font.whitelist = fonts
  Quill.register(Font, true)
  export default {
    name: "CommentEditor",
    components: {
      quillEditor
    },
    props:{
      CommentInfo:{
        type:Object,
        default(){
          return {}
        }
      }
    },
    data() {
      return {
        editorShow:false,
        editorOption: {
          placeholder: "请输入",
          theme: "snow", // or 'bubble'
          modules: {
            toolbar: {
              container: '#toolbar',
              handlers: {
                'image': function (value) {
                  if (value) {
                    //利用element组件实现文件上传
                    document.querySelector('#imgupload').click()
                    //alert('自定义图片')
                  } else {
                    this.quill.format('image', false);
                  }
                }
              }
            }
          }
        }
      }
    },
    methods:{
      async submitComment(){
        //根据artCId判断是修改还是评论
        let res;
        if (this.CommentInfo.artCId == null) {
          //评论
          res = await this.$http.post('articleComment/save', this.CommentInfo)
        }else{
          //修改
          res = await this.$http.put('articleComment/update',this.CommentInfo)
        }
        if (res.code === 200) {
          this.editorShow = false;
          this.$emit('refreshComment', res.code)
        }
      },
      beforeImgUpload(file) {
        const isJPG = file.type === 'image/jpeg' || file.type === 'image/png'
        const isLt2M = file.size / 1024 / 1024 < 2;

        if (!isJPG) {
          this.$message.error('上传头像图片只能是 JPG/PNG 格式!')
        }
        if (!isLt2M) {
          this.$message.error('上传头像图片大小不能超过 2MB!')
        }
        return isJPG && isLt2M
      },
      handleImgSuccess(res, file) {
        // 获取富文本组件实例
        let quill = this.$refs.myQuillEditor.quill
        // 如果上传成功
        if (res.code === 200) {
          // 获取光标所在位置
          let length = quill.getSelection().index;
          // 插入图片，res为服务器返回的图片链接地址
          quill.insertEmbed(length, 'image', this.$fileURL+res.data.fileUrl)
          // 调整光标到最后
          quill.setSelection(length + 1)
        } else {
          // 提示信息，需引入Message
          this.$message.error('图片插入失败')
        }
      }

    },computed: {
      userToken() {
        return window.sessionStorage.getItem('token')
      }
    }
  }
</script>

<style scoped>
</style>