package com.zgf.itc.service;

import com.zgf.itc.entity.News;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zgf.itc.utils.QueryCondition;
import com.zgf.itc.vo.ShowData;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ZGF
 */
public interface NewsService extends IService<News> {

    List<News> getFrontNews();

    ShowData getBackAll(QueryCondition qc);

    void insert(News entity);
}
