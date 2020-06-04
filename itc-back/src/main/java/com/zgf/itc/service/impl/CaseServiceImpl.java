package com.zgf.itc.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zgf.itc.entity.Case;
import com.zgf.itc.entity.Message;
import com.zgf.itc.mapper.CaseMapper;
import com.zgf.itc.service.CaseService;
import com.zgf.itc.service.MessageService;
import com.zgf.itc.utils.QueryCondition;
import com.zgf.itc.vo.CaseVo;
import com.zgf.itc.vo.ShowData;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ZGF
 */
@Service
public class CaseServiceImpl extends ServiceImpl<CaseMapper, Case> implements CaseService {

    @Resource
    private MessageService ms;

    @Override
    public boolean saveCase(Case entity) {
        entity.setCreateTime(LocalDateTime.now());
        return this.save(entity);
    }

    @Override
    public ShowData getAll(QueryCondition query) {
        IPage<CaseVo> list = this.baseMapper.getAll(new Page(query.getCurrentPage(), query.getPageSize())
                , query.getUId()
                , query.getYearSpace()
                , query.getOrderByTime()
                , query.getState());
        return ShowData
                .builder()
                .total(list.getTotal())
                .data(list.getRecords())
                .build();
    }

    @Override
    public ShowData getBack(QueryCondition query) {
        IPage<CaseVo> list = this.baseMapper.getBack(new Page(query.getCurrentPage(), query.getPageSize()),query.getState());
        return ShowData
                .builder()
                .total(list.getTotal())
                .data(list.getRecords())
                .build();
    }

    @Override
    public boolean updateCase(Case entity) {
        if (this.updateById(entity)) {
            //判断是否为管理员操作
            if (entity.getMsg() != null) {
                Message message = new Message();
                message.setCreateTime(LocalDateTime.now());
                message.setRecvId(entity.getuId());
                message.setContent("<strong>管理员</strong><span class='remind'>"+entity.getMsg()
                        +"</span>您的案例<span class='miss'>《"+entity.getTitle()+"》</span>");
                this.ms.save(message);
            }
            return true;
        }
        return false;
    }
}
