package com.zgf.itc.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zgf.itc.entity.Case;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zgf.itc.vo.CaseVo;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author ZGF
 */
public interface CaseMapper extends BaseMapper<Case> {

    IPage<CaseVo> getAll(Page page,Integer uid, Integer yearSpace, Boolean orderByTime, Integer state);

    IPage<CaseVo> getBack(Page page, Integer state);
}
