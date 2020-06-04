package com.zgf.itc.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zgf.itc.entity.Article;
import com.zgf.itc.utils.Pagination;
import com.zgf.itc.utils.QueryCondition;
import com.zgf.itc.vo.ArticleDetail;
import com.zgf.itc.vo.ArticleItem;
import com.zgf.itc.vo.FavoriteArticleItem;
import com.zgf.itc.vo.ShowData;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ZGF
 */
public interface ArticleService extends IService<Article> {

    /**
     * 保存文章
     * @param article
     * @return
     */
    boolean saveArticle(Article article);

    /**
     * 加载置顶文章
     * @return
     */
    List<ArticleItem> loadsTop();

    /**
     * 根据文章ID获取文章，并更新文章浏览数
     * @param artId
     * @param uId
     * @return
     */
    ArticleDetail getArticleById(Integer artId, Integer uId);

    /**
     * 获取我的文章列表，并实现分页
     * @param uId
     * @param pagination
     * @return
     */
    ShowData getArticlesMyUid(Integer uId, Pagination pagination);

    List<FavoriteArticleItem> getArticlesByDirId(Integer dId);

    /**
     * 获取用户最近时间的10条文章
     * @param uid
     * @param limit
     * @return
     */
    List<Article> getRecentArticle(Integer uid,Integer limit);

    ShowData loadsNormal(QueryCondition qc,Integer uid);

    List<Article> getWeekTopArticle();

    ShowData loadForBack(QueryCondition qc);

    boolean updateArticle(Article article);

    boolean removeArticle(Integer artId);

    ShowData search(QueryCondition qc);

    boolean updateTop(Article article);

    boolean deleteArticle(Integer artId, String msg);

    Article getArticleByCommentId(Integer tid);
}
