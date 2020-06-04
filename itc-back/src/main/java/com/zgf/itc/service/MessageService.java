package com.zgf.itc.service;

import com.zgf.itc.entity.Message;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ZGF
 */
public interface MessageService extends IService<Message> {

    List<Message> getMessageByRecvId(Integer uId);

    Integer getCountByRecvId(Integer uId);
}
