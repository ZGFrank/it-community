package com.zgf.itc.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zgf.itc.entity.Article;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zgf.itc.utils.QueryCondition;
import com.zgf.itc.vo.ArticleDetail;
import com.zgf.itc.vo.ArticleItem;
import com.zgf.itc.vo.FavoriteArticleItem;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author ZGF
 */
public interface ArticleMapper extends BaseMapper<Article> {

    List<ArticleItem> selectTopArticleItem();

    IPage<Article> getArticlesMyUid(Page page, Integer uId);

    ArticleDetail getDetailById(Integer artId);

    List<FavoriteArticleItem> getArticlesByDirId(Integer dId);

    List<Article> getRecentArticle(Integer uid, Integer limit);

    IPage<ArticleItem> queryNormal(Page page,Integer category, Integer type,Integer condition);

    List<Article> getWeekTopArticle();


    IPage<Article> loadForBack(Page page, String title, Integer category, Integer isOver, Integer sort);

    List<ArticleItem> selectArticleItemByIds(List<Integer> artIds);

    List<String> getArticleTagsByIds(List<Integer> artIds);

    Article getArticleByCommentId(Integer tid);

    List<ArticleItem> selectLimitAndNotIn(int limit, @Param("list") List<Integer> artIds);
}
