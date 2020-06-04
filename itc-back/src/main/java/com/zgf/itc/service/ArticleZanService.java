package com.zgf.itc.service;

import com.zgf.itc.entity.ArticleZan;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ZGF
 */
public interface ArticleZanService extends IService<ArticleZan> {

    boolean currentArtZanByUId(Integer artId, Integer uId);

    boolean saveArticleZan(ArticleZan zan);
}
