<template>
  <div>
    <div class="fly-case-header">
      <p class="fly-case-year">2020</p>
      <el-link :underline="false">
        <span class="fly-case-banner" style="font-size: 3.25rem;font-weight: 700;color: #ffffff;">分享个人案例</span>
      </el-link>
      <div class="fly-case-btn">
        <el-button @click="openCaseFrm" type="primary">提交案例</el-button>
        <el-button @click="changeContext" plain>{{loadAll?'我的案例':'所有案例'}}</el-button>
        <el-link type="info" style="padding: 0 15px;">案例要求</el-link>
      </div>
    </div>

    <div class="fly-main" style="overflow: hidden;">

      <div class="fly-tab-border fly-case-tab">
    <span>
      <a href="javascript:;" @click="yearSpace = 0" :class="{'tab-this':yearSpace===0}">2020年度</a>
      <a href="javascript:;" @click="yearSpace = 1" :class="{'tab-this':yearSpace===1}">2019年度</a>
    </span>
      </div>
      <el-tabs v-model="activeName" @tab-click="handleClick">
        <el-tab-pane label="按提交时间" name="first" :lazy="true">
          <case-list ref="obt" @editCase="openEditCase" :year-space="yearSpace" :load-all="loadAll" :order-by-time="true"/>
        </el-tab-pane>
        <el-tab-pane label="按点赞排行" name="second" :lazy="true">
          <case-list ref="obz" @editCase="openEditCase" :year-space="yearSpace" :load-all="loadAll" :order-by-time="false"/>
        </el-tab-pane>
      </el-tabs>
    </div>

    <el-dialog title="提交案例" :visible.sync="caseDialog" :destroy-on-close="true">
      <el-form ref="caseFrm" :model="caseFrm" :rules="caseRules">
        <el-form-item label="项目名称" label-width="80px" prop="title">
          <el-input v-model="caseFrm.uId" v-show="false" autocomplete="off"></el-input>
          <el-input v-model="caseFrm.title" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="项目介绍" label-width="80px" prop="introduce">
          <el-input type="textarea" v-model="caseFrm.introduce"></el-input>
        </el-form-item>
        <el-form-item label="项目地址" label-width="80px" prop="url">
          <el-input v-model="caseFrm.url" placeholder="请输入项目完整地址(包括协议http/https)" autocomplete="off"></el-input>
          <el-input v-model="caseFrm.caseImg" v-show="false" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="展示图片" label-width="80px">

          <el-upload
            class="avatar-uploader"
            accept="image/*"
            :headers="{'Authorization':userToken}"
            :action="fileUploadUrl"
            :show-file-list="false"
            :on-success="handleAvatarSuccess"
            :before-upload="beforeAvatarUpload">
            <img v-if="caseFrm.caseImg" :src="$fileURL+caseFrm.caseImg" class="avatar">
            <i v-else class="el-icon-plus avatar-uploader-icon"></i>
            <div slot="tip" class="el-upload__tip">点击上传，只能上传jpg/png文件，且不超过500kb</div>
          </el-upload>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="caseDialog = false">取 消</el-button>
        <el-button type="primary" @click="subCaseFrm">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import CaseList from "../../../components/case/CaseList"

  export default {
    name: "case",
    data() {
      return {
        activeName: 'first',
        caseDialog: false,
        yearSpace: 0,
        loadAll:true,
        caseFrm: {
          uId: this.$store.getters.getUserId,
          title: '',
          introduce: '',
          caseImg: null,
          url: ''
        },
        caseRules:{
          title: [
            { required: true, message: '请输入项目名称', trigger: 'blur' },
            { min: 2, max: 20, message: '长度在 2 到 20 个字符', trigger: 'blur' }
          ],
          introduce:[
            { required: true, message: '请输入项目介绍', trigger: 'blur' },
            { min: 10, max: 50, message: '长度在 10 到 50 个字符', trigger: 'blur' }
          ],
          url:[
            { required: true, message: '请输入项目地址', trigger: 'blur' }
          ]
        }
      }
    },
    components: {
      CaseList
    },
    methods: {
      openEditCase(item) {
        this.caseDialog = true
        this.caseFrm = item
      },
      changeContext() {
        if (this.$store.getters.getUserId == null) return this.$router.push('/login')
        this.loadAll = !this.loadAll
      },
      openCaseFrm() {
        if (this.caseFrm.uId == null) {
          return this.$router.push('/login')
        }
        this.caseDialog = true
      },
      subCaseFrm() {
        this.$refs.caseFrm.validate(async valid =>{
          if (!valid) return
          if (this.caseFrm.caseImg == null) {
            return this.$message.error('您未上传封面')
          }
          if (this.caseFrm.id == null) {
            //提交案例
            const res = await this.$http.post('case',this.caseFrm)
            if (res.code === 200) {
              this.$message.success("提交成功，等待管理员审核")
              this.caseDialog = false
              this.resetFrm()
            }else {
              this.$message.error("提交失败，请重试")
            }
          }else {
            const res = await this.$http.put('case', this.caseFrm)
            if (res.code === 200) {
              this.$message.success("修改成功")
              this.caseDialog = false
              this.resetFrm()
              await this.$refs.obt.getCase()
              await this.$refs.obz.getCase()
            }else {
              this.$message.error("修改失败，请重试")
            }
          }

        })
      },
      handleClick(tab, event) {
        //console.log(tab, event)
      },
      handleAvatarSuccess(res, file) {
        if (res.code === 200) {
          console.log(res.data)
          this.caseFrm.caseImg = res.data.fileUrl
        } else {
          this.$message.error(res.msg)
        }
      },
      beforeAvatarUpload(file) {
        const isJPG = file.type === 'image/jpeg' || file.type === 'image/png'
        const isLt2M = file.size / 1024 / 1024 < 2;

        if (!isJPG) {
          this.$message.error('上传封面图片只能是 JPG/PNG 格式!')
        }
        if (!isLt2M) {
          this.$message.error('上传封面图片大小不能超过 2MB!')
        }
        return isJPG && isLt2M
      },
      resetFrm() {
        //重置表单数据
        this.$refs.caseFrm.resetFields()
        this.caseFrm.caseImg = null
      }
    },
    computed: {
      userToken() {
        return window.sessionStorage.getItem('token')
      },
      fileUploadUrl() {
        return this.$baseURL+'/upload?old='+this.caseFrm.caseImg
      }
    }
  }
</script>

<style scoped>
  .avatar-uploader .el-upload {
    border: 1px dashed #d9d9d9;
    border-radius: 6px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
  }

  .avatar-uploader .el-upload:hover {
    border-color: #409EFF;
  }

  .avatar-uploader-icon {
    font-size: 28px;
    color: #8c939d;
    width: 240px;
    height: 150px;
    line-height: 240px;
    text-align: center;
  }

  .avatar {
    width: 240px;
    height: 150px;
    display: block;
  }
</style>