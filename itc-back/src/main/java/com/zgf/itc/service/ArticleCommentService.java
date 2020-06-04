package com.zgf.itc.service;

import com.zgf.itc.entity.ArticleComment;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zgf.itc.vo.ArtComTreeNode;
import com.zgf.itc.vo.ArticleCommentNode;
import com.zgf.itc.vo.RecentComment;

import java.util.List;

/**
 * <p>
 * 文章评论表 服务类
 * </p>
 *
 * @author ZGF
 */
public interface ArticleCommentService extends IService<ArticleComment> {

    List<ArtComTreeNode> getComments(Integer artId);

    boolean saveComment(ArticleComment comment);

    /**
     * 文章评论采纳
     * @param artCId
     * @param cUid
     * @param artId
     * @param uId
     * @return
     */
    boolean take(Integer artCId, Integer cUid, Integer artId, Integer uId);

    /**
     * 获取最近的评论用于主页展示
     * @param uid
     * @param limit
     * @return
     */
    List<RecentComment> getRecentComment(Integer uid, Integer limit);

    boolean deleteById(Integer artCId, String msg);
}
