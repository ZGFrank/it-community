package com.zgf.itc.mapper;

import com.zgf.itc.entity.CaseZan;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author ZGF
 */
public interface CaseZanMapper extends BaseMapper<CaseZan> {

    List<Integer> getCaseIdByUid(Integer yearSpace,Integer uid);
}
