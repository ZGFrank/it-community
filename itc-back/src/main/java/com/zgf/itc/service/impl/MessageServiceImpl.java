package com.zgf.itc.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zgf.itc.entity.Message;
import com.zgf.itc.mapper.MessageMapper;
import com.zgf.itc.service.MessageService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ZG
 */
@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message> implements MessageService {

    @Override
    public List<Message> getMessageByRecvId(Integer uId) {
        return this.query().eq("recv_id",uId).orderByDesc("msg_id").list();
    }

    @Override
    public Integer getCountByRecvId(Integer uId) {
        return this.query().eq("recv_id",uId).count();
    }
}
