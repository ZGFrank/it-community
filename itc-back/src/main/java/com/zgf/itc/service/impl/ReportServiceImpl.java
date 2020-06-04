package com.zgf.itc.service.impl;

import com.zgf.itc.entity.*;
import com.zgf.itc.mapper.ReportMapper;
import com.zgf.itc.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author ZGF
 */
@Service
public class ReportServiceImpl extends ServiceImpl<ReportMapper, Report> implements ReportService {

    @Resource
    private MessageService ms;
    @Resource
    private ArticleService as;
    @Resource
    private ArticleCommentService acs;
    @Resource
    private UserService us;

    @Override
    public boolean saveReport(Report entity) {
        entity.setCreateTime(LocalDateTime.now());
        if (this.save(entity)) {
            Message message = new Message();
            message.setCreateTime(LocalDateTime.now());
            User sendUser = this.us.query().select("nickname").eq("u_id", entity.getUid()).one();
            message.setSendId(entity.getUid());
            if (entity.getType() == 1) {
                //举报文章
                //获取所有包含文章管理权限管理员id
                List<Integer> ids = this.us.getUserIdsByRightsId(3);
                //随机选取一个文章管理员发送
                int index = (int) (Math.floor(Math.random() * 10 * ids.size()) % ids.size());
                Article article = this.as.query().select("title").eq("art_id", entity.getTid()).one();
                message.setRecvId(ids.get(index));
                message.setContent("用户<a href='/#/userPage/" + entity.getUid() + "'><cite>" + sendUser.getNickname()
                        + "</cite></a><span class='remind'>举报</span>文章<a href='/#/articleDetail/" + entity.getTid()
                        + "'><cite>《" + article.getTitle() + "》</cite></a> 理由为<strong>“" + entity.getReason() + "”</strong>");
                this.ms.save(message);

            } else if (entity.getType() == 2) {
                //举报评论
                //获取评论对应的文章
                Article article = this.as.getArticleByCommentId(entity.getTid());
                ArticleComment comment = this.acs.getById(entity.getTid());
                //获取所有包含文章管理权限管理员id
                List<Integer> ids = this.us.getUserIdsByRightsId(3);
                //随机选取一个文章管理员发送
                int index = (int) (Math.floor(Math.random() * 10 * ids.size()) % ids.size());
                message.setRecvId(ids.get(index));
                message.setContent("用户<a href='/#/userPage/" + entity.getUid() + "'><cite>" + sendUser.getNickname()
                        + "</cite></a><span class='remind'>举报</span>评论<a href='/#/articleDetail/" + article.getArtId()
                        + "?artCId="+entity.getTid()+"'> <cite>“" + comment.getContent() + "”</cite></a> 理由为“" + entity.getReason() + "”");
                this.ms.save(message);
            }
            return true;
        }
        return false;
    }
}
