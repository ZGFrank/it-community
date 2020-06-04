package com.zgf.itc.service.impl;

import com.zgf.itc.entity.Article;
import com.zgf.itc.entity.ArticleComment;
import com.zgf.itc.entity.Message;
import com.zgf.itc.entity.User;
import com.zgf.itc.mapper.ArticleCommentMapper;
import com.zgf.itc.service.ArticleCommentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zgf.itc.service.ArticleService;
import com.zgf.itc.service.MessageService;
import com.zgf.itc.service.UserService;
import com.zgf.itc.vo.ArtComTreeNode;
import com.zgf.itc.vo.ArticleCommentNode;
import com.zgf.itc.vo.RecentComment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.security.auth.callback.Callback;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 文章评论表 服务实现类
 * </p>
 *
 * @author ZGF
 */
@Service
public class ArticleCommentServiceImpl extends ServiceImpl<ArticleCommentMapper, ArticleComment> implements ArticleCommentService {

    @Resource
    private ArticleService articleService;
    @Resource
    private UserService userService;
    @Resource
    private MessageService ms;

    @Override
    public List<ArtComTreeNode> getComments(Integer artId) {
        Integer pId = 0;
        return recGetTree(artId, pId, 1);
    }

    /**
     * 通过递归调用获得评论树
     *
     * @param artId
     * @param pId
     * @return
     */
    private List<ArtComTreeNode> recGetTree(Integer artId, Integer pId, Integer deep) {
        List<ArticleCommentNode> nodes = this.baseMapper.getComments(artId, pId);
        List<ArtComTreeNode> tnodes = null;
        if (nodes == null) {
            return null;
        }
        tnodes = new ArrayList<>();
        if (deep == 1) {
            for (ArticleCommentNode node : nodes) {
                ArtComTreeNode artComTreeNode = new ArtComTreeNode(node.getUId(), node.getNickname()
                        , node.getAvatar(), node.getVip(), node.getArtCId(), node.getPId(), node.getContent()
                        , node.getCreateTime(), node.getIsTake(), node.getHitsZan(), recGetTree(artId, node.getArtCId(), deep + 1));
                tnodes.add(artComTreeNode);
            }
        } else {
            for (ArticleCommentNode node : nodes) {
                ArtComTreeNode artComTreeNode = new ArtComTreeNode(node.getUId(), node.getNickname()
                        , node.getAvatar(), node.getVip(), node.getArtCId(), node.getPId(), node.getContent()
                        , node.getCreateTime(), node.getIsTake(), node.getHitsZan(), null);
                tnodes.add(artComTreeNode);
                List<ArtComTreeNode> deeps = recGetTree(artId, node.getArtCId(), deep + 1);
                if (deeps != null) {
                    tnodes.addAll(deeps);
                }

            }
        }

        return tnodes;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean saveComment(ArticleComment comment) {
        try {
            comment.setCreateTime(LocalDateTime.now());
            if (this.save(comment)) {
                //发送消息
                Message message = new Message();
                message.setCreateTime(LocalDateTime.now());
                message.setSendId(comment.getuId());
                Article article = this.articleService.query().select("title", "u_id").eq("art_id", comment.getArtId()).one();
                User one = this.userService.query().select("nickname").eq("u_id", comment.getuId()).one();
                if (comment.getpId() == 0) {
                    if (!comment.getuId().equals(article.getuId())) {
                        //评论文章
                        message.setContent("用户<a href='/#/userPage/" + comment.getuId() + "'><cite>" + one.getNickname()
                                + "</cite></a><span class='remind'>评论</span>了您的文章<a href='/#/articleDetail/"
                                + comment.getArtId() + "'><cite>《" + article.getTitle() + "》</cite></a>");
                        message.setRecvId(article.getuId());
                    }
                } else {
                    //评论评论
                    ArticleComment fatherComment = this.query().select("u_id").eq("art_c_id", comment.getpId()).one();
                    if (!comment.getuId().equals(fatherComment.getuId())) {
                        message.setContent("用户<a href='/#/userPage/" + comment.getuId() + "'><cite>" + one.getNickname()
                                + "</cite></a>在文章<a href='/#/articleDetail/" + comment.getArtId() + "'><cite>《"
                                + article.getTitle() + "》</cite></a>下<span class='remind'>评论</span>了您的评论");
                        message.setRecvId(fatherComment.getuId());
                    }
                }
                this.ms.save(message);
                //文章评论数+1
                return this.articleService.update().setSql("hits_comment = hits_comment+1").eq("art_id", comment.getArtId()).update();
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    //添加数据库事务注解，保存操作一致性
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean take(Integer artCId, Integer cUid, Integer artId, Integer uId) {
        try {
            //更新评论
            this.update().set("is_take", 1).eq("art_c_id", artCId).update();
            //查询文章
            Article article = this.articleService.getById(artId);
            //获取奖励数额
            Integer coin = article.getLearnCoin();
            //文章状态便完结
            this.articleService.update().set("is_over", 1).eq("art_id", artId).update();
            //当前用户学币-coin
            this.userService.update().setSql("learn_coin = learn_coin - " + coin).eq("u_id", uId).update();
            //评论所属用户学币+coin
            this.userService.update().setSql("learn_coin = learn_coin + " + coin).eq("u_id", cUid).update();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    @Override
    public List<RecentComment> getRecentComment(Integer uid, Integer limit) {
        return this.baseMapper.getRecentComment(uid, limit);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deleteById(Integer artCId, String msg) {
        try {
            if (msg == null) {
                return this.removeById(artCId);
            }
            ArticleComment comment = this.query().select("u_id","art_id").eq("art_c_id", artCId).one();
            Article article = this.articleService.query().select("title").eq("art_id", comment.getArtId()).one();
            Message message = new Message();
            message.setCreateTime(LocalDateTime.now());
            message.setRecvId(comment.getuId());
            message.setContent("<strong>管理员</strong>因“"+msg+"”<span class='remind'>删除</span>您在文章<a href='/#/articleDetail/"
                    +comment.getArtId()+"'><cite>《"+article.getTitle()+"》</cite></a>下的评论");
            this.removeById(artCId);
            this.ms.save(message);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
