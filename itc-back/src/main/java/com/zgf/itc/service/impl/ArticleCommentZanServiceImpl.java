package com.zgf.itc.service.impl;

import com.alibaba.druid.sql.visitor.functions.If;
import com.zgf.itc.entity.*;
import com.zgf.itc.mapper.ArticleCommentZanMapper;
import com.zgf.itc.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author ZGF
 */
@Service
public class ArticleCommentZanServiceImpl extends ServiceImpl<ArticleCommentZanMapper, ArticleCommentZan> implements ArticleCommentZanService {

    @Resource
    private ArticleCommentService acs;
    @Resource
    private ArticleService as;
    @Resource
    private UserService us;
    @Resource
    private MessageService ms;

    @Override
    public List<Integer> currentArtComZanByUId(Integer artId, Integer uId) {
        //获取当前文章下的所有评论
        List<ArticleComment> ac = acs.query().eq("art_id", artId).list();
        if (ac == null || ac.size() == 0) {
            return null;
        }
        //获取评论ID
        List<Integer> artCIds = ac.stream().map(ArticleComment::getArtCId).collect(Collectors.toList());
        //查询评论ID为当前文章所有评论ID且点赞用户为当前用户的评论点赞数据
        List<ArticleCommentZan> list = this.query()
                .in("art_c_id", artCIds)
                .eq("u_id", uId)
                .list();
        if (list == null || list.size() == 0) {
            return null;
        }
        return list.stream()
                .map(ArticleCommentZan::getArtCId)
                .collect(Collectors.toList());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean saveArtComZan(ArticleCommentZan zan) {
        zan.setCreateTime(LocalDateTime.now());
        //保存评论点赞记录
        if (this.save(zan)) {
            Message message = new Message();
            ArticleComment comment = this.acs.query().select("art_id", "u_id").eq("art_c_id", zan.getArtCId()).one();
            message.setRecvId(comment.getuId());
            message.setSendId(zan.getuId());
            message.setCreateTime(LocalDateTime.now());
            Article article = this.as.query().select("title").eq("art_id", comment.getArtId()).one();
            User sendUser = this.us.query().select("nickname").eq("u_id", zan.getuId()).one();
            message.setContent("用户<a href='/#/userPage/" + zan.getuId() + "'><cite>" + sendUser.getNickname()
                    + "</cite></a>在文章<a href='/#/articleDetail/" + comment.getArtId() + "'><cite>《"
                    + article.getTitle() + "》</cite></a>下<span class='remind'>点赞</span>了您的评论");
            this.ms.save(message);
            //评论点赞数+1
            return this.acs.update()
                    .setSql("hits_zan = hits_zan + 1")
                    .eq("art_c_id", zan.getArtCId())
                    .update();
        }
        return false;
    }
}
