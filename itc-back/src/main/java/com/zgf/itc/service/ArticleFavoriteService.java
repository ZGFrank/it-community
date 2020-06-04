package com.zgf.itc.service;

import com.zgf.itc.entity.ArticleFavorite;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zgf.itc.vo.MyFavorite;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ZGF
 */
public interface ArticleFavoriteService extends IService<ArticleFavorite> {

    boolean saveFavorite(ArticleFavorite favorite);

    Integer getFidByUserAndArtId(Integer artId, Integer uId);

    List<MyFavorite> getFavoritesByUid(Integer uId);
}
