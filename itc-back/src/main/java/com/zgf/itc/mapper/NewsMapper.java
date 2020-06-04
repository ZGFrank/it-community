package com.zgf.itc.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zgf.itc.entity.News;
import com.zgf.itc.vo.NewsVo;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author ZGF
 */
public interface NewsMapper extends BaseMapper<News> {

    IPage<NewsVo> getBackAll(Page page);
}
