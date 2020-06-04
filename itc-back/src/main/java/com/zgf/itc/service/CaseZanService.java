package com.zgf.itc.service;

import com.zgf.itc.entity.CaseZan;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ZGF
 */
public interface CaseZanService extends IService<CaseZan> {

    List<Integer> getCaseIdByUid(Integer yearSpace,Integer uid);

    boolean saveZan(CaseZan entity);

    boolean deleteZan(Integer id, Integer uid);
}
