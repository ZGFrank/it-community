package com.zgf.itc.service.impl;

import com.zgf.itc.entity.*;
import com.zgf.itc.mapper.ArticleZanMapper;
import com.zgf.itc.service.ArticleService;
import com.zgf.itc.service.ArticleZanService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zgf.itc.service.MessageService;
import com.zgf.itc.service.UserService;
import net.sf.jsqlparser.statement.Commit;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author ZGF
 */
@Service
public class ArticleZanServiceImpl extends ServiceImpl<ArticleZanMapper, ArticleZan> implements ArticleZanService {

    @Resource
    private ArticleService articleService;
    @Resource
    private UserService us;
    @Resource
    private MessageService ms;

    @Override
    public boolean currentArtZanByUId(Integer artId, Integer uId) {
        return this.query()
                .eq("art_id", artId)
                .eq("u_id", uId)
                .count() != 0;
    }

    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public boolean saveArticleZan(ArticleZan zan) {
        zan.setCreateTime(LocalDateTime.now());
        //保存点赞记录
        if (this.save(zan)) {
            Message message = new Message();
            Article article = this.articleService.query().select("title","u_id").eq("art_id", zan.getArtId()).one();
            message.setRecvId(article.getuId());
            message.setSendId(zan.getuId());
            message.setCreateTime(LocalDateTime.now());
            User sendUser = this.us.query().select("nickname").eq("u_id", zan.getuId()).one();
            message.setContent("用户<a href='/#/userPage/" + zan.getuId() + "'><cite>" + sendUser.getNickname()
                    +"</cite></a><span class='remind'>点赞</span>了您的文章<a href='/#/articleDetail/"+zan.getArtId()
                    +"'><cite>《"+article.getTitle()+"》</cite></a>");
            this.ms.save(message);
            //文章点赞数+1
            return this.articleService.update()
                    .setSql("hits_zan = hits_zan + 1")
                    .eq("art_id",zan.getArtId())
                    .update();
        }
        return false;
    }
}
