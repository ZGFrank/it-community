package com.zgf.itc.service;

import com.zgf.itc.entity.ArticleCommentZan;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ZGF
 */
public interface ArticleCommentZanService extends IService<ArticleCommentZan> {

    /**
     * 通过文章ID和用户ID获取，当前用户所赞过的评论
     * @param artId
     * @param uId
     * @return
     */
    List<Integer> currentArtComZanByUId(Integer artId, Integer uId);

    boolean saveArtComZan(ArticleCommentZan zan);
}
