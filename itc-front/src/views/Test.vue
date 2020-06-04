<template>
  <div>

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
    name: 'FuncFormsEdit',
    components: {
      quillEditor
    },
    data() {
      return {
        content: null,
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