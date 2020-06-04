package com.zgf.itc.service;

import com.zgf.itc.entity.AccessBehavior;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ZGF
 */
public interface AccessBehaviorService extends IService<AccessBehavior> {

    void insert(AccessBehavior entity);
}
