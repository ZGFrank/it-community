package com.zgf.itc.service;

import com.zgf.itc.entity.SignIn;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zgf.itc.vo.SignInBaseInfo;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ZGF
 */
public interface SignInService extends IService<SignIn> {

    SignInBaseInfo getSignInfo(Integer uid);

    boolean saveRecord(Integer uid);
}
