package com.zgf.itc.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zgf.itc.entity.Exchange;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author ZGF
 */
public interface ExchangeMapper extends BaseMapper<Exchange> {

    IPage<Exchange> getAll(Page page, Integer uId, Integer state);
}
