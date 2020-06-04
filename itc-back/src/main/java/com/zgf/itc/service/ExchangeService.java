package com.zgf.itc.service;

import com.zgf.itc.entity.Exchange;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zgf.itc.utils.QueryCondition;
import com.zgf.itc.vo.ShowData;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ZGF
 */
public interface ExchangeService extends IService<Exchange> {

    boolean saveExchange(Exchange entity);

    ShowData getAll(QueryCondition query);

    boolean updateBack(Exchange entity);
}
