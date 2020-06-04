package com.zgf.itc.mapper;

import com.zgf.itc.entity.SignIn;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author ZGF
 */
public interface SignInMapper extends BaseMapper<SignIn> {

    SignIn getByDay(Integer uid,Integer day);
}
