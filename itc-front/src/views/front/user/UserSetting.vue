<template>
  <div>
    <el-tabs>
      <el-tab-pane lazy label="我的资料">
        <el-form ref="userBaseInfoFrm" :model="userBaseInfo" size="small" label-width="80px">
          <el-form-item label="姓名">
            <el-input v-model="userBaseInfo.name"></el-input>
          </el-form-item>
          <el-form-item label="昵称">
            <el-input v-model="userBaseInfo.nickname"></el-input>
          </el-form-item>

          <el-form-item label="性别">
            <el-radio-group v-model="userBaseInfo.gender">
              <el-radio :label="1">男</el-radio>
              <el-radio :label="0">女</el-radio>
            </el-radio-group>
          </el-form-item>

          <el-form-item label="邮箱">
            <el-input v-model="userBaseInfo.email"></el-input>
            <span id="activeEmail" v-if="userBaseInfo.state==0"><a>您的邮箱未激活点击激活</a></span>
          </el-form-item>
          <el-form-item label="城市">
            <el-input v-model="userBaseInfo.city"></el-input>
          </el-form-item>
          <el-form-item label="签名">
            <el-input type="textarea" v-model="userBaseInfo.signature"></el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="onSubmit">修改</el-button>
          </el-form-item>
        </el-form>
      </el-tab-pane>
      <el-tab-pane lazy label="头像">
        <el-upload
            class="avatar-uploader"
            :headers="{'Authorization':userToken}"
            :action="fileUploadUrl"
            :show-file-list="false"
            :on-success="handleAvatarSuccess"
            :before-upload="beforeAvatarUpload">
          <img v-if="userBaseInfo.avatar" :src="$fileURL+userBaseInfo.avatar" class="avatar">
          <i v-else class="el-icon-plus avatar-uploader-icon"></i>
          <div slot="tip" class="el-upload__tip">点击上传，只能上传jpg/png文件，且不超过500kb</div>
        </el-upload>
      </el-tab-pane>
      <el-tab-pane lazy label="密码">
        <el-form id="changePass" ref="userPassFrm" :rules="passRule" :model="userPass" size="small" label-width="80px">
          <el-form-item prop="originPass" label="原始密码">
            <el-input v-model="userPass.originPass" show-password></el-input>
          </el-form-item>
          <el-form-item prop="newPass" label="新密码">
            <el-input v-model="userPass.newPass" show-password></el-input>
          </el-form-item>
          <el-form-item prop="confirmPass" label="确认密码">
            <el-input v-model="userPass.confirmPass" show-password></el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="changePass">确认</el-button>
          </el-form-item>
        </el-form>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script>

  export default {
    name: "UserSetting",
    data() {
      var confirmPass = (rule, value, callback) => {
        if (value === '') {
          callback(new Error('请再次输入密码'));
        } else if (value !== this.userPass.newPass) {
          callback(new Error('两次输入密码不一致!'));
        } else {
          callback();
        }
      }
      return {
        userBaseInfo: {},
        userPass: {
          originPass: '',
          newPass: '',
          confirmPass: '',
        },
        passRule: {
          originPass: [
            {required: true, message: '请输入密码', trigger: 'blur'},
            {min: 6, max: 15, message: '长度在 6 到 15 个字符', trigger: 'blur'}
          ],
          newPass:[
            {required: true, message: '请输入密码', trigger: 'blur'},
            {min: 6, max: 15, message: '长度在 6 到 15 个字符', trigger: 'blur'}
          ],
          confirmPass: [
            {required: true, message: '请输入密码', trigger: 'blur'},
            {validator: confirmPass, trigger: 'blur'}
          ]
        }
      }
    },
    created() {
      this.userBaseInfo = JSON.parse(window.sessionStorage.getItem('userInfo'))
    },
    methods: {
      onSubmit() {
        this.updateUser()
      },
      changePass() {
        this.$refs.userPassFrm.validate(async valid => {
          if (!valid)return
          const res = await this.$http.put(`user/pass/${this.$store.getters.getUserId}`, this.userPass)
          if (res.code === 200) return this.$message.success(res.msg)
          this.$message.error(res.msg)
        });
      },
      async updateUser() {
        //如果修改了邮箱，则邮箱激活态为0
        if (this.userBaseInfo.email !== this.$store.state.userInfo.email)
          this.userBaseInfo.state = 0
        const res = await this.$http.put(`user/update`, this.userBaseInfo)
        if (res.code === 200) {
          this.$message.success(res.msg)
          this.$store.commit('saveUser', this.userBaseInfo)

        } else {
          this.$message.error(res.msg)
        }
      },
      handleAvatarSuccess(res, file) {
        if (res.code === 200) {
          console.log(res.data)
          this.userBaseInfo.avatar = res.data.fileUrl
          this.updateUser()
        } else {
          this.$message.error(res.msg)
        }
      },
      beforeAvatarUpload(file) {
        const isJPG = file.type === 'image/jpeg' || file.type === 'image/png'
        const isLt2M = file.size / 1024 / 1024 < 2;

        if (!isJPG) {
          this.$message.error('上传头像图片只能是 JPG/PNG 格式!')
        }
        if (!isLt2M) {
          this.$message.error('上传头像图片大小不能超过 2MB!')
        }
        return isJPG && isLt2M
      }
    },
    computed: {
      userToken() {
        return window.sessionStorage.getItem('token')
      },
      fileUploadUrl() {
        return this.$baseURL+'/upload?old='+this.userBaseInfo.avatar
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
    width: 178px;
    height: 178px;
    line-height: 178px;
    text-align: center;
  }
  .avatar {
    width: 178px;
    height: 178px;
    display: block;
  }
  .el-form {
    width: 70%;
  }

  #changePass {
    width: 40%;
  }

  #activeEmail {
    cursor: pointer;
  }
</style>