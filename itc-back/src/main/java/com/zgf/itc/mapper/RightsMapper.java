package com.zgf.itc.mapper;

import com.zgf.itc.entity.Rights;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author ZGF
 */
public interface RightsMapper extends BaseMapper<Rights> {

    List<Rights> getRightsByRoleId(Integer id);

    Integer getCount(Integer roleId, String path);
}
