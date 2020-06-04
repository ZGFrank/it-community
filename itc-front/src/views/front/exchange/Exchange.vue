<template>
  <div>
    <div class="fly-exchange-header">
      <el-link :underline="false">
        <span class="fly-exchange-banner" style="font-size: 3.25rem;font-weight: 700;color: #ffffff;">
          提交需求，等价交换
        </span>
      </el-link>
      <div class="fly-exchange-btn">
        <el-button @click="openExchangeFrm" type="primary">提交需求</el-button>
        <el-button @click="changeContext" plain>{{loadAll?'我的需求':'所有需求'}}</el-button>
        <el-link type="info" style="padding: 0 15px;">提交要求</el-link>
      </div>
    </div>
    <div class="exchange-container">
      <el-row :gutter="20">
        <el-col :span="8" v-for="item in exchanges" :key="item.id">
          <el-card class="box-card exchange-item">
            <div slot="header" class="clearfix">
              <span>{{item.title}}</span>
              <div style="float: right">
                <span style="color: #00FF00" v-if="item.state===0">审核中</span>
                <span style="color: #ff1125" v-if="item.state===-1">拒绝申请</span>
              </div>
            </div>
            <div class="exchange-item-body">
              <div class="content">
                <span>要求</span>
                <p>
                  {{item.claim}}
                </p>
              </div>
              <div class="exchange-tool">
                <div class="exchange-userinfo">
                  <span>发布人：{{item.name}}</span>
                  <span>QQ：{{item.qq}}</span>
                </div>
                <div>
                  <div class="layui-btn-group" v-if="!loadAll">
                    <button type="button" class="layui-btn layui-btn-normal layui-btn-sm" @click="editExchange(item)">
                      <i class="layui-icon">&#xe642;</i>
                    </button>
                    <button type="button" class="layui-btn layui-btn-danger layui-btn-sm" @click="delExchange(item)">
                      <i class="layui-icon">&#xe640;</i>
                    </button>
                  </div>
                </div>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
      <div class="exchange-page">
        <el-pagination
          background
          hide-on-single-page
          layout="prev, pager, next"
          @current-change="getExchange"
          :current-page.sync="query.currentPage"
          :page-size.sync="query.pageSize"
          :total="total">
        </el-pagination>
      </div>
    </div>

    <el-dialog title="提交需求" :visible.sync="exchangeDialog" :destroy-on-close="true">
      <el-form ref="exchangeFrm" :model="exchangeFrm" :rules="exchangeRules">
        <el-form-item label="需求名称" label-width="80px" prop="title">
          <el-input v-model="exchangeFrm.uId" v-show="false" autocomplete="off"></el-input>
          <el-input v-model="exchangeFrm.title" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="要求" label-width="80px" prop="claim">
          <el-input type="textarea" :rows="7" v-model="exchangeFrm.claim"></el-input>
        </el-form-item>
        <el-form-item label="QQ" label-width="80px" prop="qq">
          <el-input v-model="exchangeFrm.qq" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="exchangeDialog = false">取 消</el-button>
        <el-button type="primary" @click="subExchangeFrm">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  export default {
    name: "Exchange",
    data() {
      return {
        loadAll: true,
        exchangeDialog: false,
        exchanges: [],
        total: 0,
        query: {
          currentPage: 1,
          pageSize: 9,
          uId:null,
          state:1
        },
        exchangeFrm: {
          uId: this.$store.getters.getUserId,
          name: this.$store.state.userInfo.name,
          title: '',
          claim: '',
          qq: ''
        },
        exchangeRules: {
          title: [
            {required: true, message: '请输入需求名称', trigger: 'blur'},
            {min: 5, max: 20, message: '长度在 5 到 20 个字符', trigger: 'blur'}
          ],
          claim: [
            {required: true, message: '请输入要求', trigger: 'blur'},
            {min: 20, max: 220, message: '长度在 20 到 220 个字符', trigger: 'blur'}
          ],
          qq: [
            {required: true, message: '请输入联系QQ', trigger: 'blur'}
          ]
        }
      }
    },
    created() {
      this.getExchange()
    },
    methods: {
      async editExchange(item) {
        this.exchangeFrm = item
        this.exchangeDialog = true
      },
      delExchange(item) {
        //如果是系统拒绝的，不比比直接删
        if (item.state === -1) {
          this.deleteById(item.id)
        } else {
          this.$confirm('此操作将永久删除该需求, 是否继续?', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }).then(() => {
            this.deleteById(item.id)
          })
        }
      },
      async deleteById(id) {
        const res = await this.$http.delete('exchange/'+id)
        if (res.code === 200) {
          this.$message.success("删除成功")
          return this.getExchange()
        }
        this.$message.error("删除失败")
      },
      async getExchange() {
        const res = await this.$http.get('exchange',{params: this.query})
        if (res.code === 200) {
          this.exchanges = res.data.data || []
          this.total = res.data.total
        } else {
          this.$message.error("加载失败")
        }
      },
      openExchangeFrm() {
        //判断用户是否登录
        if (this.exchangeFrm.uId == null) return this.$router.push('/login')
        this.exchangeDialog = true
      },
      changeContext() {
        this.loadAll = !this.loadAll
        if (!this.loadAll) {
          this.query.uId = this.$store.getters.getUserId
          this.query.state = null
        } else {
          this.query.uId = null
          this.query.state = 1
        }
        this.getExchange()
      },
      async subExchangeFrm() {
        this.$refs.exchangeFrm.validate(async valid => {
          if (!valid) return
          //判断是创建还是修改
          if (this.exchangeFrm.id == null) {
            //创建需求
            const res = await this.$http.post('exchange', this.exchangeFrm)
            if (res.code === 200) {
              this.$message.success("提交成功，等待管理员审核")
              this.exchangeDialog = false
              this.$refs.exchangeFrm.resetFields()
              if (!this.loadAll) {
                this.getExchange()
              }
            } else {
              this.$message.error("提交失败，请重试")
            }
          } else {
            const res = await this.$http.put('exchange', this.exchangeFrm)
            if (res.code === 200) {
              this.exchangeDialog = false
              this.$refs.exchangeFrm.resetFields()
              this.getExchange()
            } else {
              this.$message.error("修改失败，请重试")
            }
          }

        })
      }
    }
  }
</script>

<style lang="less" scoped>
  .fly-exchange-header {
    position: relative;
    height: 220px;
    text-align: center;
    background: #393D49;
  }

  .fly-exchange-banner {
    position: absolute;
    left: 50%;
    top: 30px;
    width: 670px;
    margin-left: -335px;
  }

  .fly-exchange-btn {
    position: absolute;
    bottom: 30px;
    left: 0;
    width: 100%;
    text-align: center;
  }

  .fly-exchange-btn a {
    color: #fff;
  }

  .fly-exchange-btn .layui-btn-primary {
    background: none;
    color: #fff;
  }

  .fly-exchange-btn .layui-btn-primary:hover {
    border-color: #409EFF;
  }

  .exchange-container {
    padding: 20px 60px;
    background-color: #DCDFE6;
  }

  .exchange-item {
    margin-top: 10px;
  }

  .exchange-item-body {
    height: 250px;

    .content {
      height: 220px;
    }
  }

  .exchange-tool {
    display: flex;
    justify-content: space-between;
    align-items: center;
  }

  .exchange-page {
    width: 100%;
    text-align: center;
  }

  .exchange-userinfo {
    display: flex;
    flex-direction: column;
  }
</style>