package com.zgf.itc.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zgf.itc.entity.News;
import com.zgf.itc.mapper.NewsMapper;
import com.zgf.itc.service.NewsService;
import com.zgf.itc.utils.QueryCondition;
import com.zgf.itc.vo.NewsVo;
import com.zgf.itc.vo.ShowData;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ZGF
 */
@Service
public class NewsServiceImpl extends ServiceImpl<NewsMapper, News> implements NewsService {

    @Override
    public List<News> getFrontNews() {
        return this.query()
                .ge("expiry_time", LocalDateTime.now()).orderByDesc("level","create_time")
                .list();
    }

    @Override
    public ShowData getBackAll(QueryCondition qc) {
        IPage<NewsVo> list = this.baseMapper.getBackAll(new Page(qc.getCurrentPage(),qc.getPageSize()));
        return ShowData.builder().total(list.getTotal()).data(list.getRecords()).build();
    }

    @Override
    public void insert(News entity) {
        entity.setCreateTime(LocalDateTime.now());
        this.save(entity);
    }
}
