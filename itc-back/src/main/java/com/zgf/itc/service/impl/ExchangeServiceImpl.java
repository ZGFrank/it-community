package com.zgf.itc.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zgf.itc.entity.Exchange;
import com.zgf.itc.entity.Message;
import com.zgf.itc.mapper.ExchangeMapper;
import com.zgf.itc.service.ExchangeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zgf.itc.service.MessageService;
import com.zgf.itc.utils.QueryCondition;
import com.zgf.itc.vo.CaseVo;
import com.zgf.itc.vo.ShowData;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author ZGF
 */
@Service
public class ExchangeServiceImpl extends ServiceImpl<ExchangeMapper, Exchange> implements ExchangeService {

    @Resource
    private MessageService ms;

    @Override
    public boolean saveExchange(Exchange entity) {
        entity.setCreateTime(LocalDateTime.now());
        return this.save(entity);
    }

    @Override
    public ShowData getAll(QueryCondition query) {
        IPage<Exchange> list = this.baseMapper.getAll(new Page(query.getCurrentPage(), query.getPageSize())
                , query.getUId()
                , query.getState());
        return ShowData
                .builder()
                .total(list.getTotal())
                .data(list.getRecords())
                .build();
    }

    @Override
    public boolean updateBack(Exchange entity) {
        //如果通过，则设置过期时间为30天
        if (entity.getState() == 1) {
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime of = LocalDateTime.of(LocalDate.ofYearDay(now.getYear(), now.getDayOfYear() + 30), LocalTime.now());
            entity.setExpiryTime(of);
        }
        Message message = new Message();
        message.setCreateTime(LocalDateTime.now());
        message.setRecvId(entity.getuId());
        message.setContent("<strong>管理员</span><span class='remind'>"+entity.getMsg()+"</span>您的需求");
        this.ms.save(message);
        return this.updateById(entity);
    }
}
