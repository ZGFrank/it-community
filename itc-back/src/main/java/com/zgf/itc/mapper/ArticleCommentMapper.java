package com.zgf.itc.mapper;

import com.zgf.itc.entity.ArticleComment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zgf.itc.vo.ArtComTreeNode;
import com.zgf.itc.vo.ArticleCommentNode;
import com.zgf.itc.vo.RecentComment;

import java.util.List;

/**
 * <p>
 * 文章评论表 Mapper 接口
 * </p>
 *
 * @author ZGF
 */
public interface ArticleCommentMapper extends BaseMapper<ArticleComment> {

    List<ArticleCommentNode> getComments(Integer artId, Integer pId);

    ArticleCommentNode getComment(Integer artCId);

    List<RecentComment> getRecentComment(Integer uid, Integer limit);
}
