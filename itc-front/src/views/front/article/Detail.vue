<template>
  <!--<div v-html="content">
  </div>-->
  <div class="fly-panel detail-box">
    <h1>{{article.title}}</h1>
    <div class="fly-detail-info">
      <span class="layui-badge layui-bg-green fly-detail-column">{{article.name}}</span>

      <span class="layui-badge" style="background-color: #999;" v-if="!article.isOver">未结</span>
      <span class="layui-badge" style="background-color: #5FB878;" v-else>已结</span>

      <span class="layui-badge layui-bg-black" v-if="article.top">置顶</span>
      <span class="layui-badge layui-bg-red" v-if="article.status">精帖</span>
      <el-tag size="mini" v-for="(tag,index) in tags" :key="tag" :type="tagType[index]">{{tag}}</el-tag>
      <div class="fly-admin-box" data-id="123">
        <span class="layui-btn layui-btn-xs layui-btn-danger jie-admin" type="del" @click="toDelArticle()"
              v-if="article.uid===userId || isSuper">删除</span>

        <span @click="saveOrCancelTop" v-if="isSuper" class="layui-btn layui-btn-xs jie-admin" type="set" field="stick"
              rank="1"
              :class="{'font-gray':!canTop}">{{canTop ? '置顶':'取消置顶'}}</span>
      </div>

      <span class="fly-list-nums">
        <span type="reply">
          <i class="el-icon-mouse" v-if="artZan" style="color: #2f7bff" @click="saveArtZan"></i>
          <i class="el-icon-mouse" v-else @click="saveArtZan"></i>{{article.hitsZan}}
				</span>
         <span type="reply" style="cursor: pointer">
           <i @click="toComment(article.artId,0)" class="iconfont" title="回答">&#xe60c;</i> {{article.hitsComment}}
					</span>
			<i class="iconfont" title="人气">&#xe60b;</i> {{article.watch}}
        <span @click="collectArticle" v-if="article.uid!==userId">
          <i class="el-icon-star-off" v-if="fId == null"></i>
          <i v-else class="el-icon-star-on" style="color: #2f7bff"></i>
        </span>
			</span>
    </div>
    <div class="detail-about">
      <router-link :to="'/userPage/'+article.uid" class="fly-avatar">
        <img :src="$fileURL+article.avatar" :alt="article.nickname">
      </router-link>
      <div class="fly-detail-user">
        <router-link :to="'/userPage/'+article.uid">
          <cite>{{article.nickname}}</cite>
          <i class="layui-badge fly-badge-vip" v-if="article.vip > 0">VIP{{article.vip}}</i>
        </router-link>
        <span>{{article.crateTime}}</span>
      </div>
      <div class="detail-hits" id="LAY_jieAdmin" data-id="123">
        <span style="padding-right: 10px; color: #FF7200">悬赏：{{article.learnCoin}}学币</span>
        <span class="layui-btn layui-btn-xs jie-admin" type="edit" v-if="article.uid === userId"><router-link
          :to="`/write/${article.artId}`">编辑此贴</router-link></span>
      </div>
    </div>
    <mavon-editor
      :value="article.content"
      :subfield="false"
      defaultOpen="preview"
      :toolbarsFlag="false"
      :editable="false"
      :ishljs="true"
      :boxShadow="false"
      previewBackground="#ffffff"
      codeStyle="atelier-cave-dark"
      style="border: none"></mavon-editor>
    <div class="article-report" v-if="!isSuper && article.uId !== userId">
      <el-link class="report" @click="toReport(null)" :underline="false">举报</el-link>
    </div>
    <div class="fly-panel detail-box" id="flyReply">
      <fieldset class="layui-elem-field layui-field-title" style="text-align: center;">
        <legend>回帖</legend>
      </fieldset>

      <ul class="jieda" id="jieda">
        <li :id="'comment'+comment.artCId" data-id="111" class="jieda-daan" v-for="(comment,index) in comments"
            :key="comment.artCId">
          <a name="item-1"></a>
          <div @mouseover="showReport=comment.artCId" @mouseout="showReport = 0">
            <div class="detail-about detail-about-reply">
              <router-link :to="'/userPage/'+comment.uid" class="fly-avatar">
                <img :src="$fileURL+comment.avatar" :alt="comment.nickname">
              </router-link>
              <div class="fly-detail-user">
                <router-link :to="'/userPage/'+comment.uid" class="fly-link">
                  <cite>{{comment.nickname}}</cite>
                  <i class="layui-badge fly-badge-vip" v-if="comment.vip!==0">VIP{{comment.vip}}</i>
                </router-link>
                <span v-if="comment.uid===article.uid">(楼主)</span>
              </div>

              <div class="detail-hits">
                <span>{{$getFriendlyTime(comment.createTime)}}</span>
              </div>

              <i class="iconfont icon-caina" title="最佳答案" v-if="comment.isTake"></i>
            </div>
            <div class="detail-body jieda-body photos">
              <div class="ql-editor" v-html="comment.content"></div>
            </div>
            <div class="jieda-reply">
									<span @click="saveArtComZan(index,-1,comment.artCId)" class="jieda-zan"
                        :class="{'zanok':comZan.includes(comment.artCId)}" type="zan">
										<i class="iconfont icon-zan"></i>
										<em>{{comment.hitsZan}}</em>
									</span>
              <span type="reply" @click="toComment(article.artId,comment.artCId)">
										<i class="iconfont icon-svgmoban53"></i>
										回复
									</span>
              <div class="jieda-admin">
                <span type="edit" v-if="comment.uid===userId"
                      @click="toEditComment(comment.artCId,comment.content)">编辑</span>
                <span type="del" v-if="comment.uid===userId || isSuper"
                      @click="delComment(comment.artCId,comments.uId)">删除</span>

              </div>
              <span class="jieda-accept" @click="takeComment(comment.artCId,comment.uid,index,-1)" type="accept"
                    v-if="!article.isOver">采纳</span>
              <span class="jieda-accept" type="accept" @click="comment.isShow=!comment.isShow"
                    v-if="comment.children.length>0">查看全部回复({{comment.children.length}})</span>
              <div style="float: right" v-show="!isSuper && comment.uid !== userId && comment.artCId === showReport">
                <el-link :underline="false" @click="toReport(showReport)">举报</el-link>
              </div>
            </div>
          </div>
          <transition name="el-zoom-in-top">
            <ul class="jieda subReply" v-show="comment.isShow">
              <li :id="'comment'+sub.artCId" data-id="111" class="jieda-daan" v-for="(sub,subIndex) in comment.children"
                  :key="sub.artCId">

                <div @mouseover="showReport=sub.artCId" @mouseout="showReport = 0">
                  <div class="detail-about detail-about-reply">
                    <router-link :to="'/userPage/'+sub.uid" class="fly-avatar">
                      <img :src="$fileURL+sub.avatar" :alt="sub.nickname">
                    </router-link>
                    <div class="fly-detail-user">
                      <router-link :to="'/userPage/'+sub.uid" class="fly-link">
                        <cite>{{sub.nickname}}</cite>
                        <i class="layui-badge fly-badge-vip" v-if="comment.vip!==0">VIP{{comment.vip}}</i>
                      </router-link>
                      <span v-if="sub.uid===article.uid">(楼主)</span>
                      <span>  回复了  {{getReplyName(index,sub.pid)}}</span>
                    </div>

                    <div class="detail-hits">
                      <span>{{$getFriendlyTime(sub.createTime)}}</span>
                    </div>

                    <i class="iconfont icon-caina" title="最佳答案" v-if="sub.isTake"></i>
                  </div>
                  <div class="detail-body jieda-body photos">
                    <div class="ql-editor" v-html="sub.content"></div>
                  </div>
                  <div class="jieda-reply">
									<span @click="saveArtComZan(index,subIndex,sub.artCId)" class="jieda-zan"
                        :class="{'zanok':comZan.includes(sub.artCId)}" type="zan">
										<i class="iconfont icon-zan"></i>
										<em>{{sub.hitsZan}}</em>
									</span>
                    <span type="reply" @click="toComment(article.artId,sub.artCId)">
										<i class="iconfont icon-svgmoban53"></i>
										回复
									</span>
                    <div class="jieda-admin">
                      <span type="edit" v-if="sub.uid===userId" @click="toEditComment(sub.artCId,sub.content)">编辑</span>
                      <span type="del" v-if="sub.uid===userId || isSuper"
                            @click="delComment(sub.artCId,sub.uId)">删除</span>
                    </div>
                    <span @click="takeComment(sub.artCId,sub.uid,index,subIndex)" class="jieda-accept" type="accept"
                          v-if="!article.isOver">采纳</span>
                    <div style="float: right" v-show="!isSuper && sub.uid !== userId && sub.artCId === showReport">
                      <el-link :underline="false" @click="toReport(showReport)">举报</el-link>
                    </div>
                  </div>
                </div>
              </li>
            </ul>
          </transition>
        </li>
        <li class="fly-none" v-if="comments.length===0">消灭零回复</li>
      </ul>
    </div>
    <comment-editor ref="comEditor" :comment-info="commentInfo" @refreshComment="refreshComment"></comment-editor>
    <collect-article ref="collect" :art-id="parseInt(article.artId)" @changeCollect="changeColState"></collect-article>

    <el-dialog title="置顶时间" :visible.sync="topTimeVisible">
      <el-date-picker
        v-model="topTime"
        align="right"
        type="date"
        placeholder="选择日期"
        value-format="yyyy-MM-dd HH:mm"
        :picker-options="pickerOptions">
      </el-date-picker>
      <div slot="footer" class="dialog-footer">
        <el-button @click="topTimeVisible = false">取 消</el-button>
        <el-button type="primary" @click="changeTopState(1)">确 定</el-button>
      </div>
    </el-dialog>
  </div>

</template>

<script>
  import CollectArticle from "../../../components/article/CollectArticle"
  import 'element-ui/lib/theme-chalk/base.css'
  import CommentEditor from "../../../components/comment/CommentEditor"

  export default {
    name: "Detail",
    data() {
      return {
        userId: this.$store.getters.getUserId || null,
        editorShow: false,
        article: {},
        comments: [],
        tags: [],
        tagType: ["", "success", "danger"],
        artZan: false,
        comZan: [],
        commentInfo: {},
        fId: null,  //收藏ID
        startTime: new Date().getTime(),
        isSuper: false,
        showReport: 0,
        topTimeVisible: false,
        topTime: '',
        reportInfo: {
          type: 0,
          tid: 0,
          reason: '',
          uid: this.$store.getters.getUserId
        },
        pickerOptions: {
          disabledDate(time) {
            return time.getTime() < Date.now();
          },
          shortcuts: [{
            text: '一周',
            onClick(picker) {
              const date = new Date();
              date.setTime(date.getTime() + 3600 * 1000 * 24 * 7);
              picker.$emit('pick', new Date());
            }
          }, {
            text: '一个月',
            onClick(picker) {
              const date = new Date();
              date.setTime(date.getTime() + 3600 * 1000 * 24 * 30);
              picker.$emit('pick', date);
            }
          }, {
            text: '一年',
            onClick(picker) {
              const date = new Date();
              date.setTime(date.getTime() + 3600 * 1000 * 24 * 365);
              picker.$emit('pick', date);
            }
          }]
        }
      }
    },
    created() {
      this.getArticle()
      this.getComments()
      if (Object.keys(this.$store.state.userInfo).length !== 0) {
        this.getArtZan()
        this.getCommentZanByMe()
        this.judgeIsCollect()
      }
      for (let i = 0; i < 20; i++) {
        let index = Math.floor(Math.random() * 10) % 1
        console.log(index)
      }
    },
    mounted() {
      console.log('test')
      this.canOperate()
    },
    beforeDestroy() {
      let readTime = (new Date().getTime() - this.startTime) * 1.618 / 60000
      readTime = Math.floor(readTime)
      let rating = 0
      let timeRating = readTime > 10 ? 10 : readTime
      if (this.fId != null) {
        //搜藏
        rating = 10
      } else if (this.artZan || Object.keys(this.commentInfo).length !== 0) {
        //点赞或评论
        rating = 9
      }
      if (this.userId != null && (readTime !== 0 || rating !== 0))
        this.saveUserBehaviorRating(Math.max(rating, timeRating))
    },
    methods: {
      /**
       *去举报评论具体位置
       */
      toReportComment() {
        //从路由参数中获取文章评论ID
        let artCId = this.$route.query.artCId
        if (artCId != null) {
          let isDel = true
          artCId = parseInt(artCId)
          //找到举报文章的具体位置
          for (let i in this.comments) {
            if (this.comments[i].artCId === artCId) isDel = false
            for (let j in this.comments[i].children) {
              if (this.comments[i].children[j].artCId === artCId) {
                //若该文章评论ID评论对象为评论，则展开当前父评论下的所有子评论
                this.comments[i].isShow = true
                isDel = false
              }
            }
          }
          //若没有找到
          if (isDel) {
            this.$message.warning('该评论已删除')
            return this.$router.replace(this.$route.path)
          }
          //跳转到具体位置
          this.$nextTick(() => {
            const id = '#comment' + artCId
            document.querySelector(id).scrollIntoView({behavior: "smooth", block: "start", inline: "start"})
          })
        }
      },
      /**
       *判断当前用户是否具备管理文章权限
       */
      async canOperate() {
        //判断用户是否有角色
        const roleId = this.$store.state.userInfo.roleId
        if (roleId == null) return
        if (roleId === 0) {
          return this.isSuper = true
        }
        //先从session中获取
        const uid = window.sessionStorage.getItem("articleManager")
        if (uid == null) {
          //访问后端判断
          const res = await this.$http.get('rights/article/' + roleId)
          if (res.code === 200) {
            //用操作权限
            this.isSuper = true
            //将数据保存
            window.sessionStorage.setItem("articleManager", this.$store.state.userInfo.uId)
          } else {
            this.isSuper = false
          }
        } else {
          this.isSuper = true
        }

      },
      /**
       * 保存用户行为数据
       * param:
       *  rating: 用户评分
       */
      async saveUserBehaviorRating(rating) {
        await this.$http.post('user/behavior', {uId: this.userId, artId: this.article.artId, rating})
      },
      /**
       * 改变收藏状态
       * param:
       *  fId: 收藏文章信息主键ID
       */
      changeColState(fId) {
        this.fId = fId;
      },
      /**
       * 收藏或取消收藏文章
       */
      async collectArticle() {
        //判断文章是否收藏
        if (this.fId == null) {
          //打开收藏文件Dialog
          if (this.userId == null) {
            return this.$router.push('/login')
          }
          this.$refs.collect.collectShow = true
        } else {
          //取消收藏
          const res = await this.$http.delete('articleFavorite/' + this.fId)
          if (res.code === 200) {
            this.fId = null
          } else {
            this.$message.error("取消失败请重试")
          }
        }

      },
      /**
       * 删除文章
       */
      toDelArticle() {
        //若过文章属于当前用户，提示通过后直接删除
        if (this.article.uId === this.userId) {
          this.$confirm('此操作将永久删除该文章, 是否继续?', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }).then(() => {
            this.delArticle(null)
          })
        } else {
          this.$prompt('请输入原因', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消'
          }).then(({value}) => {
            if (value.trim()) {
              this.delArticle(value.trim())
            } else {
              return this.$message('您什么都没有输入')
            }
          });
        }
      },
      /**
       * 删除文章
       * param:
       *  msg: 文章删除理由,默认值为null
       */
      async delArticle(msg = null) {
        const res = await this.$http.delete('article/' + this.article.artId, {params: {msg}})
        if (res.code === 200) {
          this.$message.success("删除成功")
          return this.$router.back()
        }
        this.$message.error("删除失败")
      },
      /**
       * 去评论
       * param:
       *  artId:文章ID
       *  pId:父评论ID，默认为0，即评论文章
       */
      toComment(artId, pId = 0) {
        if (pId === 0)
          document.querySelector('#jieda').scrollIntoView({behavior: "smooth", block: "start", inline: "start"})
        let uId = this.userId
        if (uId == null)
          return this.$router.push("/login")
        this.commentInfo = {artId, pId, uId, content: ''}
        //打开评论界面
        this.$refs.comEditor.editorShow = true
      },
      /**
       * 去修改评论
       * @param artCId {Number} 评论ID
       * @param content {String} 原评论内容
       */
      toEditComment(artCId, content) {
        this.commentInfo = {artCId, content}
        this.$refs.comEditor.editorShow = true
      },
      /**
       * 删除评论
       * @param artCId {Number} 评论ID
       * @param uid {Number}  评论所属用户ID
       */
      async delComment(artCId, uid) {
        //管理员删除，且非本人的评论
        if (this.isSuper && uid !== this.userId) {
          this.$prompt('请输入原因', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消'
          }).then(async ({value}) => {
            if (value.trim()) {
              const res = await this.$http.delete('articleComment/' + artCId,
                {params: {msg: value.trim()}})
              if (res.code === 200) {
                this.refreshComment(200)
              } else {
                this.$message.error("删除失败，请重试")
              }
            } else {
              return this.$message('您什么都没有输入')
            }
          });
        } else {
          const res = await this.$http.delete('articleComment/' + artCId)
          if (res.code === 200) {
            this.refreshComment(200)
          } else {
            this.$message.error("删除失败，请重试")
          }
        }
      },
      /**
       * 刷新评论
       * @param code {Number} 评论增加、修改或删除之后的返回状态码
       */
      refreshComment(code) {
        if (code === 200) {
          this.getComments()
          this.article.hitsComment++
        } else {
          this.$message.error("评论失败")
        }
      },
      /**
       * 采纳评论
       * @param artCId {Number} 评论ID
       * @param cuid {Number} 评论所属用户ID
       * @param index {Number}  该评论在评论数组中的索引值
       * @param subIndex {Number} 若为子评论，子评论的索引值
       * @returns {Promise<void>}
       */
      async takeComment(artCId, cuid, index, subIndex) {
        let uid = this.userId
        if (uid == null) {
          await this.$router.push("/login")
        }
        const res = await this.$http.put(`articleComment/${artCId}/take/${cuid}/art/${this.article.artId}/user/${uid}`)
        if (res.code === 200) {
          if (subIndex === -1) {
            this.comments[index].isTake = 1
          } else {
            this.comments[index].children[subIndex].isTake = 1
          }
          this.article.isOver = 1
          this.$store.commit('decreaseLearnCoin', this.article.learnCoin)
        } else {
          this.$message.error("失败")
        }

      },
      /**
       * 获取文章
       * @returns {Promise<void>}
       */
      async getArticle() {
        let uid = this.userId || 0
        const res = await this.$http.get(`article/${this.$route.params.artId}/user/${uid}`)
        if (res.code === 200) {
          this.article = res.data
          this.tags = this.article.tag.split(',')
          console.log(res.data)
        }
      },
      /**
       * 判断该文章是否收藏
       * @returns {Promise<void>}
       */
      async judgeIsCollect() {
        let uid = this.userId || 0
        const res = await this.$http.get(`articleFavorite/${this.$route.params.artId}/user/${uid}`)
        if (res.code === 200) {
          this.fId = res.data
        }
      },
      /**
       * 获取该文章的所有评论
       * @returns {Promise<void>}
       */
      async getComments() {
        const res = await this.$http.get('articleComment/' + this.$route.params.artId)
        if (res.code === 200) {
          if (res.data.length > 0)
            for (let i = 0; i < res.data.length; i++)
              res.data[i].isShow = false
          this.comments = res.data
          this.toReportComment()
        }
      },
      /**
       * 判断当前用户是否赞过该文章
       * @returns {Promise<void>}
       */
      async getArtZan() {
        const res = await this.$http.get(`articleZan/${this.$route.params.artId}/user/${this.userId}`)
        this.artZan = res.data.zan;

      },
      /**
       * 获取用户对当前文章所有的点赞的评论ID
       * @returns {Promise<void>}
       */
      async getCommentZanByMe() {
        const res = await this.$http.get(`articleCommentZan/${this.$route.params.artId}/user/${this.userId}`)
        this.comZan = res.data || [];
        console.log('zan', this.comZan)
      },
      /**
       * 获取回复评论人的昵称
       * @param index
       * @param pid
       * @returns {string}
       */
      getReplyName(index, pid) {
        if (this.comments[index].artCId === pid) {
          return this.comments[index].nickname
        } else {
          for (let item of this.comments[index].children) {
            if (item.artCId === pid) {
              return item.nickname
            }
          }
        }
      },
      /**
       * 保存用户点赞行为
       * @returns {Promise<Route>}
       */
      async saveArtZan() {
        if (this.userId == null) {
          return this.$router.push("/login")
        }
        if (this.artZan) {
          this.$message.warning("您已经赞过")
        } else {
          const res = await this.$http.post('articleZan/save'
            , {artId: this.$route.params.artId, uId: this.userId})
          if (res.code === 200) {
            this.artZan = true
            this.article.hitsZan += 1
          } else {
            this.$message.error("点赞失败，请重试")
          }
        }
      },
      /**
       * 保存文章点赞行为
       * @param index
       * @param subIndex
       * @param artCId
       * @returns {Promise<Route>}
       */
      async saveArtComZan(index, subIndex, artCId) {
        if (this.userId == null) {
          return this.$router.push("/login")
        }
        if (this.comZan.includes(artCId)) {
          this.$message.warning("您已经赞过")
        } else {
          const res = await this.$http.post('articleCommentZan/save'
            , {artCId: artCId, uId: this.userId})
          if (res.code === 200) {
            this.comZan = [...this.comZan, artCId]
            if (subIndex === -1) {
              this.comments[index].hitsZan++
            } else {
              this.comments[index].children[subIndex].hitsZan++
            }
          } else {
            this.$message.error("点赞失败，请重试")
          }
        }
      },
      /**
       * 置顶或者取消置顶
       * @returns {boolean}
       */
      saveOrCancelTop() {
        if (this.canTop) {
          return this.topTimeVisible = true
        }
        this.changeTopState(0)
      },
      async changeTopState(top) {
        let data = {artId: this.article.artId, uId: this.article.uid, title: this.article.title, top}
        if (top === 1) {
          data.topTime = this.topTime
        }
        const res = await this.$http.put('article/top', data)
        if (res.code === 200) {
          this.topTimeVisible = false
          this.$message.success("设置成功")
          this.article.top = top
        } else {
          this.$message.error("操作失败")
        }
      },
      /**
       * 举报文章或者评论
       * @param artCId 评论ID，不传表示举报文章
       */
      toReport(artCId) {
        if (this.userId == null) return this.$router.push('/login')
        if (artCId == null) {
          //举报文章
          this.reportInfo.type = 1
          this.reportInfo.tId = this.article.artId
        } else {
          //举报评论
          this.reportInfo.type = 2
          this.reportInfo.tid = artCId
        }
        this.$prompt('请输入原因', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消'
        }).then(async ({value}) => {
          if (value.trim()) {
            this.reportInfo.reason = value.trim()
            const res = await this.$http.post('report', this.reportInfo)
            if (res.code === 200) {
              this.$message.success("举报成功，等待审核")
            } else
              this.$message.error("举报失败，请重试")
          } else {
            return this.$message('您什么都没有输入')
          }
        });
      }
    },
    computed: {
      /**
       * 判断是个置顶
       * @returns {boolean}
       */
      canTop() {
        if (this.article.top === 0) {
          return true
        }
        return Date.parse(this.article.tExpiryTime) > new Date().getTime();
      }
    },
    components: {
      CommentEditor, CollectArticle
    }
  }
</script>

<style lang="less" scoped>
  .mezan {
    color: #1E9FFF;
  }

  .detail-body img {
    max-height: 100px !important;
  }

  .subReply {
    margin-left: 30px;
  }

  .font-gray {
    background-color: #ccc;
  }

  .article-report {
    width: 100%;
    display: flex;
    flex-direction: row-reverse;

    .report {
      margin-right: 20px;
    }
  }
</style>