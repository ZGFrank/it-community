package com.zgf.itc.mapper;

import com.zgf.itc.entity.ArticleFavorite;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author ZGF
 */
public interface ArticleFavoriteMapper extends BaseMapper<ArticleFavorite> {

    Integer getAllCollectCount(Integer artId);
}
