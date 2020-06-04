package com.zgf.itc.service;

import com.zgf.itc.entity.Case;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zgf.itc.utils.QueryCondition;
import com.zgf.itc.vo.ShowData;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ZGF
 */
public interface CaseService extends IService<Case> {

    boolean saveCase(Case entity);

    ShowData getAll(QueryCondition query);

    ShowData getBack(QueryCondition query);

    boolean updateCase(Case entity);
}
